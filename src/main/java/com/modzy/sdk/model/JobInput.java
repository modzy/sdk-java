package com.modzy.sdk.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME, 
  include = JsonTypeInfo.As.PROPERTY, 
  property = "type")
@JsonSubTypes({ 
  @Type(value = JobInputText.class, name = "text"), 
  @Type(value = JobInputS3.class, name = "aws-s3"),
  @Type(value = JobInputEmbedded.class, name = "embedded"),
  @Type(value = JobInputJDBC.class, name = "jdbc"),
})
/**
 * 
 * This class is an input wrapper for the jobs services, it allows to add 
 * input params for n calls
 *  
 * @author Raul Casallas 
 *
 * @param <T> 
 */
public class JobInput<T> {
	
	protected Map<String,Map<String,T>> sources;
	
	@JsonIgnore
	protected ModelVersion modelVersion;
	
	public JobInput() {
		super();	
		this.sources = new HashMap<String, Map<String,T>>();
	}
	
	public JobInput(ModelVersion modelVersion) {
		this();	
		this.modelVersion = modelVersion;
	}	
	
	/**
	 * 
	 * Add a combination of inputs by searching the source input names, this method requires 
	 * that the {@link JobInput#modelVersion modelVersion} attribute was setted before in order 
	 * to find the keys to the map and use {@link JobInput#addSource(String, Map) addSource} 
	 * method to finally add the source map to the input.
	 * 
	 * @param sourceName Name of the input, if its null it will be generated
	 * @param sources Sequence if source inputs according to the model version specification
	 */
	public void addSourceByName(String sourceName, T ...sources) {
		if( sources == null ) {
			return;
		} 		
		if( this.modelVersion == null ) {
			throw new UnsupportedOperationException("This "+this.getClass().getSimpleName()+" instance should be intializated with the model version");
		}
		if( sources.length != this.modelVersion.getInputs().size() ) {
			throw new IllegalArgumentException("The number of sources provided "+sources.length+" doesn't match the model input size of "+this.modelVersion.getInputs().size() );
		}
		Map<String,T> sourceMap = new HashMap<String,T>();
		for( int i = 0; i < sources.length; i++ ){
			sourceMap.put(this.modelVersion.getInputs().get(i).getName(), sources[i]);			
		}
		this.addSource(sourceName, sourceMap);				
	}
	
	/**
	 * 
	 * Add a combination of inputs by searching the source input names, this method is a  
	 * convenience shortcut to {@link JobInput#addSourceByName(String, Object...) addSource(sourceName, sources)} 
	 * method 
	 * 
	 * @param sources Sequence if source inputs according to the model version specification
	 */
	public void addSource(T ...sources) {
		this.addSourceByName(null, sources);
	}
	
	/**
	 * 
	 * Add a source map (combination of inputs) to this input, referenced by the sourceName key,  
	 * the key names should be settled according the specific model version input names.
	 * 
	 * This method is not thread safe, so you should add sources in order.
	 * 
	 * @param sourceName Name of the input, if its null it will be generated
	 * @param sourceMap input map with keys according the model version input names
	 */
	public void addSource(String sourceName, Map<String,T> sourceMap) {
		sourceName = sourceName != null ? sourceName : "input-"+(this.sources.size()+1);
		this.sources.put(sourceName, sourceMap);		
	}
	
	/**
	 * 
	 * Add a source map (combination of inputs) to this input, this method is a 
	 * convenience shortcut  to {@link JobInput#addSource(String, Map) addSource}
	 * 
	 * @param sourceMap input map with keys according the model version input names
	 */
	public void addSource(Map<String,T> sourceMap) {
		this.addSource(null, sourceMap);
	}
	
	public Map<String, Map<String, T>> getSources() {
		return sources;
	}

	public void setSources(Map<String, Map<String, T>> sources) {
		this.sources = sources;
	}
	
	public ModelVersion getModelVersion() {
		return modelVersion;
	}

	public void setModelVersion(ModelVersion modelVersion) {
		this.modelVersion = modelVersion;
	}

	@Override
	public String toString() {
		return "JobInput [type=" + this.getClass().getSimpleName() + ", sources=" + sources + "]";
	}
	
}
