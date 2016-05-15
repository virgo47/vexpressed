package vexpressed.validation;

import vexpressed.core.ExpressionException;

public class ExpressionValidationFailed extends ExpressionException {

	public ExpressionValidationFailed(String message) {
		super(message);
	}

	public ExpressionValidationFailed(String message, Throwable cause) {
		super(message, cause);
	}
}
