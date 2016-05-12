package vexpressed.func;

import vexpressed.ExpressionType;

/** Metadata about function. */
public class FunctionDefinition {

	public final String name;
	public final ExpressionType returnType;
	public final FunctionParameterDefinition[] arguments;

	FunctionDefinition(String name, ExpressionType returnType,
		FunctionParameterDefinition[] arguments)
	{
		this.name = name;
		this.returnType = returnType;
		this.arguments = arguments;
	}
}
