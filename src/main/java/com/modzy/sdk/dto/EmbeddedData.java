package com.modzy.sdk.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.modzy.sdk.util.EmbeddedSerializer;
import lombok.Data;

@Data
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
	
}
