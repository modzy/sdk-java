package com.modzy.sdk.model;

import lombok.Data;

@Data
public class JobInputText extends JobInput<String>{

	public JobInputText() {
		super();
	}
	
	public JobInputText(ModelVersion modelVersion) {
		super(modelVersion);
	}
	
}
