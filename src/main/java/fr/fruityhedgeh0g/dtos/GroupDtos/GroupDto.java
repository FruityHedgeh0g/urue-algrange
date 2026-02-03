package fr.fruityhedgeh0g.dtos.GroupDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.SectorDtos.NestedSectorDto;
import fr.fruityhedgeh0g.dtos.UserDtos.NestedUserDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
public class GroupDto {

    @JsonView({Views.Minimal.class,Views.CreationResponse.class,Views.UpdateResponse.class})
    UUID groupId;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String name;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String description;

    @JsonView(Views.Detailed.class)
    Set<NestedUserDto> members;

    @JsonView(Views.Detailed.class)
    NestedSectorDto sector;

}
