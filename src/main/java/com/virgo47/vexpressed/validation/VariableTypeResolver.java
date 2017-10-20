package com.virgo47.vexpressed.validation;

import com.virgo47.vexpressed.core.VariableResolver;
import com.virgo47.vexpressed.meta.ExpressionType;

/**
 * Resolves the type of the variable which can be used for introspection of a particular
 * expression (or the whole family of expressions in the same context). This describes the
 * variables of an expression(s) even before the expression is evaluated.
 * <p>
 * For functions there is similar type resolver - {@link FunctionTypeResolver}.
 * <p>
 * For resolver of variables to their values during the evaluation see {@link VariableResolver}.
 */
public interface VariableTypeResolver {

	/** Returns type of a variable for the variable name. */
	ExpressionType resolveType(String variableName);
}
