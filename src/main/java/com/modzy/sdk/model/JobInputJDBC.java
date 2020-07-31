package com.modzy.sdk.model;

public class JobInputJDBC extends JobInput<String>{

	private String url;
	
	private String username;
	
	private String password;
	
	private String driver;
	
	private String query;

	public JobInputJDBC() {
		super();
	}

	public JobInputJDBC(String url, String username, String password, String driver, String query) {
		super();
		this.url = url;
		this.username = username;
		this.password = password;
		this.driver = driver;
		this.query = query;
	}



	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
}
