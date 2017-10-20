package com.virgo47.vexpressed.validation;

import com.virgo47.vexpressed.meta.ExpressionType;
import com.virgo47.vexpressed.meta.FunctionParameterDefinition;

import java.util.List;

public interface FunctionTypeResolver {

	/** Returns return type of the function call. */
	ExpressionType resolveType(String functionName, List<FunctionParameterDefinition> params);
}
