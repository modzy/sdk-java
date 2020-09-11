package com.modzy.sdk;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modzy.sdk.dto.ModelSearchParams;
import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.model.Model;
import com.modzy.sdk.model.ModelVersion;
import com.modzy.sdk.util.LoggerFactory;

/**
 * 
 * Client of the Model API Services  
 *
 */
public class ModelClient {

	private static final String REST_PATH = "models";
	
	private WebTarget restTarget;
	
	private String apiKey;
	
	private Logger logger;
	
	public ModelClient(WebTarget baseTarget, String apiKey) {
		super();
		this.logger = LoggerFactory.getLogger(this);
		this.restTarget = baseTarget.path(ModelClient.REST_PATH);
		this.apiKey = apiKey;
	}
	
	/**
	 * Wrapper of the GenericType necesary for process the response of the API
	 *  
	 * @return A GenericType object for parse a list of Model instances
	 */
	private GenericType<List<Model>> getListType(){
		return new GenericType<List<Model>>() {};
	}	
	
	/**
	 * Wrapper of the GenericType necesary for process the response of the ModelVersion API
	 *  
	 * @return A GenericType object for parse a list of ModelVersion instances
	 */
	private GenericType<List<ModelVersion>> getVersionListType(){
		return new GenericType<List<ModelVersion>>() {};
	}
	
	/**
	 * 
	 * Call the Modzy service that retrieve all models basic info (modelId, versions, and latestVersion)
	 * 
	 * @return A list of all the Modzy models
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public List<Model> getAllModels() throws ApiException{
		return this.getModels(new ModelSearchParams() );
	}

	/**
	 *
	 * Call the Modzy service that retrieve models basic info (modelId, versions, and latestVersion)
	 *
	 * @param searchParams filter for this api call
	 * @return A list of all the Modzy models
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public List<Model> getModels(ModelSearchParams searchParams) throws ApiException{
		WebTarget webTarget = this.restTarget;
		ObjectMapper objMapper = new ObjectMapper();
		Map<String, String> map = objMapper.convertValue(searchParams, new TypeReference<Map<String,String>>() {} );
		for( Map.Entry<String,String> entry : map.entrySet() ) {
			webTarget = webTarget.queryParam( entry.getKey(), entry.getValue() );
		}
		Builder builder = webTarget.request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {
			logger.info("getModels() :: "+webTarget.getUri());
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
	 * Call the Modzy API Service that return a model instance by it's identifier
	 * 
	 * @param modelId identifier of the model
	 * @return A Model instance if the modelId param is valid
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Model getModel(String modelId) throws ApiException{
		Builder builder = this.restTarget.path(modelId).request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {			
			logger.info("getModel("+modelId+") :: Ready to call modzy api");
			return builder.get(Model.class);
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
	 * Call the Modzy API Service that return a model instance by it's identifier.
	 *
	 * @param modelName name of the model
	 * @return A Model instance if the model name is valid
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Model getModelByName(String modelName) throws ApiException{
		ModelSearchParams searchParams = new ModelSearchParams();
		searchParams.setName(modelName);
		List<Model> models = this.getModels(searchParams);
		if( models != null && !models.isEmpty() ){
			return this.getModel(models.get(0).getModelId());
		}
		throw new ApiException("Model "+modelName+" not found");
	}
	
	/**
	 * 
	 * Call the Modzy API Service that return a model list related to a model identifier
	 * 
	 * @param modelId identifier of the model
	 * @return A list of Modzy Models related to the modelId param
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public List<Model> getRelatedModels(String modelId) throws ApiException{
		Builder builder = this.restTarget.path(modelId).path("related-models").request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {			
			logger.info("getRelatedModels("+modelId+") :: Ready to call modzy api");
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
	 * Call the Modzy API Service that return a model list related to a model 
	 * 
	 * @param model a valid model
	 * @return A list of Modzy Models related to the model param
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public List<Model> getRelatedModels(Model model) throws ApiException{
		return this.getRelatedModels(model.getModelId());
	}		
	
	/**
	 * Call the Modzy API Service that return a version list of a given model
	 * @param modelId a valid model identificator 
	 * @return a list of Modzy versions of the model param
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public List<ModelVersion> getModelVersions(String modelId) throws ApiException{
		Builder builder = this.restTarget.path(modelId).path("versions").request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {			
			logger.info("getModelVersions("+modelId+") :: Ready to call modzy api");
			return builder.get(this.getVersionListType());
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
	 * Call the Modzy API Service that return a version list of a given model
	 * @param model a valid model 
	 * @return a list of Modzy versions of the model param
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public List<ModelVersion> getModelVersions(Model model) throws ApiException{
		return this.getModelVersions(model.getModelId());
	}
	
	/**
	 * Call the Modzy API Service that return a version object
	 * @param modelId a valid model id
	 * @param versionId a valid version id 
	 * @return a ModelVersion instance with the information
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public ModelVersion getModelVersion(String modelId, String versionId) throws ApiException{
		Builder builder = this.restTarget.path(modelId).path("versions").path(versionId).request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {
			logger.info("getModelVersion("+modelId+","+versionId+") :: Ready to call modzy api");
			return builder.get(ModelVersion.class);
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
	 * Call the Modzy API Service that return the sample input for a model version
	 * @param modelId a valid model id
	 * @param versionId a valid version id
	 * @return a json string instance with the information
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public String getModelVersionInputSample(String modelId, String versionId) throws ApiException{
		Builder builder = this.restTarget.path(modelId)
				.path("versions").path(versionId)
				.path("sample-input")
				.request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {
			logger.info("getModelVersion("+modelId+","+versionId+") :: Ready to call modzy api");
			return builder.get(String.class);
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
	 * Call the Modzy API Service that return the sample output for a model version
	 * @param modelId a valid model id
	 * @param versionId a valid version id
	 * @return a json string instance with the information
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public String getModelVersionOutputSample(String modelId, String versionId) throws ApiException{
		Builder builder = this.restTarget.path(modelId)
				.path("versions").path(versionId)
				.path("sample-output")
				.request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {
			logger.info("getModelVersion("+modelId+","+versionId+") :: Ready to call modzy api");
			return builder.get(String.class);
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
