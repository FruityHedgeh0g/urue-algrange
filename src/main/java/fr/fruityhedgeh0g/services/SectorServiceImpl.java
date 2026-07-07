package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalGroupService;
import fr.fruityhedgeh0g.utilities.mappers.GroupMapper;
import fr.fruityhedgeh0g.utilities.mappers.SectorMapper;
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
@ApplicationScoped
@Default
public class SectorServiceImpl implements SectorService {

    @Inject SectorRepository sectorRepository;
    @Inject SectorMapper sectorMapper;
    @Inject GroupMapper groupMapper;
    @Inject InternalGroupService internalGroupService;

    @Override
    public List<SectorDto> listAll() {
        return sectorRepository.listAll()
                .stream()
                .map(sectorMapper::toDto)
                .toList();
    }

    @Override
    public SectorDto getById(UUID sectorId) {
        return sectorMapper.toDto(
                sectorRepository.findByIdOptional(sectorId)
                        .orElseThrow(() -> new UnknownResourceException("Sector not found: " + sectorId))
        );


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
    public SectorDto update(SectorDto sectorDto) {
        SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorDto.getSectorId())
                .orElseThrow(() -> new UnknownResourceException("Sector not found: " + sectorDto.getSectorId()));

        if (!sectorEntity.getName().equals(sectorDto.getName()) && sectorRepository.existsByName(sectorDto.getName()))
            throw new DuplicateResourceException("A sector with this name already exists in the system.");

        sectorEntity = sectorMapper.partialDtoToEntity(sectorEntity,sectorDto);
        sectorRepository.persist(sectorEntity);
        return sectorMapper.toDto(sectorEntity);
    }

    @Override
    @Transactional
    public void delete(UUID sectorId) {
        SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorId)
                .orElseThrow(() -> new UnknownResourceException("Sector not found: " + sectorId));

        if (!sectorEntity.getGroups().isEmpty())
            throw new InvalidResourceException("Sector cannot be deleted because it contains groups");

        sectorRepository.deleteById(sectorId);
    }

    @Override
    @Transactional
    public void assignGroup(UUID sectorId, UUID groupId) {
        GroupEntity groupEntity = internalGroupService.getEntityById(groupId)
                .orElseThrow(() -> new UnknownResourceException("Group not found: " + groupId));

        SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorId)
                .orElseThrow(() -> new UnknownResourceException("Sector not found: " + sectorId));

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
        GroupEntity groupEntity = internalGroupService.getEntityById(groupId)
                .orElseThrow(() -> new UnknownResourceException("Group not found: " + groupId));

        if (groupEntity.getSector() == null) return;

        SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorId)
                .orElseThrow(() -> new UnknownResourceException("Sector not found: " + sectorId));

        if (!groupEntity.getSector().getSectorId().equals(sectorId))
            throw new InvalidResourceException("This group is assigned to another sector");

        sectorEntity.removeGroup(groupEntity);

        sectorRepository.persist(sectorEntity);
    }

}
