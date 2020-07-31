package com.modzy.sdk.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modzy.sdk.util.LoggerFactory;

public class LoggingFilter implements ClientRequestFilter, ClientResponseFilter {

	private Logger logger;
	private ObjectMapper objectMapper;
	
	public LoggingFilter() {
		super();
		this.logger = LoggerFactory.getLogger(this);
		this.objectMapper = new ObjectMapper();		
	}
	
	public void filter(ClientRequestContext requestContext) throws IOException {		
		if( requestContext.hasEntity() ) {
			this.logger.info(this.objectMapper/*.writerWithDefaultPrettyPrinter()*/.writeValueAsString(requestContext.getEntity()));
		}				
	}

	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		this.logger.info(responseContext.toString());				
	}

}
