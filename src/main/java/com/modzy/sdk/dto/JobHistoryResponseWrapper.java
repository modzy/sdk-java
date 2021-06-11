package com.modzy.sdk.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.modzy.sdk.model.Job;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobHistoryResponseWrapper {

	private List<Job> data;

}
