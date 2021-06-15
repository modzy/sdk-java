package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class Statistic {

	private String label;
	
	private String category;
	
	private String type;
	
	private String description;
	
	private Boolean highlight;
	
	private Integer order;
	
	private Object value;
    
	
}
