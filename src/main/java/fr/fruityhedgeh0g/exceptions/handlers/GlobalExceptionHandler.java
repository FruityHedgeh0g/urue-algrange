package fr.fruityhedgeh0g.exceptions.handlers;

import fr.fruityhedgeh0g.exceptions.DuplicateEntityException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.util.NoSuchElementException;


public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public RestResponse<String> mapException(Exception x) {
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR);
    }

    @ServerExceptionMapper
    public RestResponse<String> mapDuplicateEntityException(DuplicateEntityException x) {
        return RestResponse.status(Response.Status.CONFLICT);
    }

    @ServerExceptionMapper
    public RestResponse<String> mapNoSuchElementException(NoSuchElementException x) {
        return RestResponse.status(Response.Status.NOT_FOUND);
    }
}
