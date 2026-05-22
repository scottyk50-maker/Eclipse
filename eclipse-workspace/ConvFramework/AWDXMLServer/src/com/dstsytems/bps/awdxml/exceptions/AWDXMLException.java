package com.dstsytems.bps.awdxml.exceptions;

public class AWDXMLException  extends Exception {
	private static final long serialVersionUID = 1L;

	public AWDXMLException() {
	}

	public AWDXMLException(String message) {
		super(message);
	}

	public AWDXMLException(Throwable cause) {
		super(cause);
	}

	public AWDXMLException(String message, Throwable cause) {
		super(message, cause);
	}
}
