package com.modzy.sdk.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.modzy.sdk.util.EmbeddedSerializer;

@JsonSerialize(using = EmbeddedSerializer.class)
public class EmbeddedData {

	String mediaType;
	
	String encoding;
	
	byte[] data;
	
	public EmbeddedData() {
		super();
		this.mediaType = "text/plain";
		this.encoding = "charset=US-ASCII";
		this.data = null;
	}
	
	public EmbeddedData(byte[] data) {
		super();
		this.mediaType = "application/octet-stream";
		this.encoding  = "base64";
		this.data = data;
	}
	
	public EmbeddedData(String mediaType, byte[] data) {
		super();
		this.mediaType = mediaType;
		this.encoding  = "base64";
		this.data = data;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}	
	
}
