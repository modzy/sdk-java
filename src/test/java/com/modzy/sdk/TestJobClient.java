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

import com.modzy.sdk.dto.JobHistorySearchStatus;
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
		assertNotEquals(model.getVersions().size(), 0);
		ModelVersion modelVersion = null;
		try {
			modelVersion = this.modelClient.getModelVersion(model.getIdentifier(), model.getLatestVersion());
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(modelVersion);
		assertNotEquals(modelVersion.getInputs().size(), 0);
		assertNotEquals(modelVersion.getOutputs().size(), 0);
		//
		Map<String,String> sourceMap = new HashMap<String,String>();
		sourceMap.put("input.txt", "Modzy is great!");
		JobInput<String> jobInput = new JobInputText();
		jobInput.addSource(sourceMap);
		Job job = null;
		try {			
			job = this.jobClient.submitJob(model, modelVersion, jobInput, false);
			this.logger.info( job.toString() );
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(job);
		this.logger.info(job.toString());
		assertNotNull(job.getJobIdentifier());
		assertNotNull(job.getStatus());
	}		
	
	@Test
	public void testGetJob() {
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
			job = this.jobClient.submitJob(model, modelVersion, jobInput, false);
			this.logger.info( job.toString() );
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(job);
		this.logger.info(job.toString());
		assertNotNull(job.getJobIdentifier());
		assertNotNull(job.getStatus());
		try {
			Thread.sleep(5000l);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
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
	public void testCancelJob() {
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
			job = this.jobClient.submitJob(model, modelVersion, jobInput, false);
			this.logger.info( job.toString() );
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(job);
		this.logger.info(job.toString());
		assertNotNull(job.getJobIdentifier());
		assertNotNull(job.getStatus());
		//
		try {
			Thread.sleep(5000l);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		//
		Job job2 = null;
		try {
			job2 = this.jobClient.getJob(job);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		//
		if( JobStatus.COMPLETED != job2.getStatus() ){
			Job job3 = null;
			try {
				job3 = this.jobClient.cancelJob(job);
			} catch (ApiException e) {
				fail(e.getMessage());
			}
			assertNotNull(job3);
			assertNotNull(job3.getJobIdentifier());
			this.logger.info("Job after cancel call "+job3.toString() );
			assertEquals(job.getJobIdentifier(), job3.getJobIdentifier());
			assertEquals(JobStatus.CANCELED, job3.getStatus());
		}
	}
	
	@Test
	public void testGetJobHistoryByName() {
		JobHistorySearchParams searchParams = new JobHistorySearchParams();
		searchParams.setUser("a");
		List<Job> jobs = null;
		try {
			jobs = this.jobClient.getJobHistory(searchParams);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(jobs);
		assertNotEquals(jobs.size(), 0);
		String userName;
		for( Job job : jobs ) {
			this.logger.info( job.toString() );
			assertNotNull(job.getJobIdentifier() );
			assertNotNull(job.getStatus());
			assertNotNull(job.getModel());
		}
	}

	@Test
	public void testGetJobHistoryByModel() {
		JobHistorySearchParams searchParams = new JobHistorySearchParams();
		//Search by model name
		searchParams.setModel( "Sentiment Analysis" );
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
	public void testGetJobHistoryByStatus() {
		JobHistorySearchParams searchParams = new JobHistorySearchParams();
		searchParams.setStatus(JobHistorySearchStatus.TERMINATED);
		List<Job> jobs = null;
		try {
			jobs = this.jobClient.getJobHistory(searchParams);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(jobs);
		assertNotEquals(jobs.size(), 0);
		String userName;
		for( Job job : jobs ) {
			this.logger.info( job.toString() );
			assertNotNull(job.getJobIdentifier() );
			assertNotNull(job.getStatus());
			assertNotNull(job.getModel());
		}
	}
}
