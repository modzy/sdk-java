package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Tag {

	@ToString.Include
	private String identifier;

	@ToString.Include
    private String name;
    
    private String dataType;
        
    private Boolean isCategorical;
    
    private Boolean isUserCreated;
	
}
