package vexpressed.meta;

public class VariableDefinition {

	public final String name;
	public final ExpressionType type;

	public VariableDefinition(String name, ExpressionType type) {
		this.name = name;
		this.type = type;
	}
}
