package com.modzy.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInputJDBC extends JobInput<String>{

	private String url;
	
	private String username;
	
	private String password;
	
	private String driver;
	
	private String query;
	
}
