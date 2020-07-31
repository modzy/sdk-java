package com.modzy.sdk;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.modzy.sdk.dto.TagWrapper;
import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.model.Tag;
import com.modzy.sdk.util.LoggerFactory;

/**
 * 
 * Client of the Tag API Services 
 *
 */
public class TagClient {

	private static final String REST_PATH = "models/tags";
	
	private WebTarget restTarget;
	
	private String apiKey;
	
	private Logger logger;
	
	public TagClient(WebTarget baseTarget, String apiKey) {
		super();
		this.logger = LoggerFactory.getLogger(this);
		this.restTarget = baseTarget.path(TagClient.REST_PATH);
		this.apiKey = apiKey;
	}
	
	/**
	 * Wrapper of the GenericType necesary for process the response of the API
	 *  
	 * @return A GenericType object for parse a list of Tag instances
	 */
	private GenericType<List<Tag>> getListType(){
		return new GenericType<List<Tag>>() {};
	}
		
	/**
	 * 
	 * Call the Modzy service that returns all the tags
	 * 
	 * @return A list of all the Modzy tags
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public List<Tag> getAllTags() throws ApiException {
		Builder builder = this.restTarget.request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {
			logger.info("Ready to call modzy api");
			return builder.get(this.getListType());
		}
		catch(ResponseProcessingException rpe) {
			this.logger.log(Level.SEVERE, rpe.getMessage(), rpe);
			throw new ApiException(rpe);			
		}
		catch(ProcessingException pr) {
			this.logger.log(Level.SEVERE, pr.getMessage(), pr);
			throw new ApiException(pr);
		}
		catch(WebApplicationException wae) {			
			this.logger.log(Level.SEVERE, wae.getMessage(), wae);
			throw new ApiException(wae);
		} 	
	}
	
	/**
	 * 
	 * Call the Modzy API Service that return a tag Wrapper with the list of Tag and Model instances
	 * 
	 * @param tagsId identifier(s) of the tag(s)
	 * @return A TagWrapper instance if the tagsId is valid
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public TagWrapper getTagsAndModels(String ...tagsId) throws ApiException{		
		Builder builder = this.restTarget.path(String.join(",", tagsId)).request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {						
			return builder.get(TagWrapper.class);
		}
		catch(ResponseProcessingException rpe) {
			this.logger.log(Level.SEVERE, rpe.getMessage(), rpe);
			throw new ApiException(rpe);			
		}
		catch(ProcessingException pr) {
			this.logger.log(Level.SEVERE, pr.getMessage(), pr);
			throw new ApiException(pr);
		}
		catch(WebApplicationException wae) {			
			this.logger.log(Level.SEVERE, wae.getMessage(), wae);
			throw new ApiException(wae);
		} 	 	
	}
	
}
