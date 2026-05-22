package com.dstsystems.bps.exceptions;

public class AFT_FW_Exception extends Exception {
	private static final long serialVersionUID = 1L;

	public AFT_FW_Exception() {
	}

	public AFT_FW_Exception(String message) {
		super(message);
	}

	public AFT_FW_Exception(Throwable cause) {
		super(cause);
	}

	public AFT_FW_Exception(String message, Throwable cause) {
		super(message, cause);
	}
}

