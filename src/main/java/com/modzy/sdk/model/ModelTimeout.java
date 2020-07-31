package com.modzy.sdk.model;

public class ModelTimeout {

	 private Integer status;

	 private Integer run;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getRun() {
		return run;
	}

	public void setRun(Integer run) {
		this.run = run;
	}

	@Override
	public String toString() {
		return "ModelTimeout (status=" + status + ", run=" + run + ")";
	}		
	 
}
