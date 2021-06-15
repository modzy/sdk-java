package com.modzy.sdk.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelVersion implements Comparable<ModelVersion>{

	@ToString.Include
	private String version;
   
    private Date createdAt;
    
    private Date updatedAt;
    
    private String inputValidationSchema;
    
    private String createdBy;

	@ToString.Include
    private ModelTimeout timeout;
    
    private Requirement requirement;
    
    private ContainerImage containerImage;

	@ToString.Include
    private List<ModelInput> inputs;

	@ToString.Include
    private List<ModelOutput> outputs;
    
    private List<Statistic> statistics;

    @JsonProperty("isActive")
    private Boolean active;
    
    private String longDescription;
    
    private String technicalDetails;
    
    private String sampleInput;

    private String sampleOutput;

    @JsonProperty("isAvailable")
    private Boolean available;
    
    private String sourceType;

    private String versionHistory;

    private String performanceSummary;
    
    private String status;

	@Override
	public int compareTo(ModelVersion otherModelVersion) {
		return this.version.compareTo(otherModelVersion.version);
	}
	
}
