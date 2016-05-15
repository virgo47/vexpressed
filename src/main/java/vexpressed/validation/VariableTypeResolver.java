package vexpressed.validation;

import vexpressed.meta.ExpressionType;

public interface VariableTypeResolver {

	/** Returns type for the variable name. */
	ExpressionType resolve(String variableName);
}
