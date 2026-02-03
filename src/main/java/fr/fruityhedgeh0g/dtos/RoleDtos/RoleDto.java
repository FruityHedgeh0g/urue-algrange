package fr.fruityhedgeh0g.dtos.RoleDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.Views;
import lombok.Value;

import java.util.UUID;

@Value
public class RoleDto {

    @JsonView({Views.Minimal.class,Views.CreationResponse.class,Views.UpdateResponse.class})
    UUID roleId;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String name;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String description;

    @JsonView({Views.Detailed.class,Views.Creation.class,Views.Update.class})
    String roleType;
}
