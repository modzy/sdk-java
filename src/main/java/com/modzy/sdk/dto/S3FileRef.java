package com.modzy.sdk.dto;

public class S3FileRef {

	private String bucket;
	
	private String key;

	public S3FileRef() {
		super();		
	}
		
	public S3FileRef(String bucket, String key) {
		super();
		this.bucket = bucket;
		this.key = key;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "S3FileRef (bucket=" + bucket + ", key=" + key + ")";
	}
	
	
	
}
