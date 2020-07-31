package com.modzy.sdk.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelVersion implements Comparable<ModelVersion>{

	private String version;
   
    private Date createdAt;
    
    private Date updatedAt;
    
    private String inputValidationSchema;
    
    private String createdBy;
    
    private ModelTimeout timeout;
    
    private Requirement requirement;
    
    private ContainerImage containerImage;
    
    private List<ModelInput> inputs;
    
    private List<ModelOutput> outputs;
    
    private List<Statistic> statistics;
    
    private Boolean isActive;
    
    private String longDescription;
    
    private String technicalDetails;
    
    private String sampleInput;

    private String sampleOutput;
    
    private Boolean isAvailable;
    
    private String sourceType;

    private String versionHistory;

    private String performanceSummary;
    
    private String status;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getInputValidationSchema() {
		return inputValidationSchema;
	}

	public void setInputValidationSchema(String inputValidationSchema) {
		this.inputValidationSchema = inputValidationSchema;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public ModelTimeout getTimeout() {
		return timeout;
	}

	public void setTimeout(ModelTimeout timeout) {
		this.timeout = timeout;
	}

	public Requirement getRequirement() {
		return requirement;
	}

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}

	public ContainerImage getContainerImage() {
		return containerImage;
	}

	public void setContainerImage(ContainerImage containerImage) {
		this.containerImage = containerImage;
	}

	public List<ModelInput> getInputs() {
		return inputs;
	}

	public void setInputs(List<ModelInput> inputs) {
		this.inputs = inputs;
	}

	public List<ModelOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<ModelOutput> outputs) {
		this.outputs = outputs;
	}

	public List<Statistic> getStatistics() {
		return statistics;
	}

	public void setStatistics(List<Statistic> statistics) {
		this.statistics = statistics;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getTechnicalDetails() {
		return technicalDetails;
	}

	public void setTechnicalDetails(String technicalDetails) {
		this.technicalDetails = technicalDetails;
	}

	public String getSampleInput() {
		return sampleInput;
	}

	public void setSampleInput(String sampleInput) {
		this.sampleInput = sampleInput;
	}

	public String getSampleOutput() {
		return sampleOutput;
	}

	public void setSampleOutput(String sampleOutput) {
		this.sampleOutput = sampleOutput;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getVersionHistory() {
		return versionHistory;
	}

	public void setVersionHistory(String versionHistory) {
		this.versionHistory = versionHistory;
	}

	public String getPerformanceSummary() {
		return performanceSummary;
	}

	public void setPerformanceSummary(String performanceSummary) {
		this.performanceSummary = performanceSummary;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ModelVersion (version=" + version + ", timeout=" + timeout + ", inputs=" + inputs + ", outputs=" + outputs + ")";
	}

	@Override
	public int compareTo(ModelVersion otherModelVersion) {
		return this.version.compareTo(otherModelVersion.version);
	}       
	
	
}
