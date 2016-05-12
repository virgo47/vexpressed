package vexpressed.vars;

public interface ExpressionVariableResolver {

	/** Returns value for the variable name. */
	Object resolve(String variableName);
}
