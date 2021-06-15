package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelTimeout {

	 private Integer status;

	 private Integer run;
	 
}
