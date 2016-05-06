package com.next.dynamo.exception;

public class DynamoException extends Exception {

	private static final long serialVersionUID = 1L;

	public DynamoException() {
	}

	public DynamoException(String message) {
		super(message);
	}

	public DynamoException(Throwable cause) {
		super(cause);
	}

	public DynamoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DynamoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
