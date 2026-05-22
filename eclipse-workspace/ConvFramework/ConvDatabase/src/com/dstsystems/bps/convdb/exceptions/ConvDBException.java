package com.dstsystems.bps.convdb.exceptions;

public class ConvDBException  extends Exception 
{
	private static final long serialVersionUID = 1L;

	public ConvDBException() 
	{
	}

	public ConvDBException(String message) 
	{
		super(message);
	}

	public ConvDBException(Throwable cause) 
	{
		super(cause);
	}

	public ConvDBException(String message, Throwable cause) 
	{
		super(message, cause);
	}
}
