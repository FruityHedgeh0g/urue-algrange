package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import fr.fruityhedgeh0g.services.interfaces.PostService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Alternative
@Priority(1)
@Authenticated
@IfBuildProperty(name = "quarkus.oidc.enabled", stringValue = "true")
public class PostProxy implements PostService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier("serviceImpl")
    PostService postService;

    @Override
    public Try<List<PostDto>> getAllPosts() {
        return postService.getAllPosts();
    }

    @Override
    public Try<PostDto> getPostById(UUID postId) {
        return postService.getPostById(postId);
    }

    @Override
    public Try<PostDto> createPost(PostDto postDto) {
        return postService.createPost(postDto);
    }

    @Override
    public Try<PostDto> updatePost(PostDto postDto) {
        return postService.updatePost(postDto);
    }

    @Override
    public Try<Void> deletePost(UUID postId) {

        return postService.deletePost(postId);
    }

    @Override
    public Try<PostDto> addPostBanner(UUID postId, UUID bannerId) {
        return postService.addPostBanner(postId, bannerId);
    }

    @Override
    public Try<PostDto> deletePostBanner(UUID postId) {
        return postService.deletePostBanner(postId);
    }

    @Override
    public Try<PostDto> updatePostBanner(UUID postId, UUID tagId) {
        return postService.updatePostBanner(postId, tagId);
    }

    @Override
    public Try<PostDto> addPostAttachment(UUID postId, UUID attachmentId) {
        return postService.addPostAttachment(postId, attachmentId);
    }

    @Override
    public Try<PostDto> deletePostAttachment(UUID postId, UUID attachmentId) {
        return postService.deletePostAttachment(postId, attachmentId);
    }
}
