package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Requirement {

	private Integer gpuUnits;
	
	private String cpuAmount;
	
	private String memoryAmount;

	public Integer getGpuUnits() {
		return gpuUnits;
	}

	public void setGpuUnits(Integer gpuUnits) {
		this.gpuUnits = gpuUnits;
	}

	public String getCpuAmount() {
		return cpuAmount;
	}

	public void setCpuAmount(String cpuAmount) {
		this.cpuAmount = cpuAmount;
	}

	public String getMemoryAmount() {
		return memoryAmount;
	}

	public void setMemoryAmount(String memoryAmount) {
		this.memoryAmount = memoryAmount;
	}
	
	
	
}
