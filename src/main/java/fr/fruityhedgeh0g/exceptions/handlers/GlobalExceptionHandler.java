package fr.fruityhedgeh0g.exceptions.handlers;

import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;


public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public RestResponse<String> mapException(Exception x) {
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR);
    }

    @ServerExceptionMapper
    public RestResponse<String> mapDuplicateResourceException(DuplicateResourceException x) {
        return RestResponse.status(Response.Status.CONFLICT);
    }

    @ServerExceptionMapper
    public RestResponse<String> mapUnknownResourceException(UnknownResourceException x) {
        return RestResponse.status(Response.Status.NOT_FOUND);
    }

    @ServerExceptionMapper
    public RestResponse<String> mapInvalidResourceException(InvalidResourceException x) {
        return RestResponse.status(Response.Status.BAD_REQUEST);
    }


}
