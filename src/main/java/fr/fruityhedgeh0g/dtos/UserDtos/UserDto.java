package fr.fruityhedgeh0g.dtos.UserDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.GroupDtos.GroupDto;
import fr.fruityhedgeh0g.dtos.GroupDtos.NestedGroupDto;
import fr.fruityhedgeh0g.dtos.RoleDtos.NestedRoleDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class UserDto {

    @JsonView({Views.Minimal.class,Views.Creation.class,Views.Update.class})
    UUID userId;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String firstName;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String lastName;

    @JsonView(Views.Detailed.class)
    Set<NestedRoleDto> roles;

    @JsonView(Views.Detailed.class)
    NestedGroupDto group;

    //INFO : Retrait des Sets au profit d'une méthode dans EventService retournant ces infos pour un couple Utilisateur/EventType
//    @JsonView(Views.Detailed.class)
//    Set<EventEntity> organizedEvents;
//
//    @JsonView(Views.Detailed.class)
//    Set<EventEntity> participatedEvents;
//
//    @JsonView(Views.Detailed.class)
//    Set<EventEntity> createdEvents;

}
