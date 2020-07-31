package com.modzy.sdk.model;

import com.modzy.sdk.dto.S3FileRef;

public class JobInputS3 extends JobInput<S3FileRef>{
	
	private String accessKeyID;
	
	private String secretAccessKey;
	
	private String region;
	
	public JobInputS3() {
		super();
	}
	
	public JobInputS3(ModelVersion modelVersion) {
		super(modelVersion);
	}
	
	public JobInputS3(ModelVersion modelVersion, String accessKeyID, String secretAccessKey, String region) {
		super(modelVersion);
		this.accessKeyID = accessKeyID;
		this.secretAccessKey = secretAccessKey;
		this.region = region;
	}
	
	public JobInputS3(String accessKeyID, String secretAccessKey, String region) {
		super();
		this.accessKeyID = accessKeyID;
		this.secretAccessKey = secretAccessKey;
		this.region = region;
	}

	public String getAccessKeyID() {
		return accessKeyID;
	}

	public void setAccessKeyID(String accessKeyID) {
		this.accessKeyID = accessKeyID;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}	
	
}
