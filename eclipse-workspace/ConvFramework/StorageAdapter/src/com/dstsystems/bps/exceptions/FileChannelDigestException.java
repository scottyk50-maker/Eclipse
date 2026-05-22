package com.dstsystems.bps.exceptions;

public class FileChannelDigestException extends Exception {
	private static final long serialVersionUID = 1L;

	public FileChannelDigestException() {
	}

	public FileChannelDigestException(String message) {
		super(message);
	}

	public FileChannelDigestException(Throwable cause) {
		super(cause);
	}

	public FileChannelDigestException(String message, Throwable cause) {
		super(message, cause);
	}
}
