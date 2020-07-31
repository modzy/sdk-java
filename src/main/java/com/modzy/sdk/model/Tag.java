package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Tag {

	private String identifier;
    
    private String name;
    
    private String dataType;
        
    private Boolean isCategorical;
    
    private Boolean isUserCreated;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Boolean getIsCategorical() {
		return isCategorical;
	}

	public void setIsCategorical(Boolean isCategorical) {
		this.isCategorical = isCategorical;
	}

	public Boolean getIsUserCreated() {
		return isUserCreated;
	}

	public void setIsUserCreated(Boolean isUserCreated) {
		this.isUserCreated = isUserCreated;
	}

	@Override
	public String toString() {
		return "Tag [identifier=" + identifier + ", name=" + name + "]";
	}	  
	
}
