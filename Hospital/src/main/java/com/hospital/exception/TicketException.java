package com.hospital.exception;

public class TicketException extends Exception {

	public TicketException() {
		super();
	}

	public TicketException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TicketException(String message, Throwable cause) {	
		super(message, cause);	
	}

	public TicketException(String message) {
		super(message);
	}

	public TicketException(Throwable cause) {
		super(cause);
	}
	
	
}
