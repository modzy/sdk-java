# Modzy Java SDK

![Modzy Logo](https://www.modzy.com/wp-content/uploads/2020/06/MODZY-RGB-POS.png)

<div align="center">

**Modzy's Java SDK queries models, submits inference jobs, and returns results directly to your editor.**
    

![GitHub contributors](https://img.shields.io/github/contributors/modzy/sdk-java)
![GitHub last commit](https://img.shields.io/github/last-commit/modzy/sdk-java)
![GitHub Release Date](https://img.shields.io/github/issues-raw/modzy/sdk-java)


[The job lifecycle](https://docs.modzy.com/reference/the-job-lifecycle) | [API Keys](https://docs.modzy.com/reference/api-keys-1) | [Samples](https://github.com/modzy/sdk-java/tree/main/samples) | [Documentation](https://docs.modzy.com/docs)

</div>

## Installation

[![installation](https://github.com/modzy/sdk-java/raw/main/install.gif)](https://asciinema.org/a/CMMbns4Pp4TpTkZdXgMFiguDs)

Clone the repository:

- `$ git clone https://github.com/modzy/sdk-java.git`

Use [maven](https://maven.apache.org/) to install the SDK.

- `$ ./mvnw install -DskipTests=true`

Add the dependency in your `pom.xml` file:

```xml
<dependency>
	<groupId>com.modzy</groupId>
	<artifactId>modzy-sdk</artifactId>
	<version>0.0.1</version>
</dependency>
```


### Get your API key



API keys are security credentials required to perform API requests to Modzy. Our API keys are composed of an ID that is split by a dot into two parts: a public and private part.

The *public* part is the API keys' visible part only used to identify the key and by itself, it’s unable to perform API requests.

The *private* part is the public part's complement and it’s required to perform API requests. Since it’s not stored on Modzy’s servers, it cannot be recovered. Make sure to save it securely. If lost, you can [replace the API key](https://docs.modzy.com/reference/update-a-keys-body).


Find your API key in your user profile. To get your full API key click on "Get key":

<img src="key.png" alt="get key" width="10%"/>



## Initialize

Once you have a `model` and `version` identified, get authenticated with your API key.

```java
ModzyClient modzyClient = new ModzyClient("http://url.to.modzy/api", "API Key");
```

## Basic usage

![Basic Usage](https://github.com/modzy/sdk-java/raw/main/java.gif)

### Browse models

Modzy’s Marketplace includes pre-trained and re-trainable AI models from industry-leading machine learning companies, accelerating the process from data to value.

The Model service drives the Marketplace and can be integrated with other applications, scripts, and systems. It provides routes to list, search, and filter model and model-version details.

[List models](https://docs.modzy.com/reference/list-models):

```java
List<Model> models = modzyClient.getAllModels();
for( Model model : models ){
    System.out.println("Model: "+model);
}
```

Tags help categorize and filter models. They make model browsing easier.

[List tags](https://docs.modzy.com/reference/list-tags):

```java
List<Tag> tags = modzyClient.getTagClient().getAllTags();
for( Tag tag : tags ){
    System.out.println("Tag: "+tag);
}
```

[List models by tag](https://docs.modzy.com/reference/list-models-by-tag):

```java
TagWrapper tagsModels = modzyClient.getTagsAndModels("language_and_text");
for( Model model : tagsModels.getModels() ){
    System.out.println("Model: "+model);
}
```

### Get a model's details

Models accept specific *input file [MIME](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types) types*. Some models may require multiple input file types to run data accordingly. In this sample, we use a model that requires `text/plain`.

Models require inputs to have a specific *input name* declared in the job request. This name can be found in the model’s details. In this sample, we use a model that requires `input.txt`.

Additionally, users can set their own input names. When multiple input items are processed in a job, these names are helpful to identify and get each input’s results. In this sample, we use a model that requires `input-1` and `input-2`.

[Get a model's details](https://docs.modzy.com/reference/list-model-details):

```java
Model saModel = modzyClient.getModel("ed542963de");
System.out.println("Model: "+saModel);
```

Model specific sample requests are available in the version details and in the Model Details page.

[Get version details](https://docs.modzy.com/reference/get-version-details):

```java
ModelVersion modelVersion = modzyClient.getModelVersion("ed542963de", "0.0.27");
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
```

### Submit a job and get results

A *job* is the process that sends data to a model, sets the model to run the data, and returns results.

Modzy supports several *input types* such as `text`, `embedded` for Base64 strings, `aws-s3` and `aws-s3-folder` for inputs hosted in buckets, and `jdbc` for inputs stored in databases. In this sample, we use `text`.

[Here](https://github.com/modzy/sdk-java/blob/readmeUpdates/samples.adoc) are samples to submit jobs with `embedded`, `aws-s3`, `aws-s3-folder`, and `jdbc` input types.

[Submit a job with the model, version, and input items](https://docs.modzy.com/reference/create-a-job-1):

```java
Job job = modzyClient.submitJobText("ed542963de", "0.0.27", "Modzy is great!");
```

[Hold until the inference is complete and results become available](https://docs.modzy.com/reference/get-job-details):

```java
Job job = modzyClient.blockUntilComplete(job, 20000);
```

[Get the results](https://docs.modzy.com/reference/get-results):

Results are available per input item and can be identified with the name provided for each input item upon job request. You can also add an input name to the route and limit the results to any given input item.

Jobs requested for multiple input items may have partial results available prior to job completion.

```java
JobOutput<JsonNode> jobResult = modzyClient.getResult(job);
Map<String,Map<String,JsonNode>> results = jobResult.getResults();
```

### Fetch errors

Errors may arise for different reasons. Fetch errors to know what is their cause and how to fix them.

Error      | Description
---------- | ---------
`ApiException` | Wrapper for different exceptions, check the message and the stacktrace.


Submitting jobs:

```javascript
try{
    Map<String,JsonNode> retValue = this.modzyClient.submitJobTextBlockUntilComplete("ed542963de", "Modzy is great!");
}
catch(ApiException ae){
    System.out.println("The job submission fails with message "+ae.getMessage());
    as.printStackTrace();
}
```

## Features

Modzy supports [batch processing](https://docs.modzy.com/reference/batch-processing), [explainability](https://docs.modzy.com/reference/explainability), and [model drift detection](https://docs.modzy.com/reference/model-drift-1).

## APIs

Here is a list of Modzy APIs. To see all the APIs, check our [Documentation](https://docs.modzy.com/reference/introduction).


| Feature | Code |Api route
| ---     | ---  | ---
|Get all models|modzyClient.getAllModels()|[api/models](https://docs.modzy.com/reference/get-all-models)|
|List models|modzyClient.getModels()|[api/models](https://docs.modzy.com/reference/list-models)|
|Get model details|modzyClient.getModel()|[api/models/:model-id](https://docs.modzy.com/reference/list-model-details)|
|List models by name|modzyClient.getModelByName()|[api/models](https://docs.modzy.com/reference/list-models)|
|List models by tags|modzyClient.getTagsAndModels()|[api/models/tags/:tag-id](https://docs.modzy.com/reference/list-models-by-tag) |
|Get related models|modzyClient.getRelatedModels()|[api/models/:model-id/related-models](https://docs.modzy.com/reference/get-related-models)|
|Get a model's versions|modzyClient.getModelVersions()|[api/models/:model-id/versions](https://docs.modzy.com/reference/list-versions)|
|Get version details|modzyClient.getModelVersions()|[api/models/:model-id/versions/:version-id](https://docs.modzy.com/reference/get-version-details)|
|List tags|modzyClient.getAllTags()|[api/models/tags](https://docs.modzy.com/reference/list-tags)|
|Submit a Job (Text)|modzyClient.submitJobText()|[api/jobs](https://docs.modzy.com/reference/create-a-job-1)|
|Submit a Job (Embedded)|modzyClient.submitJobEmbedded()|[api/jobs](https://docs.modzy.com/reference/create-a-job-1)|
|Submit a Job (AWS S3)|modzyClient.submitJobAWSS3()|[api/jobs](https://docs.modzy.com/reference/create-a-job-1)|
|Submit a Job (JDBC)|modzyClient.submitJobJDBC()|[api/jobs](https://docs.modzy.com/reference/create-a-job-1)|
|Cancel a job|modzyClient.cancelJob()|[api/jobs/:job-id](https://docs.modzy.com/reference/cancel-a-job)  |
|Hold until inference is complete|modzyClient.blockUntilComplete()|[api/jobs/:job-id](https://docs.modzy.com/reference/get-job-details)  |
|Get job details|modzyClient.getJob()|[api/jobs/:job-id](https://docs.modzy.com/reference/get-job-details)  |
|Get results|modzyClient.getResults()|[api/results/:job-id](https://docs.modzy.com/reference/get-results)  |
|List the job history|modzyClient.getJobHistory()|[api/jobs/history](https://docs.modzy.com/reference/list-the-job-history)  |

## Samples

Check out our [samples](https://github.com/modzy/sdk-java/tree/main/java/com/modzy/sdk/samples) for details on specific use cases.

To run samples:

Set the base url and api key in each sample file:

```javascript
// TODO: set the base url of modzy api and you api key
ModzyClient modzyClient = new ModzyClient("https://http://modzy.url", "modzy-api.key");
```

Or follow the instructions [here](https://github.com/modzy/sdk-java/tree/main/contributing.adoc#set-environment-variables-in-bash) to learn more.

And then, you can:

```bash
$ ./mvnw exec:java -Dexec.mainClass="com.modzy.sdk.samples.ModelSamples"
```
## Contributing

We are happy to receive contributions from all of our users. Check out our [contributing file](https://github.com/modzy/sdk-java/tree/main/contributing.adoc) to learn more.

## Code of conduct


[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-v2.0%20adopted-ff69b4.svg)](https://github.com/modzy/sdk-java/tree/main//CODE_OF_CONDUCT.md)
