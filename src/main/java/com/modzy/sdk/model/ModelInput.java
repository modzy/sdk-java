package com.modzy.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelInput {

	@ToString.Include
	private String name;

	@ToString.Include
    private String acceptedMediaTypes;

    private Long maximumSize;

    private String description;

}
