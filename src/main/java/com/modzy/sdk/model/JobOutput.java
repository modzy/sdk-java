package com.modzy.sdk.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class JobOutput<T> {

	private String jobIdentifier;    
    
    protected Map<String,JsonNode> results;
    
    protected Map<String,JsonNode> failures;
        
    private Integer total;
    
    private Integer completed;
    
    private Integer failed;
    
	private Boolean finished;
	
	private String submittedByKey;

	private String accountIdentifier;

	private Team team;

	private Boolean explained;

	/**
	 * Getter of the results with a fix to avoid the double nesting
	 * 
	 * @return A result map with the double nesting fixed
	 */
	public Map<String, Map<String,T>> getResults() {
		Map<String, Map<String,T>> realResult = new HashMap<String, Map<String,T>>();
		Map<String,T> mapResult;
		JsonNode nodeResult;
		Iterator<Map.Entry<String, JsonNode>> nrIterator;
		Map.Entry<String, JsonNode> nEntry;
		for( String resultKey : this.results.keySet() ) {
			mapResult = new HashMap<String,T>();
			nodeResult = this.results.get(resultKey);
			nrIterator = nodeResult.fields();
			while(nrIterator.hasNext()) {
				nEntry = nrIterator.next();
				if( nEntry.getKey().contains(".") ) {
					mapResult.put(nEntry.getKey(), (T)nEntry.getValue() );
				}
			}
			realResult.put(resultKey, mapResult);
		}
		return realResult;
	}

	public Map<String, String> getFailures() {
		Map<String,String> realFailures = new HashMap<String,String>();				
		JsonNode nodeResult;		
		for( String resultKey : this.failures.keySet() ) {						
			nodeResult = this.failures.get(resultKey).get("error");
			realFailures.put(resultKey, nodeResult != null ? nodeResult.asText() : "Undefined error" );
		}
		return realFailures;
	}
    

}
