package com.syml.todoist;

public class TaskCreationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaskCreationException() {
		super();
	}

	public TaskCreationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TaskCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskCreationException(String message) {
		super(message);
	}

	public TaskCreationException(Throwable cause) {
		super(cause);
	}

}
