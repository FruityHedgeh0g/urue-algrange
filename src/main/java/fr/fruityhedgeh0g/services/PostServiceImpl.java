package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.utilities.logging.Logged;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import fr.fruityhedgeh0g.entities.PostEntity;
import fr.fruityhedgeh0g.enums.PostStatusEnum;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.NotImplementedYetException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.PostRepository;
import fr.fruityhedgeh0g.services.interfaces.PostService;
import fr.fruityhedgeh0g.services.interfaces.internals.InternalUserService;
import fr.fruityhedgeh0g.utilities.mappers.PostMapper;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Logged
@ApplicationScoped
@Default
public class PostServiceImpl implements PostService {
    @Inject
    PostRepository postRepository;

    @Inject
    PostMapper postMapper;

    @Inject
    InternalUserService internalUserService;

    @Override
    public List<PostDto> listAll(boolean seesDrafts) {
        List<PostEntity> posts = seesDrafts ? postRepository.listAll() : postRepository.list("status", PostStatusEnum.PUBLIE);
        return posts.stream().map(postMapper::toDto).toList();
    }

    @Override
    public PostDto getById(UUID postId, boolean seesDrafts) {
        return postMapper.toDto(
                postRepository.findByIdOptional(postId)
                        .filter(post -> seesDrafts || post.isPublished())
                        .orElseThrow(() -> new UnknownResourceException("Post not found: "+postId))
        );
    }

    @Override
    @Transactional
    public PostDto create(PostDto postDto, UUID authorId) {
        PostEntity post = postMapper.toEntity(postDto);
        post.setStatus(PostStatusEnum.BROUILLON);
        post.setAuthor(internalUserService.doGetEntityById(authorId)
                .orElseThrow(() -> new UnknownResourceException("User not found: " + authorId)));
        validate(post);
        postRepository.persist(post);
        return postMapper.toDto(post);
    }

    @Override
    @Transactional
    public PostDto update(PostDto postDto) {
        if (postDto.getPostId() == null) throw new InvalidResourceException("Missing post id.");
        PostEntity post = postOrThrow(postDto.getPostId());
        postMapper.partialDtoToEntity(post, postDto);
        validate(post);
        return postMapper.toDto(post);
    }

    @Override
    @Transactional
    public PostDto changeStatus(UUID postId, PostStatusEnum status) {
        PostEntity post = postOrThrow(postId);
        post.setStatus(status);
        return postMapper.toDto(post);
    }

    private PostEntity postOrThrow(UUID postId) {
        return postRepository.findByIdOptional(postId)
                .orElseThrow(() -> new UnknownResourceException("Post not found: " + postId));
    }

    /** A Post has a title and a content. */
    private static void validate(PostEntity post) {
        if (post.getTitle() == null || post.getTitle().isBlank())
            throw new InvalidResourceException("A Post has a title.");
        if (post.getContent() == null || post.getContent().isBlank())
            throw new InvalidResourceException("A Post has a content.");
    }

    @Override
    @Transactional
    public void delete(UUID postId) {
        throw new NotImplementedYetException(this.getClass().getSimpleName());
    }

//    @Override
//    @Transactional
//    public Try<List<PostDto>> getAllPosts() {
//        Log.info("Getting all posts");
//        return Try.of(() -> postRepository
//                .findAll()
//                .stream()
//                .map(postMapper::toDto)
//                .toList())
//                .onFailure(e ->
//                        Log.error("Error getting all posts", e)
//                );
//    }
//
//    @Override
//    @Transactional
//    public Try<PostDto> getPostById( UUID postId) {
//        Log.infof("Getting post with id: %s", postId);
//        return Try.of(() -> postRepository
//                .findByIdOptional(postId)
//                .orElseThrow(() -> new UnknownResourceException("Post not found: " + postId)))
//                .map(postMapper::toDto)
//                .onFailure(e -> {
//                    if (e instanceof UnknownResourceException ex) {
//                        Log.warn(ex.getMessage());
//                    } else {
//                        Log.errorf(e, "Error getting post with id: %s", postId);
//                    }
//                });
//    }
//
//    @Override
//    @Transactional
//    public Try<PostDto> createPost( PostDto postDto) {
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public Try<PostDto> updatePost( PostDto postDto) {
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public Try<Void> deletePost( UUID postId) {
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public Try<PostDto> addPostBanner(UUID postId, UUID bannerId) {
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public Try<PostDto> deletePostBanner(UUID postId) {
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public Try<PostDto> updatePostBanner(UUID postId, UUID tagId) {
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public Try<PostDto> addPostAttachment(UUID postId, UUID attachmentId) {
//        return null;
//    }
//
//    @Override
//    @Transactional
//    public Try<PostDto> deletePostAttachment(UUID postId, UUID attachmentId) {
//        return null;
//    }

}
