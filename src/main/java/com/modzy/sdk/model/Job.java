package com.modzy.sdk.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.ToString;

@Data
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown=true)
@ToString(onlyExplicitlyIncluded = true)
public class Job {

	@ToString.Include
	private String jobIdentifier;
    
    private String submittedBy;

	@ToString.Include
    private Model model;

	@ToString.Include
    private JobStatus status;

	@ToString.Include
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
    	this.model.setIdentifier(model.getIdentifier());
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
	
}
