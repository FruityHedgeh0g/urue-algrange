package fr.fruityhedgeh0g.services.interfaces.publics;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicPostService {

    List<PostDto> listAll();
    PostDto getById(@NotNull UUID postId);
    PostDto create(@NotNull @Valid PostDto postDto);
    PostDto update(@NotNull @Valid PostDto postDto);
    void delete(@NotNull UUID postId);

//    Try<List<PostDto>> getAllPosts();
//    Try<PostDto> getPostById(@NotNull UUID postId);
//    Try<PostDto> createPost(@NotNull @Valid PostDto postDto);
//    Try<PostDto> updatePost(@NotNull @Valid PostDto postDto);
//    Try<Void> deletePost(@NotNull UUID postId);
//    Try<PostDto> addPostBanner(@NotNull UUID postId, @NotNull UUID bannerId);
//    Try<PostDto> deletePostBanner(@NotNull UUID postId);
//    Try<PostDto> updatePostBanner(@NotNull UUID postId, @NotNull UUID tagId);
//    Try<PostDto> addPostAttachment(@NotNull UUID postId, @NotNull UUID attachmentId);
//    Try<PostDto> deletePostAttachment(@NotNull UUID postId, @NotNull UUID attachmentId);


}
