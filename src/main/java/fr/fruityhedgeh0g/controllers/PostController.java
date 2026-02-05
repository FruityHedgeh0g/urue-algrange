package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.PostService;
import fr.fruityhedgeh0g.services.proxies.PostProxy;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/posts")
public class PostController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier( "postProxy")
    PostService postService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<PostDto> getAllPosts(){
        return postService.getAllPosts().get();
    }
}
