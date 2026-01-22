package fr.fruityhedgeh0g.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
public class GroupDto {

    @JsonView({Views.CreationResponse.class,Views.Basic.class})
    UUID groupId;

    @JsonView({Views.Creation.class,Views.Basic.class})
    String name;

    @JsonView({Views.Creation.class,Views.Basic.class})
    String description;

    Set<UserEntity> members;

    @JsonView({Views.Creation.class,Views.Basic.class})
    SectorEntity sector;

}
