package vexpressed.func;

import vexpressed.ExpressionType;

/** Metadata about function parameter. */
public class FunctionParameterDefinition {

	public final String name;
	public final ExpressionType type;

	public FunctionParameterDefinition(String name, ExpressionType type) {
		this.name = name;
		this.type = type;
	}
}
