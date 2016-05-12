package vexpressed.vars;

import vexpressed.ExpressionType;

public class VariableDefinition {

	public final String name;
	public final ExpressionType type;

	VariableDefinition(String name, ExpressionType type) {
		this.name = name;
		this.type = type;
	}
}
