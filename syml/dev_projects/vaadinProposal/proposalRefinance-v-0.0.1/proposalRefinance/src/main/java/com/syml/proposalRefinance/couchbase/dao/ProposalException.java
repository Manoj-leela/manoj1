package com.syml.proposalRefinance.couchbase.dao;

public class ProposalException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6011398114146739040L;

	public ProposalException() {
		super();
		
	}

	public ProposalException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ProposalException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public ProposalException(String message) {
		super(message);
		
	}

	public ProposalException(Throwable cause) {
		super(cause);
		
	}
}
