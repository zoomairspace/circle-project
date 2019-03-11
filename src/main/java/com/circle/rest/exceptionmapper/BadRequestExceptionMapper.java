package com.circle.rest.exceptionmapper;

import com.circle.exception.BadRequestException;
import com.google.common.base.Throwables;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Throwables.getStackTraceAsString(exception))
                .type(MediaType.TEXT_PLAIN).build();
    }
}
