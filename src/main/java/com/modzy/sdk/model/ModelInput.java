package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelInput {
	
	private String name;

    private String acceptedMediaTypes;

    private Long maximumSize;

    private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcceptedMediaTypes() {
		return acceptedMediaTypes;
	}

	public void setAcceptedMediaTypes(String acceptedMediaTypes) {
		this.acceptedMediaTypes = acceptedMediaTypes;
	}

	public Long getMaximumSize() {
		return maximumSize;
	}

	public void setMaximumSize(Long maximumSize) {
		this.maximumSize = maximumSize;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Input (name=" + name + ", acceptedMediaTypes=" + acceptedMediaTypes + ")";
	}	       
}
