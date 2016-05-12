package vexpressed.func;

public class FunctionArgument {

	public final String name;
	public final Object value;

	public FunctionArgument(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	@Override public String toString() {
		return (name != null ? name + ':' : "") + value;
	}
}
