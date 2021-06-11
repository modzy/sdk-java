package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class Image {
	
	private String url;
	
	private String caption;
	
	private String alt;
	
	private String relationType;
	
}
