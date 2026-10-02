package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.utilities.logging.Logged;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.ForbiddenActionException;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
import fr.fruityhedgeh0g.security.Viewer;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalEventService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalGroupService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalUserService;
import fr.fruityhedgeh0g.utilities.mappers.GroupMapper;
import fr.fruityhedgeh0g.utilities.mappers.SectorMapper;
import io.quarkus.security.Authenticated;
import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Logged
@ApplicationScoped
@Default
public class SectorServiceImpl implements SectorService {

    @Inject SectorRepository sectorRepository;
    @Inject SectorMapper sectorMapper;
    @Inject GroupMapper groupMapper;
    @Inject InternalGroupService internalGroupService;
    @Inject InternalEventService internalEventService;
    @Inject Viewer viewer;

    @Override
    public List<SectorDto> listAll() {
        return sectorRepository.listAll()
                .stream()
                .filter(this::visible)
                .map(sectorMapper::toDto)
                .toList();
    }

    @Override
    public SectorDto getById(UUID sectorId) {
        return sectorMapper.toDto(sectorOrThrow(sectorId));
    }

    private boolean visible(SectorEntity sector) {
        return !sector.isClosed() || viewer.seesClosedSecteurs();
    }

    /** The Secteur, as not found when it is fermé and the viewer is not the Super admin. */
    private SectorEntity sectorOrThrow(UUID sectorId) {
        return sectorRepository.findByIdOptional(sectorId)
                .filter(this::visible)
                .orElseThrow(() -> new UnknownResourceException("Sector not found: " + sectorId));
    }

    /** The Bureau and Admin act only on their own Secteur; the Super admin on all (ADR 0004). */
    private void requireManaged(SectorEntity sector) {
        if (!viewer.scope().covers(sector))
            throw new ForbiddenActionException("The Secteur " + sector.getSectorId() + " is not yours.");
    }

    private static void refuseWhenClosed(SectorEntity sector) {
        if (sector.isClosed())
            throw new InvalidResourceException("A Secteur fermé is read-only: " + sector.getSectorId());
    }

    @Override
    @Transactional
    public SectorDto create(SectorDto sectorDto) {
        if (sectorRepository.existsByName(sectorDto.getName()))
            throw new DuplicateResourceException("A sector with this name already exists.");

        SectorEntity sectorEntity = sectorMapper.toEntity(sectorDto);
        sectorRepository.persist(sectorEntity);

        return sectorMapper.toDto(sectorEntity);
    }

    @Override
    @Transactional
    public SectorDto update(SectorDto sectorDto, boolean mayRename) {
        SectorEntity sectorEntity = sectorOrThrow(sectorDto.getSectorId());
        requireManaged(sectorEntity);
        refuseWhenClosed(sectorEntity);

        boolean renamed = sectorDto.getName() != null && !sectorEntity.getName().equals(sectorDto.getName());
        if (renamed && !mayRename)
            throw new ForbiddenActionException("Only the Super admin renames a Secteur.");
        if (renamed && sectorRepository.existsByName(sectorDto.getName()))
            throw new DuplicateResourceException("A sector with this name already exists in the system.");

        sectorEntity = sectorMapper.partialDtoToEntity(sectorEntity,sectorDto);
        sectorRepository.persist(sectorEntity);
        return sectorMapper.toDto(sectorEntity);
    }

    @Override
    @Transactional
    public SectorDto close(UUID sectorId) {
        SectorEntity sector = sectorOrThrow(sectorId);
        if (!sector.isClosed()) {
            sector.setClosed(true);
            // The Chefs keep their title; only the Affectations end
            sector.getGroups().forEach(group -> group.setChef(null));
            internalEventService.doCloseEventsOfSector(sectorId);
        }
        return sectorMapper.toDto(sector);
    }

    @Override
    @Transactional
    public SectorDto reopen(UUID sectorId) {
        SectorEntity sector = sectorOrThrow(sectorId);
        sector.setClosed(false);
        return sectorMapper.toDto(sector);
    }

    @Override
    @Transactional
    public void assignGroup(UUID sectorId, UUID groupId) {
        GroupEntity groupEntity = internalGroupService.doGetEntityById(groupId)
                .orElseThrow(() -> new UnknownResourceException("Group not found: " + groupId));

        SectorEntity sectorEntity = sectorOrThrow(sectorId);
        requireManaged(sectorEntity);
        refuseWhenClosed(sectorEntity);

        if (groupEntity.getSector() != null) {
            if (groupEntity.getSector().getSectorId().equals(sectorId)) return;
            else throw new DuplicateResourceException("Group already assigned to another sector");
        }

        sectorEntity.addGroup(groupEntity);

        sectorRepository.persist(sectorEntity);
    }



    @Override
    @Transactional
    public void unassignGroup(UUID sectorId, UUID groupId) {
        GroupEntity groupEntity = internalGroupService.doGetEntityById(groupId)
                .orElseThrow(() -> new UnknownResourceException("Group not found: " + groupId));

        if (groupEntity.getSector() == null) return;

        SectorEntity sectorEntity = sectorOrThrow(sectorId);
        requireManaged(sectorEntity);
        refuseWhenClosed(sectorEntity);

        if (!groupEntity.getSector().getSectorId().equals(sectorId))
            throw new InvalidResourceException("This group is assigned to another sector");

        sectorEntity.removeGroup(groupEntity);

        sectorRepository.persist(sectorEntity);
    }

    @Override
    public Optional<SectorEntity> doGetEntityById(UUID sectorId) {
        return sectorRepository.findByIdOptional(sectorId);
    }

}
