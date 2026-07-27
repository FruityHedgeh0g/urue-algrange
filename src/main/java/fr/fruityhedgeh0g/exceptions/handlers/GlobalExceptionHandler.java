package fr.fruityhedgeh0g.exceptions.handlers;

import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.NotImplementedYetException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;


public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public RestResponse<Void> mapException(Exception x) {

        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR);
    }

    @ServerExceptionMapper
    public RestResponse<Void> mapDuplicateResourceException(DuplicateResourceException x) {
        return RestResponse.status(Response.Status.CONFLICT);
    }

    @ServerExceptionMapper
    public RestResponse<Void> mapUnknownResourceException(UnknownResourceException x) {
        return RestResponse.status(Response.Status.NOT_FOUND);
    }

    @ServerExceptionMapper
    public RestResponse<Void> mapInvalidResourceException(InvalidResourceException x) {
        return RestResponse.status(Response.Status.BAD_REQUEST);
    }

    @ServerExceptionMapper
    public RestResponse<Void> mapNotImplementedYetException(NotImplementedYetException x) {
        return RestResponse.status(Response.Status.NOT_IMPLEMENTED);
    }


}
