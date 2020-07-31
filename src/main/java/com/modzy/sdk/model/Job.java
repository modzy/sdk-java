package com.modzy.sdk.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Job {

	private String jobIdentifier;
    
    private String submittedBy;

    private Model model;
    
    private JobStatus status;
    
    private JobInput<?> input;
    
    private Date createdAt;
    
    private Date updatedAt;
    
    private Date submittedAt;
    
    private Integer total;
    
    private Integer completed;
    
    private Integer failed;
    
    private Long elapsedTime;
    
    public Job() {
    	super();
    }
    
    public Job(Model model) {
    	this();
    	this.model = new Model();
    	this.model.setModelId(model.getModelId());
    	this.model.setName(model.getName());
    	this.model.setVersion(model.getVersion());    	
    }
    
    public Job(Model model, ModelVersion modelVersion) {
    	this(model);
    	this.model.setVersion( modelVersion.getVersion() );     	
    }
    
    public Job(Model model, ModelVersion modelVersion, JobInput<?> input) {
    	this(model, modelVersion);
    	this.input = input;     	
    }

	public String getJobIdentifier() {
		return jobIdentifier;
	}

	public void setJobIdentifier(String jobIdentifier) {
		this.jobIdentifier = jobIdentifier;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public JobInput<?> getInput() {
		return input;
	}

	public void setInput(JobInput<?> input) {
		this.input = input;
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

	public Date getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(Date submittedAt) {
		this.submittedAt = submittedAt;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getCompleted() {
		return completed;
	}

	public void setCompleted(Integer completed) {
		this.completed = completed;
	}

	public Integer getFailed() {
		return failed;
	}

	public void setFailed(Integer failed) {
		this.failed = failed;
	}

	public Long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	@Override
	public String toString() {
		return "Job (jobIdentifier=" + jobIdentifier + ", model=" + model + ", status=" + status + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + ", submittedAt=" + submittedAt + ", input="
				+ input +" )";
	}  	   
    
	
	
}
