package com.modzy.sdk.samples;

import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.modzy.sdk.ModzyClient;
import com.modzy.sdk.dto.S3FileRef;
import com.modzy.sdk.exception.ApiException;

import com.modzy.sdk.model.*;
import io.github.cdimascio.dotenv.Dotenv;

public class JobAwsInputSample {

	public static void main(String[] args) throws ApiException {

		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		
		// Client initialization
		// TODO: set the base url of modzy api and you api key
		ModzyClient modzyClient = new ModzyClient(dotenv.get("MODZY_BASE_URL"), dotenv.get("MODZY_API_KEY"));
		
		// Create a Job with a text input, wait and retrieve results

		//  Retrieve the model object
		Model model = modzyClient.getModelByName("Facial Embedding");
		System.out.println("Model: "+model);

		//  Retrieve the model version
		ModelVersion modelVersion = modzyClient.getModelVersion(model.getModelId(), model.getLatestVersion() );
		System.out.println("ModelVersion: "+modelVersion);

		// Build the job input
		JobInput<S3FileRef> jobInput = new JobInputS3(
				modelVersion,
				"MyAccessKeyID",
				"MySecretAccessKey",
				"TheRegion"
			);
		jobInput.addSource(new S3FileRef("the-bucket", "/the/path/to/the/input_image.png"));
		System.out.println("JobInput: "+jobInput);

		//Submit the job, wait until Modzy complete it and retrieve the results
		JobOutput<JsonNode> jobOutput = modzyClient.submitJobBlockUntilComplete(model, modelVersion, jobInput);
		System.out.println("JobOutput: "+jobOutput);

		//Explore the results
		for( Entry<String,Map<String,JsonNode>>  resultsEntry : jobOutput.getResults().entrySet() ) {
			for(Entry<String,JsonNode> item : resultsEntry.getValue().entrySet() ) {
				System.out.println("result["+resultsEntry.getKey()+"]: "+item.getKey()+" "+item.getValue());
			}
		}
		//Explore the failures
		for( Entry<String,String> failuresEntry : jobOutput.getFailures().entrySet() ){
			System.out.println("failure["+failuresEntry.getKey()+"]: "+failuresEntry.getValue());
		}

	}

}
