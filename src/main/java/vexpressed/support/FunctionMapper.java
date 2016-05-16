package vexpressed.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import vexpressed.core.FunctionArgument;
import vexpressed.core.FunctionExecutionFailed;
import vexpressed.core.FunctionExecutor;
import vexpressed.core.VariableResolver;
import vexpressed.meta.ExpressionType;
import vexpressed.meta.FunctionDefinition;
import vexpressed.meta.FunctionParameterDefinition;
import vexpressed.validation.FunctionTypeResolver;

/**
 * Configuration of supported functions which can be implemented as methods on object (non-static)
 * or on class (static). Functions can be registered one-by-one using very explicit {@link
 * #registerFunction(String, Object, String, Class[])} (where object parameter can be either
 * instantiated object or object of type Class) or object/class can be scanned for function methods
 * annotated with {@link ExpressionFunction} using {@link #scanForFunctions(Object)}.
 * <p>
 * {@link FunctionExecutor} can be obtained using {@link #executor()} or {@link
 * #executor(VariableResolver)} - the latter allowing us to work with variables inside the function
 * method if any method parameter was declared to be of {@link VariableResolver} type (this one is
 * removed from the list of available function parameters, which is expected behavior).
 * <p>
 * This implementation does NOT allow overloading of parameters - function name is the only
 * identifying element. This means that complex overloaded functions must deal with it inside
 * and it may affect result type resolution too - e.g. a function that returns String for input
 * String, but Integer for input Integer will still report {@link ExpressionType#OBJECT} as a
 * return type (common type).
 * <p>
 * Scanning can find multiple methods with the same name, but these can still have different
 * function names - that's OK. If some function name "overrides" another one, previously scanned
 * method/function will be overridden and lost. Various functions can even be defined using the
 * same method (explicitly, scanning does not allow this), although right now there is no
 * mechanism to know the function name used for the call. (TODO)
 */
public class FunctionMapper implements FunctionTypeResolver {

	private final Map<String, MethodInfo> methodMap = new HashMap<>();

	/**
	 * Finds all public methods annotated by {@link ExpressionFunction} and populates
	 * method map from these. Can be called repeatedly. Can be also called with a class
	 * object, in which case delegate object is null, but can be used for functions
	 * implemented by static methods.
	 */
	public FunctionMapper scanForFunctions(Object delegate) {
		Class delegateClass = delegate.getClass();
		if (delegate instanceof Class) {
			delegateClass = (Class) delegate;
			delegate = null;
		}
		return scanForFunctions(delegateClass, delegate);
	}

	private FunctionMapper scanForFunctions(Class delegateClass, Object delegate) {
		Method[] methods = delegateClass.getDeclaredMethods();
		for (Method method : methods) {
			ExpressionFunction annotation = method.getAnnotation(ExpressionFunction.class);
			if (annotation != null) {
				String functionName = annotation.value();
				if (functionName.isEmpty()) {
					functionName = method.getName();
				}

				methodMap.put(functionName, createMethodInfo(delegate, method));
			}
		}

		return this;
	}

	private MethodInfo createMethodInfo(Object delegate, Method method) {
		Parameter[] methodParameters = method.getParameters();
		ParameterInfo[] paramListInfo = new ParameterInfo[methodParameters.length];

		for (int i = 0; i < paramListInfo.length; i++) {
			Parameter methodParameter = methodParameters[i];
			ExpressionParam paramAnnotation = methodParameter.getAnnotation(ExpressionParam.class);
			String paramName = paramAnnotation != null && !paramAnnotation.name().isEmpty()
				? paramAnnotation.name()
				: methodParameter.getName();
			Object defaultValue =
				paramAnnotation != null && !paramAnnotation.defaultValue().isEmpty()
					? paramAnnotation.defaultValue()
					: null;
			paramListInfo[i] = new ParameterInfo(
				paramName, methodParameter.getType(), defaultValue);
		}

		return new MethodInfo(delegate, method,
			method.getReturnType(), paramListInfo);
	}

	/**
	 * Explicitly adds method from delegate object to the function to method map. Can be also
	 * called with a class object, in which case delegate object is null, but can be used for
	 * functions implemented by static methods.
	 */
	public FunctionMapper registerFunction(
		String functionName, Object delegate, String methodName, Class<?>... parameterTypes)
	{
		try {
			Class<?> delegateClass = delegate.getClass();
			if (delegate instanceof Class) {
				delegateClass = (Class) delegate;
				delegate = null;
			}
			Method method = delegateClass.getDeclaredMethod(methodName, parameterTypes);
			methodMap.put(functionName, createMethodInfo(delegate, method));
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
				"Invalid method specified for function " + functionName, e);
		}
		return this;
	}

	@Override
	public ExpressionType resolveType(
		String functionName, List<FunctionParameterDefinition> argumentDefinitions)
	{
		return getMethodInfo(functionName).returnExpressionType;
	}

	private MethodInfo getMethodInfo(String functionName) {
		MethodInfo methodInfo = methodMap.get(functionName);
		if (methodInfo == null) {
			throw new FunctionExecutionFailed("Function '" + functionName + "' was not registered");
		}
		return methodInfo;
	}

	public Set<FunctionDefinition> functionInfo() {
		return methodMap.entrySet().stream()
			.map(e -> new FunctionDefinition(e.getKey(),
				e.getValue().returnExpressionType, e.getValue().parameterDefinitions()))
			.collect(Collectors.toCollection(() ->
				new TreeSet<>(Comparator.comparing(fd -> fd.name))));
	}

	/**
	 * Creates {@link FunctionExecutor} based on these function definitions (this object)
	 * with {@link VariableResolver}. This is required way for calling functions that require
	 * variable resolver parameter.
	 */
	public FunctionExecutor executor(VariableResolver variableResolver) {
		return new Executor(variableResolver);
	}

	/** Creates {@link FunctionExecutor} based on these function definitions (this object). */
	public FunctionExecutor executor() {
		return new Executor(null);
	}

	private static class MethodInfo {

		private final Object object;
		private final Method method;
		private final Class returnType;
		private final ExpressionType returnExpressionType;
		// contains also special params, like variable resolver (which is not function parameter)
		private final ParameterInfo[] paramsInfo;

		private MethodInfo(Object object, Method method,
			Class returnType, ParameterInfo[] paramDefinitions)
		{
			this.object = object;
			this.method = method;
			this.returnType = returnType;
			returnExpressionType = ExpressionType.fromClass(returnType);
			this.paramsInfo = paramDefinitions;
		}

		private FunctionParameterDefinition[] parameterDefinitions() {
			return Arrays.stream(paramsInfo)
				.filter(pi -> pi.type != VariableResolver.class)
				.map(ParameterInfo::createDefinition)
				.toArray(FunctionParameterDefinition[]::new);
		}
	}

	private static class ParameterInfo {

		public final String name;
		public final Class type;
		public final Object defaultValue;

		private ParameterInfo(String name, Class type, Object defaultValue) {
			this.name = name;
			this.type = type;
			this.defaultValue = defaultValue;
		}

		public FunctionParameterDefinition createDefinition() {
			return new FunctionParameterDefinition(name, ExpressionType.fromClass(type));
		}
	}

	private class Executor implements FunctionExecutor {

		private final VariableResolver variableResolver;

		private Executor(VariableResolver variableResolver) {
			this.variableResolver = variableResolver;
		}

		@Override
		public Object execute(String functionName, List<FunctionArgument> params) {
			MethodInfo methodInfo = getMethodInfo(functionName);

			Object[] args = prepareArguments(params, methodInfo, variableResolver);
			try {
				return methodInfo.method.invoke(methodInfo.object, args);
			} catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
				String reason = e.getCause() != null
					? e.getCause().getMessage()
					: e.getMessage();
				throw new FunctionExecutionFailed(reason, e);
			}
		}

		private Object[] prepareArguments(
			List<FunctionArgument> params, MethodInfo methodInfo, VariableResolver variableResolver)
		{
			ParameterInfo[] paramsInfo = methodInfo.paramsInfo;
			Object[] args = new Object[paramsInfo.length];
			int paramIndex = 0;
			for (ParameterInfo parameterInfo : paramsInfo) {
				Object arg;
				if (parameterInfo.type == VariableResolver.class) {
					arg = variableResolver;
				} else {
					arg = gerArgumentValue(params, parameterInfo);
				}
				args[paramIndex] = arg;
				paramIndex += 1;
			}
			return args;
		}

		private Object gerArgumentValue(
			List<FunctionArgument> params, ParameterInfo parameterInfo)
		{
			Object arg = resolveParam(params, parameterInfo);
			arg = coerceArgument(parameterInfo, arg);
			return arg;
		}

		/** Finds named argument or uses the first unnamed - <b>mutates the params list</b>. */
		private Object resolveParam(List<FunctionArgument> params, ParameterInfo parameterInfo) {
			for (Iterator<FunctionArgument> iterator = params.iterator(); iterator.hasNext(); ) {
				FunctionArgument param = iterator.next();
				if (parameterInfo.name.equals(param.parameterName)) {
					iterator.remove();
					return param.value;
				}
			}

			// find first unnamed then
			for (Iterator<FunctionArgument> iterator = params.iterator(); iterator.hasNext(); ) {
				FunctionArgument param = iterator.next();
				if (param.parameterName == null) {
					iterator.remove();
					return param.value;
				}
			}
			return parameterInfo.defaultValue;
		}

		/** Converts between similar types, also manages conversion from default value string. */
		private Object coerceArgument(ParameterInfo parameterInfo, Object arg) {
			return ExpressionType.fromClass(parameterInfo.type).promote(arg);
		}
	}
}
