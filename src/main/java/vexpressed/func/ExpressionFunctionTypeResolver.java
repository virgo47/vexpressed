package vexpressed.func;

import java.util.List;

import vexpressed.ExpressionType;

public interface ExpressionFunctionTypeResolver {

	/** Returns return type of the function call. */
	ExpressionType resolveType(String functionName, List<FunctionParameterDefinition> params);
}
