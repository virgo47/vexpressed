package vexpressed.func;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import vexpressed.ExpressionType;

/**
 * Function executor delegating function calls to another object. Every registered function
 * remembers the object (delegate) and method. Methods can be registered explicitly with
 * {@link #registerFunction(String, Object, String, Class[])} or object can be scanned for methods
 * annotated with {@link ExpressionFunction} using {@link #scanForFunctions(Object)}.
 * <p>
 * This implementation does NOT allow overloading of parameters - function name is the only
 * identifying element. This means that complex overloaded functions must deal with it inside
 * and it may affect result type resolution too - e.g. a function that returns String for input
 * String, but Integer for input Integer will still report {@link ExpressionType#OBJECT} as a
 * return type (common type).
 * <p>
 * Scanning can find multiple methods with the same name, but these can still have different
 * function names - that's OK. If some function name "overrides" another one, previously scanned
 * method/function will be overridden and lost.
 */
public class DelegateFunctionExecutor
	implements ExpressionFunctionExecutor, ExpressionFunctionTypeResolver
{

	private final Map<String, MethodInfo> methodMap = new HashMap<>();

	/**
	 * Finds all public methods annotated by {@link ExpressionFunction} and populates
	 * method map from these. Can be called repeatedly. Can be also called with a class
	 * object, in which case delegate object is null, but can be used for functions
	 * implemented by static methods.
	 */
	public DelegateFunctionExecutor scanForFunctions(Object delegate) {
		Class delegateClass = delegate.getClass();
		if (delegate instanceof Class) {
			delegateClass = (Class) delegate;
			delegate = null;
		}
		return scanForFunctions(delegateClass, delegate);
	}

	private DelegateFunctionExecutor scanForFunctions(Class delegateClass, Object delegate) {
		Method[] methods = delegateClass.getDeclaredMethods();
		for (Method method : methods) {
			ExpressionFunction annotation = method.getAnnotation(ExpressionFunction.class);
			if (annotation != null) {
				String functionName = annotation.value();
				if (functionName.isEmpty()) {
					functionName = method.getName();
				}

				methodMap.put(functionName, createMethodInfo(delegate, method, annotation));
			}
		}

		return this;
	}

	private MethodInfo createMethodInfo(
		Object delegate, Method method, ExpressionFunction annotation)
	{
		Parameter[] methodParameters = method.getParameters();
		FunctionParameterDefinition[] paramDefs =
			new FunctionParameterDefinition[methodParameters.length];

		for (int i = 0; i < paramDefs.length; i++) {
			String[] annotatedNames = annotation != null ? annotation.paramNames() : null;
			paramDefs[i] = new FunctionParameterDefinition(
				annotatedNames != null && annotatedNames.length > i
					? annotatedNames[i]
					: methodParameters[i].getName(),
				ExpressionType.fromClass(methodParameters[i].getType()));
		}

		return new MethodInfo(delegate, method,
			method.getReturnType(), paramDefs);
	}

	/**
	 * Explicitly adds method from delegate object to the function to method map. Can be also
	 * called with a class object, in which case delegate object is null, but can be used for
	 * functions implemented by static methods.
	 */
	public DelegateFunctionExecutor registerFunction(
		String functionName, Object delegate, String methodName, Class<?>... parameterTypes)
	{
		try {
			Class<?> delegateClass = delegate.getClass();
			if (delegate instanceof Class) {
				delegateClass = (Class) delegate;
				delegate = null;
			}
			Method method = delegateClass.getDeclaredMethod(methodName, parameterTypes);
			methodMap.put(functionName, createMethodInfo(delegate, method, null));
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
				"Invalid method specified for function " + functionName, e);
		}
		return this;
	}

	@Override
	public Object execute(String functionName, List<FunctionArgument> params) {
		MethodInfo methodInfo = getMethodInfo(functionName);

		Object[] args = prepareArguments(params, methodInfo);
		try {
			return methodInfo.method.invoke(methodInfo.object, args);
		} catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
			String reason = e.getCause() != null
				? e.getCause().getMessage()
				: e.getMessage();
			throw new FunctionExecutionFailed(reason, e);
		}
	}

	private MethodInfo getMethodInfo(String functionName) {
		MethodInfo methodInfo = methodMap.get(functionName);
		if (methodInfo == null) {
			throw new FunctionExecutionFailed("Function '" + functionName + "' was not registered");
		}
		return methodInfo;
	}

	private Object[] prepareArguments(List<FunctionArgument> params, MethodInfo methodInfo) {
		Method method = methodInfo.method;
		Object[] args = new Object[method.getParameterCount()];
		int paramIndex = 0;
		for (Parameter parameter : method.getParameters()) {
			String paramName = methodInfo.paramDefinitions[paramIndex].name;

			Object arg = resolveParam(params, paramName);
			if (parameter.getType() == BigDecimal.class
				&& arg != null
				&& !(arg instanceof BigDecimal))
			{
				arg = new BigDecimal(arg.toString());
			}
			args[paramIndex] = arg;
			paramIndex += 1;
		}
		return args;
	}

	/** Finds named argument or uses the first unnamed - mutates the params list. */
	private Object resolveParam(List<FunctionArgument> params, String name) {
		for (Iterator<FunctionArgument> iterator = params.iterator(); iterator.hasNext(); ) {
			FunctionArgument param = iterator.next();
			if (name.equals(param.parameterName)) {
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
		return null;
	}

	@Override
	public ExpressionType resolveType(
		String functionName, List<FunctionParameterDefinition> argumentDefinitions)
	{
		return getMethodInfo(functionName).returnExpressionType;
	}

	public Set<FunctionDefinition> functionInfo() {
		return methodMap.entrySet().stream()
			.map(e -> new FunctionDefinition(e.getKey(),
				e.getValue().returnExpressionType, e.getValue().paramDefinitions))
			.collect(Collectors.toCollection(() ->
				new TreeSet<>(Comparator.comparing(fd -> fd.name))));
	}

	private static class MethodInfo {

		private final Object object;
		private final Method method;
		private final Class returnType;
		private final ExpressionType returnExpressionType;
		private final FunctionParameterDefinition[] paramDefinitions;

		MethodInfo(Object object, Method method,
			Class returnType, FunctionParameterDefinition[] paramDefinitions)
		{
			this.object = object;
			this.method = method;
			this.returnType = returnType;
			returnExpressionType = ExpressionType.fromClass(returnType);
			this.paramDefinitions = paramDefinitions;
		}
	}
}
