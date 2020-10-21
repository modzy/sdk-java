package com.modzy.sdk;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.fasterxml.jackson.databind.JsonNode;
import com.modzy.sdk.dto.EmbeddedData;
import com.modzy.sdk.dto.ModelSearchParams;
import com.modzy.sdk.dto.S3FileRef;
import com.modzy.sdk.dto.TagWrapper;
import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.model.Job;
import com.modzy.sdk.model.JobInput;
import com.modzy.sdk.model.JobInputEmbedded;
import com.modzy.sdk.model.JobInputJDBC;
import com.modzy.sdk.model.JobInputS3;
import com.modzy.sdk.model.JobInputText;
import com.modzy.sdk.model.JobOutput;
import com.modzy.sdk.model.JobStatus;
import com.modzy.sdk.model.Model;
import com.modzy.sdk.model.ModelVersion;
import com.modzy.sdk.model.Tag;
import com.modzy.sdk.util.LoggerFactory;

/**
 * Factory and Facade for Modzy API interaction
 *
 */
public class ModzyClient {
	
	private WebTarget baseTarget;
	
	private String apiKey;
	
	private ModelClient modelClient;
	
	private JobClient jobClient;
	
	private ResultClient resultClient;
	
	private TagClient tagClient;
	
	private Logger logger;
	
	public ModzyClient(WebTarget baseTarget, String apiKey) {
		super();
		this.logger = LoggerFactory.getLogger(this);
		this.baseTarget = baseTarget;
		this.apiKey = apiKey;
		this.modelClient = new ModelClient(this.baseTarget, this.apiKey);
		this.jobClient = new JobClient(this.baseTarget, this.apiKey);
		this.resultClient = new ResultClient(this.baseTarget, this.apiKey);
		this.tagClient = new TagClient(this.baseTarget, this.apiKey);
	}
	
	public ModzyClient(String baseURL, String apiKey) {
		if(baseURL == null || baseURL.trim().equals("")){
			throw new RuntimeException("Cannot initialize the modzy client: the baseURL param should be a valid not empty string");
		}
		if(apiKey == null || apiKey.trim().equals("")){
			throw new RuntimeException("Cannot initialize the modzy client: the apiKey param should be a valid not empty string");
		}
		this.logger = LoggerFactory.getLogger(this);
		this.baseTarget = ClientBuilder.newClient().target(baseURL);
		this.apiKey = apiKey;
		this.modelClient = new ModelClient(this.baseTarget, this.apiKey);
		this.jobClient = new JobClient(this.baseTarget, this.apiKey);
		this.resultClient = new ResultClient(this.baseTarget, this.apiKey);
		this.tagClient = new TagClient(this.baseTarget, this.apiKey);
	}
	
	/**
	 * Get the model client initialized 
	 * 
	 * @return The model client instance
	 */
	public ModelClient getModelClient() {
		return this.modelClient;
	}
	
	/**
	 * 
	 * Get the job client initialized
	 * 
	 * @return The job client initialized
	 */
	public JobClient getJobClient() {
		return this.jobClient;
	}
	
	/**
	 * 
	 * Get the result client initialized
	 * 
	 * @return The result client initialized
	 */
	public ResultClient getResultClient() {
		return this.resultClient;
	}
	
	/**
	 * 
	 * Get the tag client initialized
	 * 
	 * @return The tag client initialized
	 */
	public TagClient getTagClient() {
		return this.tagClient;
	}
	
	/**
	 * @see ModelClient#getAllModels()
	 */
	public List<Model> getAllModels() throws ApiException{
		return this.modelClient.getAllModels();
	}

	/**
	 * @see ModelClient#getModels(ModelSearchParams)
	 */
	public List<Model> getModels(ModelSearchParams searchParams) throws ApiException{
		return this.modelClient.getModels(searchParams);
	}

	/**
	 * @see ModelClient#getModelByName(String)
	 */
	public Model getModelByName(String modelName) throws ApiException{
		return this.modelClient.getModelByName(modelName);
	}

	/**
	 * @see ModelClient#getModel(String)
	 */
	public Model getModel(String modelId) throws ApiException{	
		return this.modelClient.getModel(modelId);
	}
	
	/**
	 * @see ModelClient#getRelatedModels(String)
	 */
	public List<Model> getRelatedModels(String modelId) throws ApiException{
		return this.modelClient.getRelatedModels(modelId);
	}
	
	/**
	 * @see ModelClient#getModelVersions(String)
	 */
	public List<ModelVersion> getModelVersions(String modelId) throws ApiException{
		return this.modelClient.getModelVersions(modelId);
	}

	
	/**
	 * @see ModelClient#getModelVersion(String, String)
	 */
	public ModelVersion getModelVersion(String modelId, String versionId) throws ApiException{
		return this.modelClient.getModelVersion(modelId, versionId);
	}
	
	/**
	 * @see TagClient#getAllTags()
	 */
	public List<Tag> getAllTags() throws ApiException {
		return this.tagClient.getAllTags();
	}
	
	/**
	 * @see TagClient#getTagsAndModels(String...)
	 */
	public TagWrapper getTagsAndModels(String ...tagsId) throws ApiException{
		return this.tagClient.getTagsAndModels(tagsId);
	}
	
	/**
	 * @see JobClient#submitJob(Model, ModelVersion, JobInput) 
	 */
	public Job submitJob(Model model, ModelVersion modelVersion, JobInput<?> jobInput) throws ApiException{
		return this.jobClient.submitJob(model, modelVersion, jobInput);
	}
	
	
	
	/**
	 * 
	 * Create a new job for the model at the last version with the specific text inputs provided,
	 * this method use the last version of the model and try to match the textSource values with 
	 * the inputs of the specific version of the model.
	 * 
	 * @param modelId the model id string
	 * @param textSource the source(s) of the model 
	 * @return the updated instance of the Job returned by Modzy API
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job submitJobText(String modelId, String ...textSource) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, model.getLatestVersion());
		JobInput<String> jobInput = new JobInputText(modelVersion);		
		jobInput.addSource(textSource);
		return this.submitJob(model, modelVersion, jobInput);
	} 
	
	/**
	 * 
	 * Create a new job for the model at the specific version with the text inputs provided,
	 * this method try to match the textSource values with the inputs of the specific version 
	 * of the model.
	 * 
	 * @param modelId the model id string
	 * @param versionId version id string
	 * @param textSource the source(s) of the model 
	 * @return the updated instance of the Job returned by Modzy API
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job submitJobText(String modelId, String versionId, String ...textSource) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, versionId);			
		JobInput<String> jobInput = new JobInputText(modelVersion);		
		jobInput.addSource(textSource);
		return this.submitJob(model, modelVersion, jobInput);
	}
	
	/**
	 * 
	 * Create a new job for the model at the last version with the embedded inputs provided,
	 * this method try to match the textSource values with the inputs of the specific version 
	 * of the model.
	 * 
	 * @param modelId the model id string
	 * @param embeddedSource the source(s) of the model 
	 * @return the updated instance of the Job returned by Modzy API
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job submitJobEmbedded(String modelId, EmbeddedData ...embeddedSource) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, model.getLatestVersion());			
		JobInput<EmbeddedData> jobInput = new JobInputEmbedded(modelVersion);		
		jobInput.addSource(embeddedSource);
		return this.submitJob(model, modelVersion, jobInput);
	}
	
	/**
	 * 
	 * Create a new job for the model at the specific version with the embedded inputs provided,
	 * this method try to match the textSource values with the inputs of the specific version 
	 * of the model.
	 * 
	 * @param modelId the model id string
	 * @param versionId version id string
	 * @param embeddedSource the source(s) of the model 
	 * @return the updated instance of the Job returned by Modzy API
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job submitJobEmbedded(String modelId, String versionId, EmbeddedData ...embeddedSource) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, versionId);			
		JobInput<EmbeddedData> jobInput = new JobInputEmbedded(modelVersion);		
		jobInput.addSource(embeddedSource);
		return this.submitJob(model, modelVersion, jobInput);
	}
	
	/**
	 * 
	 * Create a new job for the model at the last version with the amazon web services s3  
	 * inputs provided, this method try to match the textSource values with the inputs of the 
	 * specific version of the model.
	 * 
	 * @param modelId the model id string
	 * @param accessKeyID access key of aws-s3
	 * @param secretAccessKey secret access key of aws-s3
	 * @param region aws-s3 region
	 * @param s3FileRefSource the source(s) of the model
	 * @return the updated instance of the Job returned by Modzy API
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job submitJobAWSS3(String modelId, String accessKeyID, String secretAccessKey, String region, S3FileRef ...s3FileRefSource ) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, model.getLatestVersion());			
		JobInput<S3FileRef> jobInput = new JobInputS3(modelVersion, accessKeyID, secretAccessKey, region);		
		jobInput.addSource( s3FileRefSource );
		return this.submitJob(model, modelVersion, jobInput);
	}
	
	/**
	 * 
	 * Create a new job for the model at the specific version with the amazon web services s3  
	 * inputs provided, this method try to match the textSource values with the inputs of the 
	 * specific version of the model.
	 * 
	 * @param modelId the model id string
	 * @param versionId version id string
	 * @param accessKeyID access key of aws-s3
	 * @param secretAccessKey secret access key of aws-s3
	 * @param region aws-s3 region
	 * @param s3FileRefSource the source(s) of the model
	 * @return the updated instance of the Job returned by Modzy API
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job submitJobAWSS3(String modelId, String versionId, String accessKeyID, String secretAccessKey, String region, S3FileRef ...s3FileRefSource ) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, versionId);			
		JobInput<S3FileRef> jobInput = new JobInputS3(modelVersion, accessKeyID, secretAccessKey, region);		
		jobInput.addSource( s3FileRefSource );
		return this.submitJob(model, modelVersion, jobInput);
	}
	
	/**
	 * 
	 * Create a new job for the model at the last version with the jdbc input provided.
	 * 
	 * Modzy will create a data source with the parameters provided and will execute 
	 * the query provided, then will match the inputs defined of the model with the columns 
	 * of the resultset.
	 * 
	 * @param modelId the model id string
	 * @param url connection url to the database
	 * @param username database username
	 * @param password database password
	 * @param driver fully qualified name of the driver class for jdbc
	 * @param query the query to get the inputs of the model
	 * @return the updated instance of the Job returned by Modzy API
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job submitJobJDBC(String modelId, String url, String username, String password, String driver, String query ) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, model.getLatestVersion());			
		JobInput<String> jobInput = new JobInputJDBC(url, username, password, driver, query);				
		return this.submitJob(model, modelVersion, jobInput);
	}
	
	/**
	 * 
	 * Create a new job for the model at the last version with the jdbc input provided.
	 * 
	 * Modzy will create a data source with the parameters provided and will execute 
	 * the query provided, then will match the inputs defined of the model with the columns 
	 * of the resultset.
	 * 
	 * @param modelId the model id string
	 * @param versionId version id string
	 * @param url connection url to the database
	 * @param username database username
	 * @param password database password
	 * @param driver fully qualified name of the driver for jdbc
	 * @param query the select query to obtain the inputs
	 * @return the updated instance of the Job returned by Modzy API
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job submitJobJDBC(String modelId, String versionId, String url, String username, String password, String driver, String query ) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, versionId);			
		JobInput<String> jobInput = new JobInputJDBC(url, username, password, driver, query);		
		return this.submitJob(model, modelVersion, jobInput);
	}
	
	/**
	 * 
	 * Utility method that wait until the job status change.
	 * 
	 * This method will sleep the main thread waiting for a job status change
	 * on the server side, it use the timeout value as a reference of the time 
	 * that will wait.
	 *  
	 * @param job the job that will wait for
	 * @param timeout the time in miliseconds expected for a status change
	 * @param jobStatus the status that will wait until finish
	 * @return A updated instance of the job that has changed its state
	 * @throws ApiException if there is something wrong with the service or the call 
	 */
	public Job blockUntilNotInJobStatus( Job job, long timeout, JobStatus jobStatus ) throws ApiException {		
		this.logger.info("["+job.getJobIdentifier()+"] "+job.getModel().getName()+": waiting for end of "+jobStatus+" with a timeout of "+timeout+"ms" );
		long initime = System.currentTimeMillis();		
		while( jobStatus.equals( job.getStatus() ) ) {
			this.logger.info("["+job.getJobIdentifier()+"] "+job.getModel().getName()+": "+(100*(System.currentTimeMillis() - initime)/timeout)+"% waiting for end of "+jobStatus+" with a timeout of "+timeout+"ms" );			
			try {				
				Thread.sleep(Math.max( 2500, timeout/20 ) );
			} catch (InterruptedException ie) {
				throw new ApiException(ie);
			}					
			job = this.jobClient.getJob(job);
			this.logger.info("["+job.getJobIdentifier()+"] "+job.getModel().getName()+": new job status "+job.getStatus() );			
		}		
		return job;
	}
	
	/**
	 * 
	 * Utility method that wait until the job finish.
	 * 
	 * This method first check the status of the job and wait until the job reach
	 * the completed status by passing thought the submitted and in_progress state.
	 * 
	 * @param job The job to block
	 * @param timeout the timeout of the model
	 * @return A updated instance of the job in a final state
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job blockUntilComplete( Job job, long timeout ) throws ApiException{
		if( JobStatus.SUBMITTED.equals( job.getStatus() ) ) {
			this.blockUntilNotInJobStatus(job, timeout, JobStatus.SUBMITTED);
		}
		if( JobStatus.IN_PROGRESS.equals( job.getStatus() ) ) {
			this.blockUntilNotInJobStatus(job, timeout, JobStatus.IN_PROGRESS);
		}
		return job;
	}
	
	/**
	 * 
	 * @see ResultClient#getResult(Job)
	 * 
	 */
	public JobOutput<JsonNode> getResult(Job job) throws ApiException{
		return this.resultClient.getResult(job);
	}
	
	/**
	 * 
	 * @see ResultClient#getResult(Job)
	 * 
	 */
	public <T extends JobOutput<?>> T getResult(Job job, Class<T> outputClass) throws ApiException{
		return this.resultClient.getResult(job, outputClass);
	}
	
	/**
	 * 
	 * Create a new Job for the model at specific version with the input provided and then wait until the
	 * job finish and return his results.
	 *  
	 * @param modelId identifier of the model
	 * @param modelVersionId identifier of the model version 
	 * @param jobInput The inputs of the model to pass to Modzy
	 * @return the job result {@link JobOutput}
	 * @throws ApiException if there is something wrong with the services or the call
	 */
	public JobOutput<JsonNode> submitJobBlockUntilComplete(String modelId, String modelVersionId, JobInput<?> jobInput ) throws ApiException{
		Job job = this.jobClient.submitJob(modelId, modelVersionId, jobInput);
		job = this.blockUntilNotInJobStatus(job, 20000, JobStatus.SUBMITTED);
		job = this.blockUntilNotInJobStatus(job, 30000, JobStatus.IN_PROGRESS);
		if( !job.getStatus().equals(JobStatus.COMPLETED) ) {
			throw new ApiException("The job finished with a status "+job.getStatus() );
		}
		this.logger.info("["+job.getJobIdentifier()+"] "+modelId+" :: "+modelVersionId+" :: getting job results ");
		return this.resultClient.getResult(job);
	}
	
	/**
	 * 
	 * Create a new Job for the model at specific version with the input provided and then wait until the
	 * job finish and return his results.
	 *  
	 * @param model The model instance in which the model will run
	 * @param modelVersion The specific version of the model
	 * @param jobInput The inputs of the model to pass to Modzy
	 * @return the job result {@link JobOutput}
	 * @throws ApiException if there is something wrong with the services or the call
	 */
	public JobOutput<JsonNode> submitJobBlockUntilComplete(Model model, ModelVersion modelVersion, JobInput<?> jobInput ) throws ApiException{
		Job job = this.jobClient.submitJob(model, modelVersion, jobInput);
		this.logger.info("["+job.getJobIdentifier()+"] "+model.getName()+" :: "+modelVersion.getVersion()+" :: waiting ");
		job = this.blockUntilNotInJobStatus(job, modelVersion.getTimeout().getStatus(), JobStatus.SUBMITTED);
		job = this.blockUntilNotInJobStatus(job, modelVersion.getTimeout().getRun(), JobStatus.IN_PROGRESS);
		if( !job.getStatus().equals(JobStatus.COMPLETED) ) {
			throw new ApiException("The job finished with a status "+job.getStatus() );
		}
		this.logger.info("["+job.getJobIdentifier()+"] "+model.getName()+" :: "+modelVersion.getVersion()+" :: getting job results ");
		return this.resultClient.getResult(job);		
	}
	
	/**
	 * 
	 * Create a new job for the model at the last version with the specific text inputs provided,
	 * this method use the last version of the model and try to match the textSource values with 
	 * the inputs of the specific version of the model and then wait until the job finish and 
	 * return his results.
	 * 
	 * @param modelId the model id string
	 * @param textSource the source(s) of the model 
	 * @return A mapped response of the model
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Map<String,JsonNode> submitJobTextBlockUntilComplete(String modelId, String textSource) throws ApiException {
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, model.getLatestVersion());
		JobInput<String> jobInput = new JobInputText(modelVersion);		
		jobInput.addSource(textSource);
		JobOutput<JsonNode> jobOutput = this.submitJobBlockUntilComplete(model, modelVersion, jobInput);
		Map<String,JsonNode> mapResult = null;
		for( String resultsKey : jobOutput.getResults().keySet() ) {
			mapResult = jobOutput.getResults().get(resultsKey);
			break;
		}
		if( mapResult == null ) {
			throw new ApiException( jobOutput.getFailures().values().toString() );
		}
		this.logger.info(model.getName()+": job result "+mapResult);
		return mapResult;
	}
	
	/**
	 * 
	 * Create a new job for the model at the specific version with the text inputs provided,
	 * this method try to match the textSource values with the inputs of the specific version 
	 * of the model and then wait until the job finish and return his results.
	 * 
	 * @param modelId the model id string
	 * @param versionId version id string
	 * @param textSource the source(s) of the model 
	 * @return A mapped response of the model
	 * @throws ApiException if there is something wrong with the service or the call
	 */	
	public Map<String,JsonNode> submitJobTextBlockUntilComplete(String modelId, String versionId, String ...textSource) throws ApiException {
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, versionId);
		JobInput<String> jobInput = new JobInputText(modelVersion);		
		jobInput.addSource(textSource);
		JobOutput<JsonNode> jobOutput = this.submitJobBlockUntilComplete(model, modelVersion, jobInput);
		Map<String,JsonNode> mapResult = null;
		for( String resultsKey : jobOutput.getResults().keySet() ) {
			mapResult = jobOutput.getResults().get(resultsKey);
			break;
		}
		if( mapResult == null ) {
			throw new ApiException( jobOutput.getFailures().values().toString() );
		}
		this.logger.info(model.getName()+": job result "+mapResult);
		return mapResult;
	}
	
	/**
	 * 
	 * Create a new job for the model at the last version with the embedded inputs provided,
	 * this method try to match the textSource values with the inputs of the specific version 
	 * of the model and then wait until the job finish and return his results.
	 * 
	 * @param modelId the model id string
	 * @param embeddedSource the source(s) of the model 
	 * @return A mapped response of the model
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Map<String,JsonNode> submitJobEmbeddedBlockUntilComplete(String modelId, EmbeddedData ...embeddedSource) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, model.getLatestVersion());			
		JobInput<EmbeddedData> jobInput = new JobInputEmbedded(modelVersion);		
		jobInput.addSource(embeddedSource);
		JobOutput<JsonNode> jobOutput = this.submitJobBlockUntilComplete(model, modelVersion, jobInput);
		Map<String,JsonNode> mapResult = null;
		for( String resultsKey : jobOutput.getResults().keySet() ) {
			mapResult = jobOutput.getResults().get(resultsKey);
			break;
		}
		if( mapResult == null ) {
			throw new ApiException( jobOutput.getFailures().values().toString() );
		}
		this.logger.info(model.getName()+": job result "+mapResult);
		return mapResult;
	}
	
	/**
	 * 
	 * Create a new job for the model at the specific version with the embedded inputs provided,
	 * this method try to match the textSource values with the inputs of the specific version 
	 * of the model and then wait until the job finish and return his results.
	 * 
	 * @param modelId the model id string
	 * @param versionId version id string
	 * @param embeddedSource the source(s) of the model 
	 * @return A mapped response of the model
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Map<String,JsonNode> submitJobEmbeddedBlockUntilComplete(String modelId, String versionId, EmbeddedData ...embeddedSource) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, versionId);			
		JobInput<EmbeddedData> jobInput = new JobInputEmbedded(modelVersion);		
		jobInput.addSource(embeddedSource);
		JobOutput<JsonNode> jobOutput = this.submitJobBlockUntilComplete(model, modelVersion, jobInput);
		Map<String,JsonNode> mapResult = null;
		for( String resultsKey : jobOutput.getResults().keySet() ) {
			mapResult = jobOutput.getResults().get(resultsKey);
			break;
		}
		if( mapResult == null ) {
			throw new ApiException( jobOutput.getFailures().values().toString() );
		}
		this.logger.info(model.getName()+": job result "+mapResult);
		return mapResult;
	}
	
	/**
	 * 
	 * Create a new job for the model at the last version with the amazon web services s3  
	 * inputs provided, this method try to match the textSource values with the inputs of the 
	 * specific version of the model and then wait until the job finish and return his results.
	 * 
	 * @param modelId the model id string
	 * @param accessKeyID access key of aws-s3
	 * @param secretAccessKey secret access key of aws-s3
	 * @param region aws-s3 region
	 * @param s3FileRefSource the source(s) of the model
	 * @return A mapped response of the model
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Map<String,JsonNode> submitJobAWSS3BlockUntilComplete(String modelId, String accessKeyID, String secretAccessKey, String region, S3FileRef ...s3FileRefSource ) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, model.getLatestVersion());			
		JobInput<S3FileRef> jobInput = new JobInputS3(modelVersion, accessKeyID, secretAccessKey, region);		
		jobInput.addSource( s3FileRefSource );
		JobOutput<JsonNode> jobOutput = this.submitJobBlockUntilComplete(model, modelVersion, jobInput);
		Map<String,JsonNode> mapResult = null;
		for( String resultsKey : jobOutput.getResults().keySet() ) {
			mapResult = jobOutput.getResults().get(resultsKey);
			break;
		}
		if( mapResult == null ) {
			throw new ApiException( jobOutput.getFailures().values().toString() );
		}
		this.logger.info(model.getName()+": job result "+mapResult);
		return mapResult;
	}
	
	/**
	 * 
	 * Create a new job for the model at the specific version with the amazon web services s3  
	 * inputs provided, this method try to match the textSource values with the inputs of the 
	 * specific version of the model and then wait until the job finish and return his results.
	 * 
	 * @param modelId the model id string
	 * @param versionId version id string
	 * @param accessKeyID access key of aws-s3
	 * @param secretAccessKey secret access key of aws-s3
	 * @param region aws-s3 region
	 * @param s3FileRefSource the source(s) of the model
	 * @return A mapped response of the model
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Map<String,JsonNode> submitJobAWSS3BlockUntilComplete(String modelId, String versionId, String accessKeyID, String secretAccessKey, String region, S3FileRef ...s3FileRefSource ) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, versionId);			
		JobInput<S3FileRef> jobInput = new JobInputS3(modelVersion, accessKeyID, secretAccessKey, region);		
		jobInput.addSource( s3FileRefSource );
		JobOutput<JsonNode> jobOutput = this.submitJobBlockUntilComplete(model, modelVersion, jobInput);
		Map<String,JsonNode> mapResult = null;
		for( String resultsKey : jobOutput.getResults().keySet() ) {
			mapResult = jobOutput.getResults().get(resultsKey);
			break;
		}
		if( mapResult == null ) {
			throw new ApiException( jobOutput.getFailures().values().toString() );
		}
		this.logger.info(model.getName()+": job result "+mapResult);
		return mapResult;
	}
	
	/**
	 * 
	 * Create a new job for the model at the last version with the jdbc input provided
	 * and then wait until the job finish and return his results.
	 * 
	 * Modzy will create a data source with the parameters provided and will execute 
	 * the query provided, then will match the inputs defined of the model with the columns 
	 * of the resultset.
	 * 
	 * @param modelId the model id string
	 * @param url connection url to the database
	 * @param username database username
	 * @param password database password
	 * @param driver fully qualified name of the driver class for jdbc
	 * @param query the query to get the inputs of the model
	 * @return A mapped response of the model
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Map<String,JsonNode>  submitJobJDBCBlockUntilComplete(String modelId, String url, String username, String password, String driver, String query ) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, model.getLatestVersion());			
		JobInput<String> jobInput = new JobInputJDBC(url, username, password, driver, query);				
		JobOutput<JsonNode> jobOutput = this.submitJobBlockUntilComplete(model, modelVersion, jobInput);
		Map<String,JsonNode> mapResult = null;
		for( String resultsKey : jobOutput.getResults().keySet() ) {
			mapResult = jobOutput.getResults().get(resultsKey);
			break;
		}
		if( mapResult == null ) {
			throw new ApiException( jobOutput.getFailures().values().toString() );
		}
		this.logger.info(model.getName()+": job result "+mapResult);
		return mapResult;
	}
	
	/**
	 * 
	 * Create a new job for the model at the last version with the jdbc input provided
	 * and then wait until the job finish and return his results.
	 * 
	 * Modzy will create a data source with the parameters provided and will execute 
	 * the query provided, then will match the inputs defined of the model with the columns 
	 * of the resultset.
	 * 
	 * @param modelId the model id string
	 * @param versionId version id string
	 * @param url connection url to the database
	 * @param username database username
	 * @param password database password
	 * @param driver fully qualified name of the driver for jdbc
	 * @param query the select query to obtain the inputs
	 * @return A mapped response of the model
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Map<String,JsonNode>  submitJobJDBCBlockUntilComplete(String modelId, String versionId, String url, String username, String password, String driver, String query ) throws ApiException{
		Model model = this.modelClient.getModel(modelId);
		ModelVersion modelVersion = this.modelClient.getModelVersion(modelId, versionId);			
		JobInput<String> jobInput = new JobInputJDBC(url, username, password, driver, query);		
		JobOutput<JsonNode> jobOutput = this.submitJobBlockUntilComplete(model, modelVersion, jobInput);
		Map<String,JsonNode> mapResult = null;
		for( String resultsKey : jobOutput.getResults().keySet() ) {
			mapResult = jobOutput.getResults().get(resultsKey);
			break;
		}
		if( mapResult == null ) {
			throw new ApiException( jobOutput.getFailures().values().toString() );
		}
		this.logger.info(model.getName()+": job result "+mapResult);
		return mapResult;
	}

	/**
	 *
	 * Utility method that wait until the job status change to end state.
	 *
	 * @param job the job that will wait for
	 * @param timeoutInMillis the time in miliseconds expected for a status change
	 * @return A updated instance of the job
	 * @throws ApiException if there is something wrong with the service or the call
	 */
	public Job blockUntilComplete(Job job, Long timeoutInMillis) throws ApiException{
		timeoutInMillis = timeoutInMillis != null ? timeoutInMillis : 20000L;
		job = this.blockUntilNotInJobStatus(job, timeoutInMillis, JobStatus.SUBMITTED);
		job = this.blockUntilNotInJobStatus(job, timeoutInMillis, JobStatus.IN_PROGRESS);
		return job;
	}
}
