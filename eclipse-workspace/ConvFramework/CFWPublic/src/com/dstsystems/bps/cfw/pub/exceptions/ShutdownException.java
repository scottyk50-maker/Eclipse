package com.dstsystems.bps.cfw.pub.exceptions;

public class ShutdownException extends Exception {
	private static final long serialVersionUID = 1L;

	public ShutdownException() {
	}

	public ShutdownException(String message) {
		super(message);
	}

	public ShutdownException(Throwable cause) {
		super(cause);
	}

	public ShutdownException(String message, Throwable cause) {
		super(message, cause);
	}
}
