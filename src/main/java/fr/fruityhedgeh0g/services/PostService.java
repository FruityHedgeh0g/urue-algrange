package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.model.dtos.PostDto;
import fr.fruityhedgeh0g.repositories.PostRepository;
import fr.fruityhedgeh0g.utilities.mappers.PostMapper;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
public class PostService {
    @Inject
    PostRepository postRepository;

    @Inject
    PostMapper postMapper;

    public Try<List<PostDto>> getAllPosts() {
        Log.debug("Getting all posts");
        return Try.of(() -> postRepository
                .findAll()
                .stream()
                .map(postMapper::toDto)
                .toList())
                .onFailure(e -> Log.error("Error getting all posts", e));
    }

    public Try<PostDto> getPostById(@NotNull UUID postId) {
        Log.debug("Getting post with id: " + postId);
        return Try.of(() -> postRepository
                        .findByIdOptional(postId)
                        .orElseThrow(NoSuchElementException::new))
                .map(postMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof NoSuchElementException) {
                        Log.warn("Post not found: " + postId);
                    }else {
                        Log.error("Error getting post with id: " + postId, e);
                    }
                });
    }

    public Try<PostDto> createPost(@NotNull PostDto postDto) {
        Log.debug("Creating post: " + postDto.getPostId());
        return null;
    }

    public Try<PostDto> updatePost(@NotNull PostDto postDto) {
        Log.debug("Updating post: " + postDto.getPostId());
        return null;
    }

    public void deletePost(@NotNull UUID postId) {
        Log.debug("Deleting post with id: " + postId);
        Try.of(() -> postRepository.deleteById(postId))
                .onFailure(e -> Log.error("Error deleting post with id: " + postId, e));
    }
}
