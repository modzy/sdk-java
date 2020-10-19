package com.modzy.sdk.samples;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
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
		
		// Create a Job with a embedded input, wait, and retrieve results

		// Get the model object:
		// If you already know the model identifier (i.e.: you get from the URL of the model details page or the input sample),
		// you can skip this step, if you don't you can find the model identifier by using its name as follows:
		Model model = modzyClient.getModelByName("Multi-Language OCR");
		// Or if you already know the model id and want to know more about the model, you can use this instead:
		// Model model = modzyClient.getModel("c60c8dbd79");
		// You can find more information about how to query the models on the ModelSamples.java file

		// The model identifier is under the identifier property. You can take a look at the other properties under Model class		
		// Or just log the model identifier, and potencially the latest version
		System.out.println(String.format("The model identifier is %s and the latest version is %s", model.getIdentifier(), model.getLatestVersion()));

		// Get the model version object:
		// If you already know the model version and the input key(s) of the model version you can skip this step. Also, you can
		// use the following code block to know about the inputs keys, and skip the call on future job submissions.
		ModelVersion modelVersion = modzyClient.getModelVersion(model.getModelId(), model.getLatestVersion() );
		// The info stored in modelVersion provides insights about the amount of time that the model can spend processing,
		// the inputs, and output keys of the model.
		System.out.println(String.format("The model version is %s", modelVersion));
		System.out.println(String.format("  timeouts: status %sms, run %sms ",modelVersion.getTimeout().getStatus(), modelVersion.getTimeout().getRun()));
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

		// Send the job:
		// An embedded input is a byte array encoded as a string in Base64, that's very handy for small to middle size files, for
		// bigger files can be a memory issue because you need to load the file in memory (load + encode).

		byte[] imageBytes  = IOUtils.toByteArray( JobAwsInputSample.class.getClassLoader().getResourceAsStream("samples/image.png") );
		byte[] configBytes = IOUtils.toByteArray( JobAwsInputSample.class.getClassLoader().getResourceAsStream("samples/config.json") );

		// With the info about the model (identifier), the model version (version string, input/output keys), you are ready to
		// submit the job. Just prepare the source object:
		JobInput<EmbeddedData> jobInput = new JobInputEmbedded(modelVersion);
		Map<String,EmbeddedData> mapSource = new HashMap<>();
		mapSource.put("input", new EmbeddedData("image/png", imageBytes));
		mapSource.put("config.json", new EmbeddedData("application/json", configBytes));
		jobInput.addSource("source-key", mapSource);
		// An inference job groups input data that you send to a model. You can send any amount of inputs to
		// process and you can identify and refer to a specific input by the key that you assign, for example we can add:
		mapSource = new HashMap<>();
		mapSource.put("input", new EmbeddedData("image/png", imageBytes));
		mapSource.put("config.json", new EmbeddedData("application/json", configBytes));
		jobInput.addSource("second-key", mapSource);
		// You don't need to load all the inputs from files, just convert to bytes as follows:
		configBytes = "{\"languages\": [\"spa\"]}".getBytes();
		mapSource = new HashMap<>();
		mapSource.put("input", new EmbeddedData("image/png", imageBytes));
		mapSource.put("config.json", new EmbeddedData("application/json", configBytes));
		jobInput.addSource("another-key", mapSource);
		//If you send a wrong input key, the model fails to process the input.
		mapSource = new HashMap<>();
		mapSource.put("a.wrong.key", new EmbeddedData("image/png", imageBytes));
		mapSource.put("config.json", new EmbeddedData("application/json", configBytes));
		jobInput.addSource("wrong-key", mapSource);
		// If you send a correct input key, but some wrong values, the model fails to process the input.
		mapSource = new HashMap<>();
		mapSource.put("input", new EmbeddedData("application/json", configBytes));
		mapSource.put("config.json", new EmbeddedData("image/png", imageBytes));
		jobInput.addSource("wrong-values", mapSource);

		// When you have all your inputs ready, you can use our helper method to submit the job as follows:
		Job job = modzyClient.submitJob(model, modelVersion, jobInput);
		// Modzy creates the job and queue for processing. The job object contains all the info that you need to keep track
		// of the process, the most important being the job_identifier and the job status.
		System.out.println(String.format("job: %s", job));
		// The job moves to SUBMITTED, meaning that Modzy acknowledged the job and sent it to the queue to be processed.
		// We provide a helper method to hold until the job finishes processing. Its a good practice to set a max timeout
		// if you're doing a test (ie: 2*status+run). in any case, it will hold until the job
		// finishes and moves to COMPLETED, CANCELED, or TIMEOUT.
		job = modzyClient.blockUntilComplete(job, null);

		// Get the results:
		// Check the status of the job. Jobs may be canceled or can reach a timeout.
		if( JobStatus.COMPLETED.equals( job.getStatus() ) ){
			// A completed job means that all the inputs were processed by the model. Check the results for each
			// input key provided in the source map to see the model output.
			JobOutput<JsonNode> result = modzyClient.getResult(job);
			// The result object has some useful info:
			System.out.println(
					String.format("Result: finished: %s, total: %s, completed: %s, failed: %s",
							result.getFinished(), result.getTotal(), result.getCompleted(), result.getFailed()
					)
			);
			// Notice that we are iterating thought the same keys of the input sources
			for( String key : jobInput.getSources().keySet() ){
				// The result object has the individual results of each job input. In this case the output key is called
				// results.json, so we can get that specific model result as follows:
				if( result.getResults().containsKey(key) ){
					JsonNode modelResult = result.getResults().get(key).get("results.json");
					// The output for this model comes in a JSON format, so we can directly log the model results:
					System.out.print(String.format("    %s: ", key));
					Entry<String, JsonNode> field;
					String textValue;
					for (Iterator<Entry<String, JsonNode>> it = modelResult.fields(); it.hasNext(); ) {
						field = it.next();
						textValue = field.getValue().asText().replace('\n', ' ');
						System.out.print( String.format(" %s: %s", field.getKey(), textValue) );
					}
					System.out.println();
				}
				else{
					// If the model raises an error, we can get the specific error message:
					System.err.println(String.format("    %s: failure: %s", key, result.getFailures().get(key)));
				}
			}
		}
		else{
			System.err.println(String.format("processing failed: %s", job));
		}
		
	}

}
