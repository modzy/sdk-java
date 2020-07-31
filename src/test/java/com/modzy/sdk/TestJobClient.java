package com.modzy.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Before;
import org.junit.Test;

import com.modzy.sdk.dto.EmbeddedData;
import com.modzy.sdk.dto.JobHistorySearchParams;
import com.modzy.sdk.dto.S3FileRef;
import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.filter.LoggingFilter;
import com.modzy.sdk.model.Job;
import com.modzy.sdk.model.JobInput;
import com.modzy.sdk.model.JobInputEmbedded;
import com.modzy.sdk.model.JobInputS3;
import com.modzy.sdk.model.JobInputText;
import com.modzy.sdk.model.JobStatus;
import com.modzy.sdk.model.Model;
import com.modzy.sdk.model.ModelVersion;
import com.modzy.sdk.util.LoggerFactory;
import io.github.cdimascio.dotenv.Dotenv;

public class TestJobClient {

	private ModelClient modelClient;
	private JobClient jobClient;
	private String apiKey;
	
	private Logger logger;
	
	@Before
	public void setUp() throws Exception {
		this.logger = LoggerFactory.getLogger(this);
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		Client client = ClientBuilder.newClient().register(LoggingFilter.class);
		WebTarget baseTarget = client.target(dotenv.get("MODZY_BASE_URL"));
		this.apiKey = dotenv.get("MODZY_API_KEY");
		this.modelClient = new ModelClient(baseTarget, this.apiKey);
		this.jobClient   = new JobClient(baseTarget, dotenv.get("MODZY_API_KEY"));
	}
	
	@Test
	public void testSubmitJob(){
		Model model = null;
		try {
			model = this.modelClient.getModel("ed542963de");//sentiment-analysis
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(model);
		assertNotEquals(model.getVersions(), 0);		
		ModelVersion modelVersion = null;
		try {
			modelVersion = this.modelClient.getModelVersion(model.getIdentifier(), model.getLatestVersion());
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(modelVersion);
		assertNotEquals(modelVersion.getInputs(), 0);
		assertNotEquals(modelVersion.getOutputs(), 0);
		//
		Map<String,String> sourceMap = new HashMap<String,String>();
		sourceMap.put("input.txt", "Modzy is great!");
		JobInput<String> jobInput = new JobInputText();
		jobInput.addSource(sourceMap);
		Job job = null;
		try {			
			job = this.jobClient.submitJob(model, modelVersion, jobInput);
			this.logger.info( job.toString() );
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(job);
		this.logger.info(job.toString());
		assertNotNull(job.getJobIdentifier());
		assertNotNull(job.getStatus());
		assertNotNull(job.getSubmittedAt());
		assertNotNull(job.getSubmittedBy());		
	}		
	
	@Test
	public void textGetJob() {
		Model model = null;
		try {
			model = this.modelClient.getModel("ed542963de");//sentiment-analysis
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(model);
		assertNotEquals(model.getVersions(), 0);		
		ModelVersion modelVersion = null;
		try {
			modelVersion = this.modelClient.getModelVersion(model.getIdentifier(), model.getLatestVersion());
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(modelVersion);
		assertNotEquals(modelVersion.getInputs(), 0);
		assertNotEquals(modelVersion.getOutputs(), 0);
		//
		Map<String,String> sourceMap = new HashMap<String,String>();
		sourceMap.put("input.txt", "Modzy is great!");
		JobInput<String> jobInput = new JobInputText();
		jobInput.addSource(sourceMap);
		Job job = null;
		try {			
			job = this.jobClient.submitJob(model, modelVersion, jobInput);
			this.logger.info( job.toString() );
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(job);
		this.logger.info(job.toString());
		assertNotNull(job.getJobIdentifier());
		assertNotNull(job.getStatus());
		assertNotNull(job.getSubmittedAt());
		assertNotNull(job.getSubmittedBy());		
		//
		Job job2 = null;
		try {
			job2 = this.jobClient.getJob(job);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(job2);		
		assertNotNull(job2.getJobIdentifier());
		this.logger.info( "Job after get call "+job2.toString() );
		assertEquals(job.getJobIdentifier(), job2.getJobIdentifier());		
	}
	
	@Test
	public void textCancelJob() {
		Model model = null;
		try {
			model = this.modelClient.getModel("ed542963de");//sentiment-analysis
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(model);
		assertNotEquals(model.getVersions(), 0);		
		ModelVersion modelVersion = null;
		try {
			modelVersion = this.modelClient.getModelVersion(model.getIdentifier(), model.getLatestVersion());
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(modelVersion);
		assertNotEquals(modelVersion.getInputs(), 0);
		assertNotEquals(modelVersion.getOutputs(), 0);
		//
		Map<String,String> sourceMap = new HashMap<String,String>();
		sourceMap.put("input.txt", "Modzy is great!");
		JobInput<String> jobInput = new JobInputText();
		jobInput.addSource(sourceMap);
		Job job = null;
		try {			
			job = this.jobClient.submitJob(model, modelVersion, jobInput);
			this.logger.info( job.toString() );
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(job);
		this.logger.info(job.toString());
		assertNotNull(job.getJobIdentifier());
		assertNotNull(job.getStatus());
		assertNotNull(job.getSubmittedAt());
		assertNotNull(job.getSubmittedBy());		
		//
		Job job2 = null;
		try {
			job2 = this.jobClient.cancelJob(job);			
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(job2);			
		assertNotNull(job2.getJobIdentifier());
		this.logger.info("Job after cancel call "+job2.toString() );
		assertEquals(job.getJobIdentifier(), job2.getJobIdentifier());
		assertEquals(JobStatus.CANCELED, job2.getStatus());
	}
	
	@Test
	public void testGetJobHistory() {
		JobHistorySearchParams searchParams = new JobHistorySearchParams();
		List<Job> jobs = null;
		try {
			jobs = this.jobClient.getJobHistory(searchParams);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(jobs);
		assertNotEquals(jobs.size(), 0);		
		for( Job job : jobs ) {
			this.logger.info( job.toString() );
			assertNotNull(job.getJobIdentifier() );
			assertNotNull(job.getStatus());
			assertNotNull(job.getModel());			
		}
	}		
	
	@Test
	public void testGetJobHistoryByAccessKey() {
		JobHistorySearchParams searchParams = new JobHistorySearchParams();		
		searchParams.setAccessKey( this.apiKey.split("\\.")[0] );
		List<Job> jobs = null;
		try {
			jobs = this.jobClient.getJobHistory(searchParams);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(jobs);
		assertNotEquals(jobs.size(), 0);		
		for( Job job : jobs ) {
			this.logger.info( job.toString() );
			assertNotNull(job.getJobIdentifier() );
			assertNotNull(job.getStatus());
			assertNotNull(job.getModel());			
		}
	}
	
	@Test
	public void testGetJobHistoryByDate() {				
		JobHistorySearchParams searchParams = new JobHistorySearchParams();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, -1);
		// Only Start Date
		searchParams.setStartDate(calendar.getTime());		
		List<Job> jobs = null;
		try {
			jobs = this.jobClient.getJobHistory(searchParams);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(jobs);
		assertNotEquals(jobs.size(), 0);
		//	Only End Date
		searchParams.setStartDate(null);
		searchParams.setEndDate( new Date() );		
		try {
			jobs = this.jobClient.getJobHistory(searchParams);
			fail("This call should fail");
		} catch (ApiException e) {
			// With only the end data it should fail
		}
		// Start and End Date
		searchParams.setStartDate(calendar.getTime());				
		try {
			jobs = this.jobClient.getJobHistory(searchParams);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(jobs);
		assertNotEquals(jobs.size(), 0);
	}

	@Test
	public void testGetJobHistoryByJobIdentifier() {
		JobHistorySearchParams searchParams = new JobHistorySearchParams();
		//Empty array
		searchParams.setJobIdentifiers( new String[]{""} );
		List<Job> jobs = null;
		try {
			jobs = this.jobClient.getJobHistory(searchParams);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(jobs);
		assertEquals(jobs.size(), 0);
		//unexisting identifiers
		searchParams.setJobIdentifiers( new String[]{"A", "B", "C"} );		
		try {
			jobs = this.jobClient.getJobHistory(searchParams);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(jobs);
		assertEquals(jobs.size(), 0);
		//existing identifiers
		Model model = new Model();
		model.setIdentifier("ed542963de");
		model.setVersion("0.0.27");		
		//
		Map<String,String> sourceMap = new HashMap<String,String>();
		sourceMap.put("input.txt", "Modzy is great!");
		JobInput<String> jobInput = new JobInputText();
		jobInput.addSource(sourceMap);
		Job job = new Job();
		job.setModel(model);
		job.setInput(jobInput);
		try {			
			job = this.jobClient.submitJob(job);
			this.logger.info( job.toString() );
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(job);		
		assertNotNull(job.getJobIdentifier());
		searchParams.setJobIdentifiers( new String[]{job.getJobIdentifier()} );		
		try {
			jobs = this.jobClient.getJobHistory(searchParams);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(jobs);
		assertNotEquals(jobs.size(), 0);
		
	}
}
