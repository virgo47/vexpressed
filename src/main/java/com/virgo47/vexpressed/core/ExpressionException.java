package com.virgo47.vexpressed.core;

public class ExpressionException extends RuntimeException {

	public ExpressionException(String message) {
		super(message);
	}

	public ExpressionException(String message, Throwable cause) {
		super(message, cause);
	}
}
