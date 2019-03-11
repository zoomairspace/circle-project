package com.circle.rest;

import com.circle.api.DataBuffer;
import com.circle.data.rest.CurrencyPair;
import com.circle.data.rest.GetOrderVolume;
import com.circle.data.rest.OrderSide;
import com.circle.exception.BadRequestException;
import com.circle.exception.InternalServerException;
import com.circle.exception.NotFoundException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource
 */
@Path("/")
public class Resource {

	@Inject
	private DataBuffer dataBuffer;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{currency_pair}")
	public Response getOrderVolume(@PathParam("currency_pair") String currencyPair,
								   @QueryParam("price") double price,
								   @QueryParam("side") String side) {

    	try {
			final GetOrderVolume order = dataBuffer
					.lookup(CurrencyPair.BTC_ETH, price, OrderSide.valueOf(side.toUpperCase()));
			final JSONObject json = new JSONObject()
					.put("amount", order.getVolume())
					.put("last_update", order.getLastUpdate());
			return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
    		if (e instanceof IllegalArgumentException) {
				throw new BadRequestException(e);
			} else if (e instanceof  NotFoundException) {
    			throw e;
			} else {
    			throw new InternalServerException(e);
			}
		}
	}
}
