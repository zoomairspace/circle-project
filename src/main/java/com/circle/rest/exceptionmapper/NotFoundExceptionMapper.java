package com.circle.rest.exceptionmapper;

import com.circle.exception.NotFoundException;
import com.google.common.base.Throwables;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(Throwables.getStackTraceAsString(exception))
                .type(MediaType.TEXT_PLAIN).build();
    }
}
