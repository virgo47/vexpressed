package vexpressed.vars;

import vexpressed.ExpressionType;

public interface ExpressionVariableTypeResolver {

	/** Returns type for the variable name. */
	ExpressionType resolve(String variableName);
}
