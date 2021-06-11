package com.modzy.sdk.model;

import com.modzy.sdk.dto.EmbeddedData;
import lombok.Data;

@Data
public class JobInputEmbedded extends JobInput<EmbeddedData>{

	public JobInputEmbedded() {		
		super();
	}
	
	public JobInputEmbedded(ModelVersion modelVersion) {
		super(modelVersion);
	}

	
}
