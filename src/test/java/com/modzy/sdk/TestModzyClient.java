package com.modzy.sdk;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.modzy.sdk.dto.EmbeddedData;
import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.filter.LoggingFilter;
import com.modzy.sdk.util.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;

public class TestModzyClient {

	private ModzyClient modzyClient;
	private Logger logger;
	
	@Before
	public void setUp() throws Exception {
		Client client = ClientBuilder.newClient().register(LoggingFilter.class);
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		WebTarget baseTarget = client.target(dotenv.get("MODZY_BASE_URL"));		
		this.modzyClient = new ModzyClient(baseTarget, dotenv.get("MODZY_API_KEY"));		
		this.logger = LoggerFactory.getLogger(this);
	}
	
	
	@Test
	public void testSentimentAnalysis() {
		Map<String,JsonNode> retValue = null;
		try {			
			retValue = this.modzyClient.submitJobTextBlockUntilComplete("ed542963de", "Modzy is great!");
		} catch (ApiException e) {
			this.logger.log(Level.SEVERE, e.getMessage(), e);
			fail(e.getMessage());
		}
		assertNotNull(retValue);
		assertNotEquals(0,retValue.values().size());
		for( String key : retValue.keySet() ) {
			this.logger.info(retValue.get(key).toString());
			assertNotNull(retValue.get(key));
		}
		this.logger.info(retValue.toString());
	}
	
	@Test
	public void testSentimentAnalysisWithJDBC() {
		Map<String,JsonNode> retValue = null;
		try {			
			retValue = this.modzyClient.submitJobJDBCBlockUntilComplete(
						"ed542963de", "0.0.27", 
						"jdbc:postgresql://testdb-postgres:5432/test",
						"test", "test",
						"org.postgresql.Driver",
						"select text_sample as 'input.txt' from text_samples"
					);
		} catch (ApiException e) {
			this.logger.log(Level.SEVERE, e.getMessage(), e);
			fail(e.getMessage());
		}
		assertNotNull(retValue);
		assertNotEquals(0,retValue.values().size());
		for( String key : retValue.keySet() ) {
			this.logger.info(retValue.get(key).toString());
			assertNotNull(retValue.get(key));
		}
		this.logger.info(retValue.toString());
	}
	
		
	@Test
	public void testRussianToEnglishTranslation() {
		Map<String,JsonNode> retValue = null;
		try {			
			retValue = this.modzyClient.submitJobTextBlockUntilComplete("cbf9e8d6da", "Машинное обучение - это здорово!");
		} catch (ApiException e) {
			this.logger.log(Level.SEVERE, e.getMessage(), e);
			fail(e.getMessage());
		}
		assertNotNull(retValue);
		assertNotEquals(0,retValue.values().size());
		for( String key : retValue.keySet() ) {
			this.logger.info(retValue.get(key).toString());
			assertNotNull(retValue.get(key));
		}
		this.logger.info(retValue.toString());
	}
	
	@Test
	public void testNSFW() {		
		//
		Map<String,JsonNode> retValue = null;
		InputStream is =TestModzyClient.class.getResourceAsStream("/images/example_1.jpg");
		byte[] imageBytes = null;
		try {
			imageBytes = IOUtils.toByteArray( is );                                        
		} catch (IOException e) {
			this.logger.log(Level.SEVERE, e.getMessage(), e);
			fail(e.getMessage());
		}
		assertNotNull(imageBytes);
		try {
			retValue = this.modzyClient.submitJobEmbeddedBlockUntilComplete("e3f73163d3", Arrays.asList(new EmbeddedData(imageBytes)));
		} catch (ApiException e) {
			this.logger.log(Level.SEVERE, e.getMessage(), e);
			fail(e.getMessage());
		}
		assertNotEquals(0,retValue.values().size());
		for( String key : retValue.keySet() ) {
			this.logger.info(retValue.get(key).toString());
			assertNotNull(retValue.get(key));
		}
		this.logger.info(retValue.toString());
	}
	

}
