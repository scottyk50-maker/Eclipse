package com.dstsystems.bps.storageadapter.snowbound;

public class SnowBoundFileTypeException   extends Exception {
	private static final long serialVersionUID = 1L;

	public SnowBoundFileTypeException() {
	}

	public SnowBoundFileTypeException(String message) {
		super(message);
	}

	public SnowBoundFileTypeException(Throwable cause) {
		super(cause);
	}

	public SnowBoundFileTypeException(String message, Throwable cause) {
		super(message, cause);
	}
}