package com.modzy.sdk.samples;

import java.util.List;

import com.modzy.sdk.ModzyClient;
import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.model.Model;
import com.modzy.sdk.model.ModelVersion;

import io.github.cdimascio.dotenv.Dotenv;

public class ModelSamples {

	public static void main(String[] args) throws ApiException {

		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		
		// Client initialization
		// TODO: set the base url of modzy api and you api key
		ModzyClient modzyClient = new ModzyClient(dotenv.get("MODZY_BASE_URL"), dotenv.get("MODZY_API_KEY"));
		
		// Get all models
		
		List<Model> models = modzyClient.getAllModels();
		for( Model model : models ) {
			System.out.println("model: "+model);
		}
		
		// Get a model by identifier
		
		Model saModel = modzyClient.getModel("ed542963de"); // sentiment-analysis
		
		System.out.println("model: "+saModel);
		
		// Get related models
		
		List<Model> relModels = modzyClient.getRelatedModels("ed542963de");
		for( Model model : relModels ) {
			System.out.println("model: "+model);
		}
		
		
		// Get Versions
		
		List<ModelVersion> versions = modzyClient.getModelVersions("ed542963de");
		for( ModelVersion version : versions ) {
			System.out.println("version: "+version);
		}
	}

}
