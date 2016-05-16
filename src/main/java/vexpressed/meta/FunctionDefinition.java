package vexpressed.meta;

/** Metadata about function. */
public class FunctionDefinition {

	public final String name;
	public final ExpressionType returnType;
	public final FunctionParameterDefinition[] params;

	public FunctionDefinition(String name, ExpressionType returnType,
		FunctionParameterDefinition[] params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}
}
