package com.dstsystems.bps.exceptions;

public class CenteraException  extends Exception {
	private static final long serialVersionUID = 1L;

	public CenteraException() {
	}

	public CenteraException(String message) {
		super(message);
	}

	public CenteraException(Throwable cause) {
		super(cause);
	}

	public CenteraException(String message, Throwable cause) {
		super(message, cause);
	}
}
