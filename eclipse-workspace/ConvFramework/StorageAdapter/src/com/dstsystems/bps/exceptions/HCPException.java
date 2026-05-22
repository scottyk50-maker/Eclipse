package com.dstsystems.bps.exceptions;

public class HCPException extends Exception {
	private static final long serialVersionUID = 1L;

	public HCPException() {
	}

	public HCPException(String message) {
		super(message);
	}

	public HCPException(Throwable cause) {
		super(cause);
	}

	public HCPException(String message, Throwable cause) {
		super(message, cause);
	}
}
