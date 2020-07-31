package com.modzy.sdk;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modzy.sdk.dto.JobHistorySearchParams;
import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.model.Job;
import com.modzy.sdk.model.JobInput;
import com.modzy.sdk.model.Model;
import com.modzy.sdk.model.ModelVersion;
import com.modzy.sdk.util.LoggerFactory;

/**
 * 
 * Client of the Job API Services 
 *
 */
public class JobClient {
	
	private static final String REST_PATH = "jobs";
	
	private WebTarget restTarget;
	
	private String apiKey;
	
	private Logger logger;
	
	public JobClient(WebTarget baseTarget, String apiKey) {
		super();
		this.logger = LoggerFactory.getLogger(this);
		this.restTarget = baseTarget.path(JobClient.REST_PATH);
		this.apiKey = apiKey;
	}
	
	/**
	 * Wrapper of the GenericType necesary for process the response of the API
	 *  
	 * @return A GenericType object for parse a list of Job instances
	 */
	private GenericType<List<Job>> getListType(){
		return new GenericType<List<Job>>() {};
	}	
	
	/**
	 * 
	 * Call the Modzy API Service and query on the history of jobs
	 * 
	 * @param searchParams search parameters see {@link JobHistorySearchParams class}
	 * @return List of Jobs according to the search params
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public List<Job> getJobHistory(JobHistorySearchParams searchParams) throws ApiException{
		WebTarget webTarget = this.restTarget.path("history");
		ObjectMapper objMapper = new ObjectMapper();		
		Map<String, String> map = objMapper.convertValue(searchParams, new TypeReference<Map<String,String>>() {} );		
		for( Entry<String,String> entry : map.entrySet() ) {
			webTarget = webTarget.queryParam( entry.getKey(), entry.getValue() );					
		}
		if( searchParams.getJobIdentifiers() != null ) {
			try {				
				webTarget = webTarget.queryParam( "jobIdentifiers", objMapper.writeValueAsString( Arrays.asList( searchParams.getJobIdentifiers() ) ) );
			} catch (JsonProcessingException jpe) {
				throw new ApiException(jpe);
			}
		}
		Builder builder = webTarget.request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {
			logger.info("querying job history "+webTarget.getUri());			
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
	 * Call the Modzy API Service that post a new job and return it's instance
	 * 
	 * @param job the job instance to pass to Modzy
	 * @return the updated instance of the Job returned by Modzy API
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job submitJob(Job job) throws ApiException{
		Builder builder = this.restTarget.request(MediaType.APPLICATION_JSON);		
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {
			logger.info("creating job: "+job);
			return builder.post(Entity.entity(job, MediaType.APPLICATION_JSON), Job.class);
		}		
		catch(ResponseProcessingException rpe) {
			this.logger.log(Level.SEVERE, rpe.getMessage(), rpe);
			throw new ApiException(rpe);			
		}
		catch(ProcessingException pr) {
			this.logger.log(Level.SEVERE, pr.getMessage(), pr);
			throw new ApiException(pr);
		}
		
	}
	
	/**
	 * 
	 * Create a new Job for the model at specific version with the input provided
	 * 
	 * @param model The model instance in which the model will run
	 * @param modelVersion The specific version of the model
	 * @param jobInput The inputs of the model to pass to Modzy
	 * @return the updated instance of the Job returned by Modzy API
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job submitJob(Model model, ModelVersion modelVersion, JobInput<?> jobInput) throws ApiException{		
		return this.submitJob( new Job(model, modelVersion, jobInput) );
	}	
	
	/**
	 * 
	 * Create a new Job for the model at specific version with the input provided
	 * 
	 * @param modelId identifier of the model
	 * @param modelVersionId identifier of the model version 
	 * @param jobInput the inputs of the model to pass to Modzy
	 * @return the updated instance of the Job returned by Modzy API
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job submitJob(String modelId, String modelVersionId, JobInput<?> jobInput) throws ApiException{
		Model model = new Model();
		model.setModelId(modelId);
		ModelVersion modelVersion = new ModelVersion();
		modelVersion.setVersion(modelVersionId);
		return this.submitJob( new Job(model, modelVersion, jobInput) );
	}
	
	/**
	 * 
	 * Call the Modzy API Service that return a job instance by it's identifier
	 * 
	 * @param jobId identifier of the job
	 * @return A Job instance if the jobId param is valid
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job getJob(String jobId) throws ApiException{		
		Builder builder = this.restTarget.path(jobId).request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {
			return builder.get(Job.class);
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
	 * Refresh the state of the Job provided, this method is a shortcut to 
	 * {@link JobClient#getJob(String) getJob(jobId)}
	 * 
	 * @param job the Job instance that will be refreshed
	 * @return a updated job instance 
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job getJob(Job job) throws ApiException{
		return this.getJob(job.getJobIdentifier());
	}
	
	/**
	 * 
	 * Call the Modzy API Service that cancel the Job by it's identifier
	 * 
	 * @param jobId identifier of the Job
	 * @return a Job Instance if the jobId param is a valid identifier
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job cancelJob(String jobId) throws ApiException{
		Builder builder = this.restTarget.path(jobId).request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {
			logger.info("canceling job "+jobId);
			return builder.delete(Job.class);			
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
	 * Cancel the Job provided, this method is a shortcut to 
	 * {@link JobClient#cancelJob(String) getJob(jobId)}
	 * 
	 * @param job the Job instance that will be canceled
	 * @return a updated job instance (hopefully canceled)
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job cancelJob(Job job) throws ApiException{
		this.logger.info("canceling job "+job);
		return this.cancelJob(job.getJobIdentifier());
	}		
	
	
}
