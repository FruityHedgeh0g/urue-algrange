package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import fr.fruityhedgeh0g.entities.PostEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.PostRepository;
import fr.fruityhedgeh0g.services.interfaces.PostService;
import fr.fruityhedgeh0g.utilities.mappers.PostMapper;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Identifier("serviceImpl")
@Default
public class PostServiceImpl implements PostService {
    @Inject
    PostRepository postRepository;

    @Inject
    PostMapper postMapper;

    @Override
    @Transactional
    public Try<List<PostDto>> getAllPosts() {
        Log.info("Getting all posts");
        return Try.of(() -> postRepository
                .findAll()
                .stream()
                .map(postMapper::toDto)
                .toList())
                .onFailure(e ->
                        Log.error("Error getting all posts", e)
                );
    }

    @Override
    @Transactional
    public Try<PostDto> getPostById( UUID postId) {
        Log.infof("Getting post with id: %s", postId);
        return Try.of(() -> postRepository
                .findByIdOptional(postId)
                .orElseThrow(() -> new UnknownResourceException("Post not found: " + postId)))
                .map(postMapper::toDto)
                .onFailure(e -> {
                    if (e instanceof UnknownResourceException ex) {
                        Log.warn(ex.getMessage());
                    } else {
                        Log.errorf(e, "Error getting post with id: %s", postId);
                    }
                });
    }

    @Override
    @Transactional
    public Try<PostDto> createPost( PostDto postDto) {
        return null;
    }

    @Override
    @Transactional
    public Try<PostDto> updatePost( PostDto postDto) {
        return null;
    }

    @Override
    @Transactional
    public Try<Void> deletePost( UUID postId) {
        return null;
    }

    @Override
    @Transactional
    public Try<PostDto> addPostBanner(UUID postId, UUID bannerId) {
        return null;
    }

    @Override
    @Transactional
    public Try<PostDto> deletePostBanner(UUID postId) {
        return null;
    }

    @Override
    @Transactional
    public Try<PostDto> updatePostBanner(UUID postId, UUID tagId) {
        return null;
    }

    @Override
    @Transactional
    public Try<PostDto> addPostAttachment(UUID postId, UUID attachmentId) {
        return null;
    }

    @Override
    @Transactional
    public Try<PostDto> deletePostAttachment(UUID postId, UUID attachmentId) {
        return null;
    }

}
