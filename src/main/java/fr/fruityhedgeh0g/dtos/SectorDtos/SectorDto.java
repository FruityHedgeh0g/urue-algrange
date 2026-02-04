package fr.fruityhedgeh0g.dtos.SectorDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.GroupDtos.NestedGroupDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.entities.GroupEntity;
import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class SectorDto {

    @JsonView({Views.Minimal.class,Views.CreationResponse.class,Views.UpdateResponse.class}) UUID sectorId;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String name;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String description;

    @JsonView(Views.Detailed.class)
    Set<NestedGroupDto> groups;

}

