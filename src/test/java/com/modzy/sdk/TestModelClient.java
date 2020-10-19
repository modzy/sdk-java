package com.modzy.sdk;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.filter.LoggingFilter;
import com.modzy.sdk.model.Model;
import com.modzy.sdk.model.ModelVersion;
import com.modzy.sdk.util.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;

public class TestModelClient {

	private ModelClient modelClient;
	private Logger logger;

	@Before
	public void setUp() throws Exception {
		this.logger = LoggerFactory.getLogger(this);
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		Client client = ClientBuilder.newClient().register(LoggingFilter.class);
		WebTarget baseTarget = client.target(dotenv.get("MODZY_BASE_URL"));
		this.modelClient = new ModelClient(baseTarget, dotenv.get("MODZY_API_KEY"));
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetAllModels(){
		List<Model> models = null;
		try {
			models = this.modelClient.getAllModels();
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(models);
		assertNotEquals(models.size(), 0);
		for( Model model : models ) {
			this.logger.info( model.toString() );
			assertNotNull(model.getModelId());
			assertNotNull(model.getLatestVersion());
			assertNotNull(model.getVersions());
		}
	}

	@Test
	public void testGetModelByName(){
		Model model = null;
		try {
			model = this.modelClient.getModelByName("Military Equipment Classification");
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(model);
		this.logger.info( model.toString() );
		assertNotNull(model.getModelId());
		assertNotNull(model.getName());
		assertNotNull(model.getDescription());
		assertNotNull(model.getAuthor());
	}

	@Test
	public void testGetModel() {
		Model model = null;
		try {
			model = this.modelClient.getModel("ed542963de");
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(model);
		this.logger.info( model.toString() );
		assertNotNull(model.getModelId());
		assertNotNull(model.getName());
		assertNotNull(model.getDescription());
		assertNotNull(model.getAuthor());
	}

	@Test
	public void testGetRelatedModelsString() {
		List<Model> models = null;
		try {
			models = this.modelClient.getRelatedModels("ed542963de");
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(models);
		assertNotEquals(models.size(), 0);
		this.logger.info( models.toString() );
		for( Model model : models ) {
			assertNotNull(model.getModelId());
			assertNotNull(model.getName());
			assertNotNull(model.getAuthor());
		}
	}

	@Test
	public void testGetRelatedModelsModel() {
		Model saModel = null;
		try {
			saModel = this.modelClient.getModel("ed542963de");
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(saModel);
		assertNotNull(saModel.getModelId());
		this.logger.info( saModel.toString() );
		List<Model> models = null;
		try {
			models = this.modelClient.getRelatedModels(saModel);
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(models);
		assertNotEquals(models.size(), 0);
		this.logger.info( models.toString() );
		for( Model model : models ) {
			assertNotNull(model.getModelId());
			assertNotNull(model.getName());
			assertNotNull(model.getAuthor());
		}

	}

	@Test
	public void testGetModelVersionsString() {
		List<ModelVersion> modelVersions = null;
		try {
			modelVersions = this.modelClient.getModelVersions("ed542963de");
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(modelVersions);
		assertNotEquals(modelVersions.size(), 0);
		this.logger.info( modelVersions.toString() );
		for( ModelVersion modelVersion : modelVersions ) {
			assertNotNull(modelVersion.getVersion());
			try {
				modelVersion = this.modelClient.getModelVersion("ed542963de", modelVersion.getVersion());
			}
			catch(ApiException ae) {
				fail(ae.getMessage());
			}
			assertNotNull(modelVersion.getCreatedBy());
			assertNotNull(modelVersion.getCreatedAt());
		}
	}

	@Test
	public void testGetModelVersionsModel() {
		Model saModel = null;
		try {
			saModel = this.modelClient.getModel("ed542963de");
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(saModel);
		assertNotNull(saModel.getModelId());
		this.logger.info( saModel.toString() );
		List<ModelVersion> modelVersions = null;
		try {
			modelVersions = this.modelClient.getModelVersions(saModel);
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(modelVersions);
		assertNotEquals(modelVersions.size(), 0);
		this.logger.info( modelVersions.toString() );
		for( ModelVersion modelVersion : modelVersions ) {
			assertNotNull(modelVersion.getVersion());
			try {
				modelVersion = this.modelClient.getModelVersion("ed542963de", modelVersion.getVersion());
			}
			catch(ApiException ae) {
				fail(ae.getMessage());
			}
			assertNotNull(modelVersion.getCreatedBy());
			assertNotNull(modelVersion.getCreatedAt());
		}
	}

	@Test
	public void testGetModelVersion() {
		ModelVersion modelVersion = null;
		try {
			modelVersion = this.modelClient.getModelVersion("c60c8dbd79", "0.0.1");
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(modelVersion);
	}

	@Test
	public void testGetModelVersionInputSample() {
		String inputSample = null;
		try {
			inputSample = this.modelClient.getModelVersionInputSample("c60c8dbd79", "0.0.1");
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(inputSample);
	}

	@Test
	public void testGetModelVersionOutputSample() {
		String outputSample = null;
		try {
			outputSample = this.modelClient.getModelVersionOutputSample("c60c8dbd79", "0.0.1");
		}
		catch(ApiException ae) {
			fail(ae.getMessage());
		}
		assertNotNull(outputSample);
	}
}
