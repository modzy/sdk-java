package com.modzy.sdk.dto;

import java.util.List;

import com.modzy.sdk.model.Model;
import com.modzy.sdk.model.Tag;

public class TagWrapper {

	private List<Tag> tags;
	
	private List<Model> models;

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Model> getModels() {
		return models;
	}

	public void setModels(List<Model> models) {
		this.models = models;
	}	
	
}
