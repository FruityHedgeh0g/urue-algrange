package fr.fruityhedgeh0g.dtos.roleDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.dtos.userDtos.NestedUserDto;
import fr.fruityhedgeh0g.enums.RoleTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class RoleDto {

    @NotNull
    @JsonView({Views.Minimal.class,Views.CreationResponse.class,Views.UpdateResponse.class})
    UUID roleId;

    @NotBlank
    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String name;

    @NotBlank
    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String description;

    @NotBlank
    @JsonView({Views.Detailed.class,Views.Creation.class,Views.Update.class})
    RoleTypeEnum roleType;

    @JsonView({Views.Detailed.class})
    Set<NestedUserDto> users;
}
