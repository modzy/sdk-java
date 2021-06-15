package com.modzy.sdk.dto;

import java.util.List;

import com.modzy.sdk.model.Model;
import com.modzy.sdk.model.Tag;
import lombok.Data;

@Data
public class TagWrapper {

	private List<Tag> tags;
	
	private List<Model> models;
	
}
