package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelOutput {

	@ToString.Include
	private String name;

	@ToString.Include
    private String mediaType;

    private Long maximumSize;

    private String description;
    
}
