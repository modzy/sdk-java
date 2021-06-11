package com.modzy.sdk.samples;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.modzy.sdk.ModzyClient;
import com.modzy.sdk.dto.ModelSearchParams;
import com.modzy.sdk.exception.ApiException;
import com.modzy.sdk.model.Model;
import com.modzy.sdk.model.ModelInput;
import com.modzy.sdk.model.ModelOutput;
import com.modzy.sdk.model.ModelVersion;

import io.github.cdimascio.dotenv.Dotenv;

public class ModelSamples {

	public static void main(String[] args) throws ApiException {

		// The system admin can provide the right base API URL, the API key can be downloaded from your profile page on Modzy.
		// You can config those params as is described in the readme file (as environment variables, or by using the .env file),
		// or you can just update the BASE_URL and API_KEY vars on this sample code (not recommended for production environments).

		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// The MODZY_BASE_URL should point to the API services route, it may be different from the Modzy page URL.
		// (ie: https://modzy.example.com/api).
		String baseURL = dotenv.get("MODZY_BASE_URL");
		// The MODZY_API_KEY is your own personal API key. It is composed by a public part, a dot character and a private part
		// (ie: AzQBJ3h4B1z60xNmhAJF.uQyQh8putLIRDi1nOldh).
		String apiKey  = dotenv.get("MODZY_API_KEY");

		// Client initialization
		//   Initialize the ApiClient instance with the BASE_URL and the API_KEY to store those arguments
		//   for the following API calls.
		ModzyClient modzyClient = new ModzyClient(baseURL,apiKey);

		// Get all models
		// You can get the full list of models from Modzy by using getAllModels method, this call will return just the
		// identifier and the latest version of each model
		List<Model>	models = modzyClient.getAllModels();
		System.out.println(String.format("all models: %s", models.size()));
		// Also, you can do more interesting search by the get_models method:
		// Search by author
		ModelSearchParams searchParams = new ModelSearchParams();
		searchParams.setAuthor("Open Source");
		models = modzyClient.getModels(searchParams);
		System.out.println(String.format("Open source models: %s", models.size()));
		// Active models
		searchParams = new ModelSearchParams();
		searchParams.setActive(true);
		models = modzyClient.getModels(searchParams);
		System.out.println(String.format("Active models: %s", models.size()));
		// Search by name (and limiting the results)
		searchParams = new ModelSearchParams();
		searchParams.setName("Image");
		searchParams.setPerPage(5);
		models = modzyClient.getModels(searchParams);
		System.out.println(String.format("Models with name start with 'Image': %s", models.size()));
		// Combined search
		searchParams = new ModelSearchParams();
		searchParams.setName("Image");
		searchParams.setAuthor("Open Source");
		searchParams.setActive(true);
		models = modzyClient.getModels(searchParams);
		System.out.println(String.format("Active open source models which name starts with 'Image': %s", models.size()));
		// Get model details
		// the models route didn't return much info about the models, just modelId, latestVersion and versions:
		ModelVersion modelVersion;
		for( Model model : models ){
			System.out.println(String.format(" %s", model));
			// In order to get more info about the models you need to get the details by identifier
			model = modzyClient.getModel(model.getIdentifier());
			// then you'll get all the details about the model
			System.out.println(String.format("Model details properties: %s", getNotNullProperties(model)));
			// In order to get information about the input/output keys and types you need to get the model version
			// details as follows:
			modelVersion = modzyClient.getModelVersion(model.getIdentifier(), model.getLatestVersion());
			// then you'll get all the details about the specific model version
			System.out.println(String.format("ModelVersion details properties: %s", getNotNullProperties(modelVersion)));
			// Probably the more interesting are the ones related with the inputs and outputs of the model
			System.out.println("  inputs:");
			for( ModelInput input : modelVersion.getInputs() ){
				System.out.println(
						String.format("    key %s, type %s, description: %s", input.getName(), input.getAcceptedMediaTypes(), input.getDescription())
				);
			}
			System.out.println("  outputs:");
			for( ModelOutput output : modelVersion.getOutputs() ){
				System.out.println(
						String.format("    key %s, type %s, description: %s", output.getName(), output.getMediaType(), output.getDescription())
				);
			}
		}
		// Get model by name
		// If you aren't familiar with the models ids, you can find the model by name as follows
		Model model = modzyClient.getModelByName("Dataset Joining");
		// the method will return the first coincidence and return the details
		System.out.println(
				String.format(
						"Dataset Joining: id:%s, author: %s, is_active: %s, description: %s",
						model.getIdentifier(),
						model.getAuthor(),
						model.getIsActive(),
						model.getDescription()
				)
		);
		// Finally, you can find the models related with this search
		models = modzyClient.getRelatedModels(model.getIdentifier());
		System.out.println("Related models");
		for( Model modelAux : models ){
			System.out.println(
					String.format("    %s :: %s (%s)", modelAux.getIdentifier(), modelAux.getName(), modelAux.getAuthor())
			);
		}
	}

	private static List<String> getNotNullProperties(Object object){
		List<String> notNullProperties = new ArrayList<>();
		Class<?> theClass = object.getClass();
		Object value;
		for(Field field : theClass.getDeclaredFields()){
			try {
				field.setAccessible(true);
				value = field.get(object);
				if( value != null ){
					notNullProperties.add(field.getName());
				}
			} catch (IllegalAccessException e) {

			}
		}
		return notNullProperties;
	}

}
