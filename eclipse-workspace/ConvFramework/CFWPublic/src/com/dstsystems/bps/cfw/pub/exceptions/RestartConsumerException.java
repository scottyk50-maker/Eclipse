package com.dstsystems.bps.cfw.pub.exceptions;

public class RestartConsumerException  extends Exception {
	private static final long serialVersionUID = 1L;

	public RestartConsumerException() {
	}

	public RestartConsumerException(String message) {
		super(message);
	}

	public RestartConsumerException(Throwable cause) {
		super(cause);
	}

	public RestartConsumerException(String message, Throwable cause) {
		super(message, cause);
	}
}
