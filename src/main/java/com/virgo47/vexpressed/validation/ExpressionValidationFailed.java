package com.virgo47.vexpressed.validation;

import com.virgo47.vexpressed.core.ExpressionException;

public class ExpressionValidationFailed extends ExpressionException {

	public ExpressionValidationFailed(String message) {
		super(message);
	}

	public ExpressionValidationFailed(String message, Throwable cause) {
		super(message, cause);
	}
}
