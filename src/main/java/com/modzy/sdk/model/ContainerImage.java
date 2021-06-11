package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContainerImage {
	
	private String uploadStatus;
	
	private String loadStatus;
	
	private Integer uploadPercentage;
	
	private Integer loadPercentage;
	
	private Long containerImageSize;
	
	private String registryHost;

	private String repositoryNamespace;
	
	private String imagePrefix;

	private String repositoryName;
	
	
}
