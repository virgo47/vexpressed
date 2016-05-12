package vexpressed.vars;

import vexpressed.ExpressionException;

public class UnknownVariable extends ExpressionException {

	public final String variableName;

	UnknownVariable(String variableName) {
		super("Unknown variable " + variableName);
		this.variableName = variableName;
	}
}
