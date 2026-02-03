package fr.fruityhedgeh0g.dtos.GroupDtos;

import fr.fruityhedgeh0g.dtos.SectorDtos.NestedSectorDto;
import fr.fruityhedgeh0g.entities.SectorEntity;
import lombok.Value;

import java.util.UUID;

public record NestedGroupDto(UUID groupId, String name, NestedSectorDto sector){}
