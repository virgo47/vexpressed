package vexpressed.meta;

import java.util.Set;

/** Metadata about known variables and functions (identifiers) for a particular expression. */
public class ExpressionIdentifiers {

	public final Set<VariableDefinition> variables;
	public final Set<FunctionDefinition> functions;

	public ExpressionIdentifiers(
		Set<VariableDefinition> variables, Set<FunctionDefinition> functions)
	{
		this.variables = variables;
		this.functions = functions;
	}
}
