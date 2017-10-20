package com.virgo47.vexpressed.core;

/** Resolver of variables f */
public interface VariableResolver {

	VariableResolver NULL_VARIABLE_RESOLVER = var -> null;

	/** Returns value for the variable name. */
	Object resolve(String variableName) throws UnknownVariable;

	/** Version of {@link #resolve(String)} that does not require cast in the client code. */
	@SuppressWarnings("unchecked")
	default <RT> RT resolveSafe(String variableName) throws UnknownVariable {
		return (RT) resolve(variableName);
	}
}
