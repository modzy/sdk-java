package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class Requirement {

	private Integer gpuUnits;
	
	private String cpuAmount;
	
	private String memoryAmount;
	
}
