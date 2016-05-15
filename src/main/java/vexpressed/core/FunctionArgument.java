package vexpressed.core;

public class FunctionArgument {

	public final String parameterName;
	public final Object value;

	FunctionArgument(String parameterName, Object value) {
		this.parameterName = parameterName;
		this.value = value;
	}

	@Override
	public String toString() {
		return (parameterName != null ? parameterName + ':' : "") + value;
	}
}
