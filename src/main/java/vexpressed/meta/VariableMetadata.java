package vexpressed.meta;

public class VariableMetadata {

	public final String name;
	public final ExpressionType type;

	public VariableMetadata(String name, ExpressionType type) {
		this.name = name;
		this.type = type;
	}
}
