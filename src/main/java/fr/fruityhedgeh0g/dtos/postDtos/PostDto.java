package fr.fruityhedgeh0g.dtos.postDtos;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.mediaDtos.NestedMediaDto;
import fr.fruityhedgeh0g.dtos.Views;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class PostDto {

    @JsonView({Views.Minimal.class,Views.CreationResponse.class,Views.UpdateResponse.class})
    UUID postId;

    @JsonView({Views.Basic.class,Views.Creation.class,Views.Update.class})
    String  title;

    @JsonView({Views.Detailed.class,Views.Creation.class,Views.Update.class})
    String content;

    @JsonView({Views.Detailed.class})
    NestedMediaDto banner;

    @JsonView({Views.Detailed.class})
    List<NestedMediaDto> attachments;
}
