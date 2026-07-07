package fr.fruityhedgeh0g.services.proxies.log;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.services.interfaces.PostService;
import io.quarkus.logging.Log;
import io.vavr.control.Try;
import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@Priority(200)
@Decorator
public class PostLogProxy implements PostService {

    @Inject
    @Delegate
    PostService postService;

    @Override
    public List<PostDto> listAll() {
        Log.debugf("Retrieving all posts...");
        return Try.of(postService::listAll)
                .onSuccess(posts -> Log.debugf("%d posts retrieved.",posts.size()))
                .onFailure(t -> Log.errorf(t,"An error occurred while retrieving posts."))
                .get();
    }

    @Override
    public PostDto getById(UUID postId) {
        Log.debugf("Retrieving post by id %s...",postId);
        return Try.of(() -> postService.getById(postId))
                .onSuccess(post -> {
                        Log.debugf("Post retrieved: "+post.toString());
                })
                .onFailure(t -> {
                    switch(t){
                        case UnknownResourceException ex -> Log.errorf(ex,"Post with id %s not found.", postId);
                        default -> Log.errorf(t,"An error occurred while retrieving post.");
                    }
                })
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
