package vexpressed;

import java.util.Set;

import vexpressed.func.FunctionDefinition;
import vexpressed.vars.VariableDefinition;

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
