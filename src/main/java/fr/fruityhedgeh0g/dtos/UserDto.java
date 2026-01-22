package fr.fruityhedgeh0g.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.entities.EventEntity;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.roles.RoleEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class UserDto {

    @JsonView({Views.Creation.class, Views.Basic.class , Views.Update.class})
    UUID userId;

    @JsonView({Views.Detailed.class, Views.Update.class})
    Set<RoleEntity> roles;

    @JsonView(Views.Detailed.class)
    GroupDto group;

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
