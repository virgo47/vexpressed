package vexpressed.core;

public interface VariableResolver {

	/** Returns value for the variable name. */
	Object resolve(String variableName) throws UnknownVariable;
}
