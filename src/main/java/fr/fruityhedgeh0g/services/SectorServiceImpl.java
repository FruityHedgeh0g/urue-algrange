package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.dtos.sectorDtos.SectorDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.services.interfaces.GroupService;
import fr.fruityhedgeh0g.services.interfaces.SectorService;
import fr.fruityhedgeh0g.services.interfaces.internal.InternalGroupService;
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
@Identifier("serviceImpl")
@Default
public class SectorServiceImpl implements SectorService {
    @Inject
    SectorRepository sectorRepository;

//    @Inject
//    InternalGroupService internalGroupService;

    @Inject
    SectorMapper sectorMapper;

    @Inject
    GroupMapper groupMapper;

    @Override
    public List<SectorDto> listAll() {
        return sectorRepository.listAll()
                .stream()
                .map(sectorMapper::toDto)
                .toList();
    }

    @Override
    public Optional<SectorDto> getById(UUID sectorId) {
        return sectorRepository.findByIdOptional(sectorId)
                .map(sectorMapper::toDto);
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
                .orElseThrow(() -> new UnknownResourceException("This resource is unknown in the system and cannot be updated."));

        if (!sectorEntity.getName().equals(sectorDto.getName()) && sectorRepository.existsByName(sectorDto.getName()))
            throw new DuplicateResourceException("A sector with this name already exists in the system.");

        sectorEntity = sectorMapper.partialDtoToEntity(sectorEntity,sectorDto);
        sectorRepository.persist(sectorEntity);
        return sectorMapper.toDto(sectorEntity);
    }

    @Override
    @Transactional
    public void delete(UUID sectorId) {
        //todo: développer la suppression.
        sectorRepository.deleteById(sectorId);
    }

    @Override
    @Transactional
    public void assignGroup(UUID sectorId, UUID groupId) {
        SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorId)
                .orElseThrow(() -> new UnknownResourceException("Sector not found: " + sectorId));

//        GroupEntity groupEntity = internalGroupService.getInternalEntityById(groupId)
//                .orElseThrow(() -> new UnknownResourceException("Group not found: " + groupId));
//
//        if (groupEntity.getSector() != null) {
//            if (groupEntity.getSector().getSectorId().equals(sectorId)) return;
//            throw new DuplicateResourceException("Group already assigned to another sector");
//        }
//
//        sectorEntity.addGroup(groupEntity);
//
//        sectorRepository.persist(sectorEntity);
    }



    @Override
    @Transactional
    public void unassignGroup(UUID sectorId, UUID groupId) {

    }


//
//    @Override
//    @Transactional
//    public Try<SectorDto> assignGroupToSector( UUID sectorId,  UUID groupId) {
//        Log.infof("Assigning group with id: %s to sector with id: %s", groupId, sectorId);
//        return Try.of(()-> {
//            Log.debugf("Checking if sector with id: %s exists and retrieve it", sectorId);
//            SectorEntity sectorEntity = sectorRepository.findByIdOptional(sectorId)
//                    .orElseThrow(() -> new UnknownResourceException("Sector not found: " + sectorId));
//
//            Log.debugf("Checking if group with id: %s is already assigned to this sector", groupId);
//            if (sectorEntity.getGroups().stream().anyMatch(e -> e.getGroupId().equals(groupId)))
//                throw new DuplicateResourceException("Group already belongs to this sector");
//
//
//            Log.debugf("Checking if group with id: %s exists and retrieve it", groupId);
//            GroupEntity groupEntity = groupService.internalGetEntityById(groupId).getOrElseThrow(ex -> ex);
//
//            Log.debugf("Checking if group with id: %s is already assigned to a sector", groupId);
//            if (groupEntity.getSector() != null) throw new DuplicateResourceException("Group already belongs to a sector");
//
//            sectorEntity.addGroup(groupEntity);
//            return sectorMapper.toDto(sectorEntity);
//        }).onFailure(ex -> {
//            if (ex instanceof UnknownResourceException) {
//                Log.warn(ex.getMessage());
//            } else {
//                Log.errorf(ex, "Error assigning group with id: %s to sector with id: %s", groupId, sectorId);
//            }
//        });
//    }
//
//    @Override
//    @Transactional
//    public Try<SectorDto> unassignGroupFromSector( UUID sectorId,  UUID groupId) {
//        Log.infof("Unassigning group with id: %s from sector with id: %s", groupId, sectorId);
//        return Try.of(() -> {
//            Log.debugf("Checking if sector with id: %s exists", sectorId);
//            SectorEntity sector = sectorRepository.findByIdOptional(sectorId)
//                .orElseThrow(() -> new UnknownResourceException("Sector not found:" + sectorId));
//
//
//            Log.debugf("Checking if group with id: %s is assigned to this sector and retrieve it", groupId);
//            GroupEntity groupEntity = sector.getGroups().stream()
//                    .filter(e -> e.getGroupId().equals(groupId))
//                    .findFirst().orElseThrow(() -> new UnknownResourceException("Group is not assigned to this sector"));
//
//            sector.removeGroup(groupEntity);
//
//            return sectorMapper.toDto(sector);
//        }).onFailure(ex -> {
//            if (ex instanceof UnknownResourceException) {
//                Log.warn(ex.getMessage());
//            } else {
//                Log.errorf(ex, "Error unassigning group with id: %s from sector with id: %s", groupId, sectorId);
//            }
//        });
//    }
//
//
//    @Override
//    @Transactional
//    public Try<Void> deleteSector( UUID sectorId) {
//        Log.infof("Deleting sector with id: %s", sectorId);
//        return Try.run(() -> {
//            Log.debugf("Checking if sector with id: %s exists", sectorId);
//            SectorEntity sector =sectorRepository.findByIdOptional(sectorId)
//                        .orElseThrow(() -> new UnknownResourceException("Sector not found:" + sectorId));
//
//            sector.getGroups().forEach(group -> group.setSector(null));
//
//            Log.debugf("Deleting sector with id: %s", sectorId);
//            sectorRepository.delete(sector);
//        }).onFailure(ex -> {
//            if (ex instanceof UnknownResourceException) {
//                Log.warn(ex.getMessage());
//            } else {
//                Log.errorf(ex, "Error deleting sector with id: %s", sectorId);
//            }
//        });
//    }
}
