package fr.fruityhedgeh0g.exceptions.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;


public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public RestResponse<String> mapException(Exception x) {
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR);
    }
}
