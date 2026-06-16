package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import fr.fruityhedgeh0g.dtos.roleDtos.RoleDto;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface PostService {

    Try<List<PostDto>> listAll();
    Try<PostDto> getById(@NotNull UUID postId);
    Try<PostDto> create(@NotNull @Valid PostDto postDto);
    Try<PostDto> update(@NotNull @Valid PostDto postDto);
    Try<PostDto> delete(@NotNull UUID postId);

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
