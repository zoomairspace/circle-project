package com.circle.rest.exceptionmapper;

import com.circle.exception.InternalServerException;
import com.google.common.base.Throwables;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InternalServerExceptionMapper implements ExceptionMapper<InternalServerException> {

    @Override
    public Response toResponse(InternalServerException exception) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Throwables.getStackTraceAsString(exception))
                .type(MediaType.TEXT_PLAIN).build();
    }
}
