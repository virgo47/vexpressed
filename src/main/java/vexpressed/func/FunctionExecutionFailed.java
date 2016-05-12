package vexpressed.func;

import vexpressed.ExpressionException;

public class FunctionExecutionFailed extends ExpressionException {

	public FunctionExecutionFailed(String message) {
		super(message);
	}

	public FunctionExecutionFailed(String message, Throwable cause) {
		super(message, cause);
	}
}
