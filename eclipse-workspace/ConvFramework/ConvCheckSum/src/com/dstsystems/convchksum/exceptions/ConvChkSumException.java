package com.dstsystems.convchksum.exceptions;

public class ConvChkSumException extends Exception {
	private static final long serialVersionUID = 1L;

	public ConvChkSumException() {
	}

	public ConvChkSumException(String message) {
		super(message);
	}

	public ConvChkSumException(Throwable cause) {
		super(cause);
	}

	public ConvChkSumException(String message, Throwable cause) {
		super(message, cause);
	}
}
