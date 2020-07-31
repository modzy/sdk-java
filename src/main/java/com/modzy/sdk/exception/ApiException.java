package com.modzy.sdk.exception;

public class ApiException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8483284489941313684L;

	public ApiException() {
		super();
	}

	public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return super.toString();
	}
	
}
