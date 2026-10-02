package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import fr.fruityhedgeh0g.services.interfaces.publics.PublicMediaService;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaService extends PublicMediaService {

//    Try<List<MediaDto>> getAllMedia();
//    Try<MediaDto> getMediaById(@NotNull UUID mediaId);
//    Try<MediaDto> createMedia(@NotNull @Valid MediaDto mediaDto);
//    Try<MediaDto> updateMedia(@NotNull @Valid MediaDto mediaDto);
//    Try<Void> deleteMedia(@NotNull UUID mediaId);
}
