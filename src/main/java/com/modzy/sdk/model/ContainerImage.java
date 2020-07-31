package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getLoadStatus() {
		return loadStatus;
	}

	public void setLoadStatus(String loadStatus) {
		this.loadStatus = loadStatus;
	}

	public Integer getUploadPercentage() {
		return uploadPercentage;
	}

	public void setUploadPercentage(Integer uploadPercentage) {
		this.uploadPercentage = uploadPercentage;
	}

	public Integer getLoadPercentage() {
		return loadPercentage;
	}

	public void setLoadPercentage(Integer loadPercentage) {
		this.loadPercentage = loadPercentage;
	}

	public Long getContainerImageSize() {
		return containerImageSize;
	}

	public void setContainerImageSize(Long containerImageSize) {
		this.containerImageSize = containerImageSize;
	}

	public String getRegistryHost() {
		return registryHost;
	}

	public void setRegistryHost(String registryHost) {
		this.registryHost = registryHost;
	}

	public String getRepositoryNamespace() {
		return repositoryNamespace;
	}

	public void setRepositoryNamespace(String repositoryNamespace) {
		this.repositoryNamespace = repositoryNamespace;
	}

	public String getImagePrefix() {
		return imagePrefix;
	}

	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}
	
	
}
