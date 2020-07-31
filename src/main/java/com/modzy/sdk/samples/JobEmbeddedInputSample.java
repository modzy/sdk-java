package com.modzy.sdk.samples;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

import com.modzy.sdk.dto.S3FileRef;
import com.modzy.sdk.model.*;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.modzy.sdk.ModzyClient;
import com.modzy.sdk.dto.EmbeddedData;
import com.modzy.sdk.exception.ApiException;

import io.github.cdimascio.dotenv.Dotenv;

public class JobEmbeddedInputSample {

	public static void main(String[] args) throws ApiException, IOException {

		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		
		// Client initialization
		// TODO: set the base url of modzy api and you api key
		ModzyClient modzyClient = new ModzyClient(dotenv.get("MODZY_BASE_URL"), dotenv.get("MODZY_API_KEY"));
		
		// Create a Job with a text input, wait and retrieve results

		InputStream is = JobAwsInputSample.class.getClassLoader().getResourceAsStream("images/image.png");
		byte[] imageBytes = IOUtils.toByteArray( is );

		//  Retrieve the model object
		Model model = modzyClient.getModelByName("NSFW Image Detection");
		System.out.println("Model: "+model);

		//  Retrieve the model version
		ModelVersion modelVersion = modzyClient.getModelVersion( model.getModelId(), model.getLatestVersion() );
		System.out.println("ModelVersion: "+modelVersion);

		// Build the job input
		JobInput<EmbeddedData> jobInput = new JobInputEmbedded(modelVersion);
		jobInput.addSource( new EmbeddedData("image/png", imageBytes) );
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
