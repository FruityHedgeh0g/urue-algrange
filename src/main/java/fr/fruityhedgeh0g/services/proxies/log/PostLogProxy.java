package fr.fruityhedgeh0g.services.proxies.log;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import fr.fruityhedgeh0g.services.interfaces.PostService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Priority(200)
@Decorator
public class PostLogProxy implements PostService {

    @Inject
    @Delegate
    PostService postService;

    @Override
    public List<PostDto> listAll() {
        Log.debugf("Trying to retrieve all posts.");
        return Try.of(postService::listAll)
                .onSuccess(posts -> Log.debugf("%d posts retrieved.",posts.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving posts."))
                .get();
    }

    @Override
    public Optional<PostDto> getById(UUID postId) {
        Log.debugf("Trying to retrieve post by id %s.",postId);
        return Try.of(() -> postService.getById(postId))
                .onSuccess(post -> {
                    if (post.isPresent())
                        Log.debugf("Post retrieved.");
                    else Log.debugf("There is no post with id %s.",postId);
                })
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving post."))
                .get();
    }

    @Override
    public PostDto create(PostDto postDto) {
        return null;
    }

    @Override
    public PostDto update(PostDto postDto) {
        return null;
    }

    @Override
    public void delete(UUID postId) {

    }
}
