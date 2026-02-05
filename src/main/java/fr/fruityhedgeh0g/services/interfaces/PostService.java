package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import io.vavr.control.Try;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Try<List<PostDto>> getAllPosts();
    Try<PostDto> getPostById(@NotNull UUID postId);
    Try<PostDto> createPost(@NotNull PostDto postDto);
    Try<PostDto> updatePost(@NotNull PostDto postDto);
    void deletePost(@NotNull UUID postId);


}
