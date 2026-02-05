package fr.fruityhedgeh0g.services.proxies;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import fr.fruityhedgeh0g.services.interfaces.PostService;
import io.smallrye.common.annotation.Identifier;
import io.vavr.control.Try;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Identifier("postProxy")
@AllArgsConstructor
@ApplicationScoped
public class PostProxy implements PostService {
    @Inject
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
