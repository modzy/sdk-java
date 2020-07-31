package com.modzy.sdk.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.modzy.sdk.model.JobStatus;

@JsonInclude(Include.NON_EMPTY)
public class JobHistorySearchParams extends Pagination{

	private String user;
	
	private String accessKey;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date startDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date endDate;
	
	@JsonIgnore
	private String[] jobIdentifiers;
	
	private JobStatus status;

	public JobHistorySearchParams() {
		super();
		this.status=JobStatus.ALL;
	}
	
	public JobHistorySearchParams(String user, String accessKey, Date startDate, Date endDate,
			String[] jobIdentifiers, JobStatus status, Integer page, Integer perPage, 
			String sortBy, String direction) {
		this();
		this.user = user;
		this.accessKey = accessKey;
		this.startDate = startDate;
		this.endDate = endDate;
		this.jobIdentifiers = jobIdentifiers;
		this.status = status;
		this.page = page;
		this.perPage = perPage;
		this.sortBy = sortBy;
		this.direction = direction;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String[] getJobIdentifiers() {
		return jobIdentifiers;
	}

	public void setJobIdentifiers(String[] jobIdentifiers) {
		this.jobIdentifiers = jobIdentifiers;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}
	
}
