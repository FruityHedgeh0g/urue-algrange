package fr.fruityhedgeh0g.dtos.userDtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.groupDtos.NestedGroupDto;
import fr.fruityhedgeh0g.dtos.roleDtos.NestedRoleDto;
import fr.fruityhedgeh0g.dtos.Views;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class UserDto {

    @NotNull
    @JsonView({Views.Minimal.class,Views.Creation.class,Views.Update.class})
    @JsonAlias("user_id")
    UUID userId;

    @NotBlank(groups={Views.Creation.class})
    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    @JsonAlias({"first_name","updated_first_name"})
    String firstName;

    @NotBlank(groups={Views.Creation.class})
    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    @JsonAlias({"last_name","updated_last_name"})
    String lastName;

    @JsonView(Views.Detailed.class)
    Set<NestedRoleDto> roles;

    @JsonView(Views.Detailed.class)
    NestedGroupDto group;

    //INFO : Retrait des Sets au profit d'une méthode dans EventServiceImpl retournant ces infos pour un couple Utilisateur/EventType
//    @JsonView(Views.Detailed.class)
//    Set<EventEntity> organizedEvents;
//
//    @JsonView(Views.Detailed.class)
//    Set<EventEntity> participatedEvents;
//
//    @JsonView(Views.Detailed.class)
//    Set<EventEntity> createdEvents;

}
