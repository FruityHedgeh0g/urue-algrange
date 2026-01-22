package fr.fruityhedgeh0g.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Value;

import java.util.UUID;

@Value
public class RoleDto {

    @JsonView({Views.CreationResponse.class, Views.Basic.class})
    UUID roleId;

    @JsonView({Views.Creation.class, Views.Basic.class})
    String name;

    @JsonView({Views.Creation.class,Views.Basic.class})
    String description;

    @JsonView({Views.Creation.class,Views.Basic.class})
    String roleType;
}
