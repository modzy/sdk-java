package com.modzy.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Before;
import org.junit.Test;

import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.filter.LoggingFilter;
import com.modzy.sdk.model.Job;
import com.modzy.sdk.model.JobInput;
import com.modzy.sdk.model.JobInputText;
import com.modzy.sdk.model.JobOutput;
import com.modzy.sdk.model.JobStatus;
import com.modzy.sdk.model.Model;
import com.modzy.sdk.model.ModelVersion;
import com.modzy.sdk.util.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;

public class TestResultClient {
	
	private ModelClient modelClient;
	private JobClient jobClient;
	private ResultClient resultClient;
	
	private Logger logger;
	
	@Before
	public void setUp() throws Exception {
		this.logger = LoggerFactory.getLogger(this);
		Client client = ClientBuilder.newClient().register(LoggingFilter.class);
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		WebTarget baseTarget = client.target(dotenv.get("MODZY_BASE_URL"));		
		this.modelClient  = new ModelClient(baseTarget, dotenv.get("MODZY_API_KEY"));
		this.jobClient    = new JobClient(baseTarget, dotenv.get("MODZY_API_KEY"));
		this.resultClient = new ResultClient(baseTarget, dotenv.get("MODZY_API_KEY"));
	}
	
	@Test
	public void testGetResult(){
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
		do {
			this.logger.info("Waiting... for run");
			try {				
				Thread.sleep( 15000 );
			} catch (InterruptedException e) {
				fail(e.getMessage()); 
			}
			try {
				job = this.jobClient.getJob(job);
			} catch (ApiException e) {
				fail(e.getMessage());
			}
		}
		while(!job.getStatus().equals(JobStatus.COMPLETED) && !job.getStatus().equals(JobStatus.CANCELED));		
		//
		JobOutput<?> jobOutput = null;
		try {
			jobOutput = this.resultClient.getResult(job);
		} catch (ApiException e) {
			fail(e.getMessage());
		}
		assertNotNull(jobOutput);
		assertEquals(job.getJobIdentifier(), jobOutput.getJobIdentifier());
		assertNotNull(jobOutput.getResults());				
		Map<String,?> mapResult;
		Object result;
		for( String resultsKey : jobOutput.getResults().keySet() ) {
			mapResult = jobOutput.getResults().get(resultsKey);
			assertNotEquals(0, mapResult.size());	
			for( String resultKey : mapResult.keySet() ){
				result = mapResult.get(resultKey);
				this.logger.info("res["+resultsKey+"]["+resultKey+"]: "+result.getClass()+" => "+result.toString());
			}
		}		
		
	}	

}
