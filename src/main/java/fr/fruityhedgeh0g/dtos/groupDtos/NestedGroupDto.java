package fr.fruityhedgeh0g.dtos.groupDtos;

import fr.fruityhedgeh0g.dtos.sectorDtos.NestedSectorDto;

import java.util.UUID;

public record NestedGroupDto(UUID groupId, String name, NestedSectorDto sector){}
