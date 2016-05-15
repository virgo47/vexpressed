package vexpressed.validation;

import vexpressed.meta.ExpressionType;
import vexpressed.meta.FunctionParameterDefinition;

import java.util.List;

public interface FunctionTypeResolver {

	/** Returns return type of the function call. */
	ExpressionType resolveType(String functionName, List<FunctionParameterDefinition> params);
}
