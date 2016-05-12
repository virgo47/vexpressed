package vexpressed.func;

import java.util.List;

import vexpressed.ExpressionType;

public interface ExpressionFunctionTypeResolver {

	/**
	 * Returns type for the function call. Functional argument contains name just like
	 * normal function execution (for named parameters, otherwise null) but value contains
	 * {@link ExpressionType}.
	 */
	ExpressionType resolveType(String functionName, List<FunctionArgument> params);
}
