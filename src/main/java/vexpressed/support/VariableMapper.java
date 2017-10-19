package vexpressed.support;

import vexpressed.core.UnknownVariable;
import vexpressed.core.VariableResolver;
import vexpressed.meta.ExpressionType;
import vexpressed.meta.VariableMetadata;
import vexpressed.validation.VariableTypeResolver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Configuration of supported variables provided by objects of type {@link T}. This means that
 * the value of a variable (name of which is always String, resolved value can be any type) is
 * somehow extracted from an object of type {@link T}. This can happen either directly, as defined
 * by {@link #define(String, ExpressionType, Function)}, or indirectly adding another mapper with
 * {@link #addDelegate(VariableMapper, Function)}.
 *
 * This configuration object can be used to obtain {@link #variableMetadata()} (list of supported
 * variable names and their types) for a particular expression or a set of possible expressions
 * in a specific context. E.g. there can be an extension point where we can define any expression,
 * but all of them can use only pre-concieved set of variables and functions.
 *
 * It can also resolve variables for a particular object, but this is not a convenient way how to
 * use it during expression evaluation. Instead, use {@link #resolverFor(Object)} to obtain
 * a {@link VariableResolver} that "wraps" around an instance of type {@link T}.
 *
 * @param <T> type of an source object used to resolve the variable
 */
public class VariableMapper<T> implements VariableTypeResolver {

	private Map<String, Function<T, Object>> variableValueFunctions = new HashMap<>();
	private Map<String, ExpressionType> variableTypes = new HashMap<>();
	private boolean finished;

	private List<MapperDelegate> mapperDelegates = new ArrayList<>();

	/**
	 * Defines a variable by its name, type and a resolving function that takes object of type
	 * {@link T} as input.
	 */
	public VariableMapper<T> define(String variableName,
		ExpressionType variableType, Function<T, Object> valueFunction)
	{
		checkThatNotFinished();

		variableValueFunctions.put(variableName, valueFunction);
		variableTypes.put(variableName, variableType);
		return this;
	}

	/**
	 * Defines delegate for resolving unresolved variables. The delegate can be for objects
	 * of different type and objectFunction is used to extract the "sub-object" (identity can
	 * be used, using the same object).
	 *
	 * @noinspection unchecked
	 */
	public <DT> VariableMapper<T> addDelegate(
		VariableMapper<DT> delegate, Function<T, DT> objectFunction)
	{
		checkThatNotFinished();

		mapperDelegates.add(new MapperDelegate(
			(VariableMapper<Object>) delegate, (Function<Object, Object>) objectFunction));
		return this;
	}

	/**
	 * Finishes building of this object, any further {@link #define(String, ExpressionType,
	 * Function)} call throws {@link IllegalStateException}. This allows to expose this object
	 * without fear that it will be modified.
	 */
	public VariableMapper<T> finish() {
		finished = true;
		return this;
	}

	private void checkThatNotFinished() {
		if (finished) {
			throw new IllegalStateException(
				"Entity variables were sealed (method finish was called).");
		}
	}

	/** */
	public Object resolveVariable(String variableName, T object) {
		ResolutionResult result = resolveInternal(variableName, object);
		if (!result.resolved) {
			for (MapperDelegate delegate : mapperDelegates) {
				result = delegate.delegateMapper.resolveInternal(
					variableName, delegate.delegateObjectFunction.apply(object));
				if (result.resolved) break;
			}
		}

		if (result.resolved) {
			return result.value;
		}

		throw new UnknownVariable(variableName);
	}

	private ResolutionResult resolveInternal(String variableName, T object) {
		Function<T, Object> valueFunction = variableValueFunctions.get(variableName);
		return valueFunction != null
			? new ResolutionResult(valueFunction.apply(object))
			: ResolutionResult.UNRESOLVED;
	}

	public Set<VariableMetadata> variableMetadata() {
		TreeSet<VariableMetadata> result = variableTypes.entrySet().stream()
			.map(e -> new VariableMetadata(e.getKey(), e.getValue()))
			.collect(Collectors.toCollection(() ->
				new TreeSet<>(Comparator.comparing(vd -> vd.name))));
		for (MapperDelegate delegate : mapperDelegates) {
			result.addAll(delegate.delegateMapper.variableMetadata());
		}
		return result;
	}

	@Override
	public ExpressionType resolveType(String variableName) {
		ExpressionType expressionType = variableTypes.get(variableName);
		if (expressionType == null) {
			for (MapperDelegate delegate : mapperDelegates) {
				expressionType = delegate.delegateMapper.variableTypeInternal(variableName);
				if (expressionType != null) break;
			}
		}

		if (expressionType != null) {
			return expressionType;
		}

		throw new UnknownVariable(variableName);
	}

	private ExpressionType variableTypeInternal(String variableName) {
		return variableTypes.get(variableName);
	}

	public VariableResolver resolverFor(T object) {
		return var -> resolveVariable(var, object);
	}

	private static class MapperDelegate {
		final VariableMapper<Object> delegateMapper;
		final Function<Object, Object> delegateObjectFunction;

		private MapperDelegate(VariableMapper<Object> delegateMapper,
			Function<Object, Object> delegateObjectFunction)
		{
			this.delegateMapper = delegateMapper;
			this.delegateObjectFunction = delegateObjectFunction;
		}
	}

	private static class ResolutionResult {
		static final ResolutionResult UNRESOLVED = new ResolutionResult();

		final Object value;
		final boolean resolved;

		private ResolutionResult(Object value) {
			this.value = value;
			resolved = true;
		}

		private ResolutionResult() {
			value = null;
			resolved = false;
		}
	}
}
