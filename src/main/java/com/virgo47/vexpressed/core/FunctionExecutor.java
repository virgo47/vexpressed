package com.virgo47.vexpressed.core;

import java.util.List;

public interface FunctionExecutor {

	FunctionExecutor NULL_FUNCTION_EXECUTOR = (functionName, params) -> null;

	/**
	 * Returns value for the function call, possibly with parameters.
	 * Some parameters can be named, but this is not guaranteed. Order and name interpretation
	 * is fully deferred to the implementation.
	 */
	Object execute(String functionName, List<FunctionArgument> params)
		throws FunctionExecutionFailed;

	/** Version of {@link #execute(String, List)} that does not require cast in the client code. */
	@SuppressWarnings("unchecked")
	default <RT> RT executeSafe(String functionName, List<FunctionArgument> params)
		throws FunctionExecutionFailed
	{
		return (RT) execute(functionName, params);
	}
}
