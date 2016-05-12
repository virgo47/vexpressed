package vexpressed.vars;

/**
 * Variable resolver based on {@link VariableMapper} wrapping around an object of type {@link T}.
 * This is just a traditional way instead of using lambda {@code variableName ->
 * mapper.resolveVariable(variableName, object)} (and a bit shorter).
 */
public class VariableMapperResolver<T> implements ExpressionVariableResolver {

	private final VariableMapper<T> mapper;
	private final T object;

	public VariableMapperResolver(VariableMapper<T> mapper, T object) {
		this.mapper = mapper;
		this.object = object;
	}

	@Override
	public Object resolve(String variableName) {
		return mapper.resolveVariable(variableName, object);
	}
}
