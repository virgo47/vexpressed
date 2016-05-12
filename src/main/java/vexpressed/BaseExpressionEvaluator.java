package vexpressed;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import vexpressed.func.DelegateFunctionExecutor;
import vexpressed.vars.ExpressionVariableResolver;
import vexpressed.vars.ExpressionVariableTypeResolver;

/**
 * Default/base expression evaluator with caching, including {@link BasicFunctions}.
 * Whenever repeated expression evaluation is expected with the same set of functions
 * this can be instantiated once and reused. Additional function definitions can
 * added with {@link #addFunctionsFrom(Object)} (scanning) or with {@link #addFunction(String,
 * Object, String, Class[])} (explicit function). See {@link DelegateFunctionExecutor} for more
 * as both methods are delegated to a single internal instance of this executor.
 */
public class BaseExpressionEvaluator {

	private Map<String, ParseTree> expressionCache = new ConcurrentHashMap<>();

	private DelegateFunctionExecutor functionExecutor = new DelegateFunctionExecutor()
		.scanForFunctions(BasicFunctions.class);

	protected BaseExpressionEvaluator addFunctionsFrom(Object functionsDelegate) {
		functionExecutor.scanForFunctions(functionsDelegate);
		return this;
	}

	protected BaseExpressionEvaluator addFunction(
		String functionName, Object delegate, String methodName, Class<?>... parameterTypes)
	{
		functionExecutor.registerFunction(
			functionName, delegate, methodName, parameterTypes);
		return this;
	}

	/** Evaluates expression using provided variableResolver. */
	public ExpressionType check(
		String expression, ExpressionVariableTypeResolver variableResolver)
	{
		ParseTree parseTree = expressionParseTree(expression);
		ExpressionValidatorVisitor visitor = new ExpressionValidatorVisitor(variableResolver)
			.withFunctionTypeResolver(functionExecutor);
		return visitor.visit(parseTree);
	}

	/** Evaluates expression using provided variableResolver. */
	public Object eval(String expression, ExpressionVariableResolver variableResolver) {
		ParseTree parseTree = expressionParseTree(expression);
		ParseTreeVisitor visitor = new ExpressionCalculatorVisitor(variableResolver)
			.withFunctionExecutor(functionExecutor);
		return visitor.visit(parseTree);
	}

	/** Like {@link #eval(String, ExpressionVariableResolver)} but casts the result to boolean. */
	public boolean evalBoolean(String expression, ExpressionVariableResolver variableResolver) {
		Object result = eval(expression, variableResolver);
		return (boolean) result;
	}

	private ParseTree expressionParseTree(String expression) {
		ParseTree parseTree = expressionCache.get(expression);
		if (parseTree == null) {
			parseTree = ExpressionUtils.createParseTree(expression);
			expressionCache.put(expression, parseTree);
		}
		return parseTree;
	}
}
