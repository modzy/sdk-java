package com.modzy.sdk.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.modzy.sdk.model.Job;

@JsonIgnoreProperties(ignoreUnknown=true)
public class JobHistoryResponseWrapper {

	private List<Job> data;

	public List<Job> getData() {
		return data;
	}

	public void setData(List<Job> data) {
		this.data = data;
	}

}
