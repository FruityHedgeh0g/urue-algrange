package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import fr.fruityhedgeh0g.services.interfaces.PostService;
import io.quarkus.arc.properties.IfBuildProperty;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@ApplicationScoped
@Identifier("proxy")
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
        return null;
    }

    @Override
    public Try<PostDto> getPostById(UUID postId) {
        return null;
    }

    @Override
    public Try<PostDto> createPost(PostDto postDto) {
        return null;
    }

    @Override
    public Try<PostDto> updatePost(PostDto postDto) {
        return null;
    }

    @Override
    public void deletePost(UUID postId) {

    }
}
