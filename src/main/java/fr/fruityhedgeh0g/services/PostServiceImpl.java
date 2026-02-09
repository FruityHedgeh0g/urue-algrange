package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import fr.fruityhedgeh0g.entities.PostEntity;
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

    @Transactional
    public Try<List<PostDto>> getAllPosts() {
        return null;
    }

    @Override
    public Try<List<PostDto>> getAllPostsFiltered(String filter) {
        return null;
    }

    @Transactional
    public Try<PostDto> getPostById( UUID postId) {
        return null;
    }

    @Transactional
    public Try<PostDto> createPost( PostDto postDto) {
        return null;
    }

    //TODO : Développer l'update
    @Transactional
    public Try<PostDto> updatePost( PostDto postDto) {
        return null;
    }

    //TODO : Gérer la suppression des références sur les autres tables (Côté Entity)
    @Transactional
    public Try<Void> deletePost( UUID postId) {
        return null;
    }

    @Override
    public Try<PostDto> addPostBanner(UUID postId, UUID bannerId) {
        return null;
    }

    @Override
    public Try<PostDto> deletePostBanner(UUID postId) {
        return null;
    }

    @Override
    public Try<PostDto> updatePostBanner(UUID postId, UUID tagId) {
        return null;
    }

    @Override
    public Try<PostDto> addPostAttachment(UUID postId, UUID attachmentId) {
        return null;
    }

    @Override
    public Try<PostDto> deletePostAttachment(UUID postId, UUID attachmentId) {
        return null;
    }

}
