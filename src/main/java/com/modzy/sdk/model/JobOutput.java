package com.modzy.sdk.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public class JobOutput<T> {

	private String jobIdentifier;    
    
    protected Map<String,JsonNode> results;
    
    protected Map<String,JsonNode> failures;
        
    private Integer total;
    
    private Integer completed;
    
    private Integer failed;
    
    private Boolean finished;

	public String getJobIdentifier() {
		return jobIdentifier;
	}

	public void setJobIdentifier(String jobIdentifier) {
		this.jobIdentifier = jobIdentifier;
	}

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

	public void setResults(Map<String,JsonNode> results) {
		this.results = results;
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

	public void setFailures(Map<String,JsonNode> failures) {
		this.failures = failures;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getCompleted() {
		return completed;
	}

	public void setCompleted(Integer completed) {
		this.completed = completed;
	}

	public Integer getFailed() {
		return failed;
	}

	public void setFailed(Integer failed) {
		this.failed = failed;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return "JobOutput [jobIdentifier=" + jobIdentifier + ", results=" + results + ", failures=" + failures
				+ ", total=" + total + ", completed=" + completed + ", failed=" + failed + ", finished=" + finished
				+ "]";
	}
    

}
