package com.syml.client;

public class ClientSurveyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6011398114146739040L;

	public ClientSurveyException() {
		super();
		
	}

	public ClientSurveyException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClientSurveyException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public ClientSurveyException(String message) {
		super(message);
		
	}

	public ClientSurveyException(Throwable cause) {
		super(cause);
		
	}
}
