package vexpressed.meta;

import java.util.Set;

/** Metadata about known variables and functions (identifiers) for a particular expression. */
public class ExpressionMetadata {

	public final Set<VariableMetadata> variables;
	public final Set<FunctionMetadata> functions;

	public ExpressionMetadata(
		Set<VariableMetadata> variables, Set<FunctionMetadata> functions)
	{
		this.variables = variables;
		this.functions = functions;
	}
}
