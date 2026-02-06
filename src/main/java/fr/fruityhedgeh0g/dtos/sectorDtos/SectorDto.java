package fr.fruityhedgeh0g.dtos.sectorDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.groupDtos.NestedGroupDto;
import fr.fruityhedgeh0g.dtos.Views;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class SectorDto {

    @NotNull
    @JsonView({Views.Minimal.class,Views.CreationResponse.class,Views.UpdateResponse.class})
    UUID sectorId;

    @NotBlank
    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String name;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String description;

    @JsonView(Views.Detailed.class)
    Set<NestedGroupDto> groups;

}

