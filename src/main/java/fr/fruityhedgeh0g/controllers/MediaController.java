package fr.fruityhedgeh0g.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.dtos.Views;
import fr.fruityhedgeh0g.services.interfaces.MediaService;
import fr.fruityhedgeh0g.services.proxies.MediaProxy;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.annotation.Identifier;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/medias")
public class MediaController {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken token;

    @Inject
    @Identifier( "mediaProxy")
    MediaService mediaService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public @JsonView(Views.Basic.class) List<MediaDto> getAllMedias(){
        return mediaService.getAllMedia().get();
    }
}
