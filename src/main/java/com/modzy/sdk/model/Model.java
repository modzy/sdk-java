package com.modzy.sdk.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(Include.NON_EMPTY)
public class Model {

	@ToString.Include
	@JsonAlias("modelId")
    private String identifier;
    
    private String permalink;

	@ToString.Include
    private String name;

	@ToString.Include
    private String version;

    private String description;

    private String longDescription;

    private String author;

	@ToString.Include
    private List<String> versions;       

    private String latestActiveVersion;

    private Boolean isAvailable;

    private Boolean isRecommended;

    private String sourceType;

    private Boolean isExperimental;
    
    private List<Tag> tags;

    private List<Feature> features;

    private List<Image> images;

    private Date createdAt;

    private Date updatedAt;     
    
    private String imagePrefix;

    private String repositoryName;

    private Date creationDateTime;

    private Date updateDateTime;       
    
    private Boolean isActive;

    private String latestVersion;       
    
    private Boolean isExpired;
    
    private Date lastActiveDateTime;
    
    private String createdByEmail;
	
}
