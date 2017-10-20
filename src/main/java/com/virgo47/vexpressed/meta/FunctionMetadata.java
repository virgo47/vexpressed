package com.virgo47.vexpressed.meta;

/** Metadata about function. */
public class FunctionMetadata {

	public final String name;
	public final ExpressionType returnType;
	public final FunctionParameterDefinition[] params;

	public FunctionMetadata(String name, ExpressionType returnType,
		FunctionParameterDefinition[] params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}
}
