package com.dstsystems.bps.cipher.exception;

public class keyPhraseException extends Exception {
	private static final long serialVersionUID = 1L;

	public keyPhraseException() {
	}

	public keyPhraseException(String message) {
		super(message);
	}

	public keyPhraseException(Throwable cause) {
		super(cause);
	}

	public keyPhraseException(String message, Throwable cause) {
		super(message, cause);
	}

}
