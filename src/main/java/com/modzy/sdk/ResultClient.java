package com.modzy.sdk;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.model.Job;
import com.modzy.sdk.model.JobOutput;
import com.modzy.sdk.model.JobOutputJSON;
import com.modzy.sdk.util.LoggerFactory;

/**
 * 
 * Client of the Result API Services 
 *
 */
public class ResultClient {

	private static final String REST_PATH = "results";
	
	private WebTarget restTarget;
	
	private String apiKey;
	
	private Logger logger;
	
	public ResultClient(WebTarget baseTarget, String apiKey) {
		super();
		this.logger = LoggerFactory.getLogger(this);
		this.restTarget = baseTarget.path(ResultClient.REST_PATH);
		this.apiKey = apiKey;
	}
	
	/**
	 * 
	 * Get the result object of the job on the result format provided
	 * 
	 * @param <T> Subclass of JobOutput in which the result will be formatted
	 * @param jobId The job identifier which results we want to get
	 * @param outputClass Subclass of JobOutput in which the result will be formatted
	 * @return the job result {@link JobOutput}
	 * @throws ApiException if there is something wrong with the service or the call of is the job is on submitted state
	 */
	public <T extends JobOutput<?>> T getResult(String jobId, Class<T> outputClass) throws ApiException{
		Builder builder = this.restTarget.path(jobId).request(MediaType.APPLICATION_JSON);
		builder.header("Authorization", "ApiKey "+this.apiKey);
		try {
			logger.info("querying job results "+jobId);
			return builder.get(outputClass);//TODO: By now is just JSON			
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
	 * Get the result object of the job on the result format provided
	 * 
	 * @param <T> Subclass of JobOutput in which the result will be formatted
	 * @param job the job which reuslt we want to get
	 * @param outputClass Subclass of JobOutput in which the result will be formatted
	 * @return the job result {@link JobOutput}
	 * @throws ApiException if there is something wrong with the service or the call of is the job is on submitted state
	 */
	public <T extends JobOutput<?>> T getResult(Job job, Class<T> outputClass) throws ApiException{
		return this.getResult(job.getJobIdentifier(), outputClass);
	}
	
	/**
	 * 
	 * Get the result of the job in the default format (JSON)
	 * 
	 * @param jobId The job identifier which results we want to get 
	 * @return the job result {@link JobOutput}
	 * @throws ApiException if there is something wrong with the service or the call of is the job is on submitted state
	 */
	public JobOutput<JsonNode> getResult(String jobId) throws ApiException{
		return this.getResult(jobId, JobOutputJSON.class); 	
	}
	
	/**
	 * 
	 * Get the result of the job in the default format (JSON)
	 * 
	 * @param job The job which results we want to get
	 * @return the job result {@link JobOutput}
	 * @throws ApiException if there is something wrong with the service or the call of is the job is on submitted state
	 */
	public JobOutput<JsonNode> getResult(Job job) throws ApiException{
		return this.getResult(job.getJobIdentifier(), JobOutputJSON.class);
	}
	
}
