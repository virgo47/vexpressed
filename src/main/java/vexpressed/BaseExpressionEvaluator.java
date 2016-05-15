package vexpressed;

import vexpressed.core.ExpressionCalculatorVisitor;
import vexpressed.core.VariableResolver;
import vexpressed.meta.ExpressionType;
import vexpressed.meta.FunctionDefinition;
import vexpressed.support.DelegateFunctionExecutor;
import vexpressed.validation.ExpressionValidatorVisitor;
import vexpressed.validation.VariableTypeResolver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * Default/base expression evaluator with caching, including {@link BasicFunctions}.
 * Whenever repeated expression evaluation is expected with the same set of functions
 * this can be instantiated once and reused. Additional function definitions can
 * added with {@link #addFunctionsFrom(Object)} (scanning) or with {@link #addFunction(String,
 * Object, String, Class[])} (explicit function). See {@link DelegateFunctionExecutor} for more
 * as both methods are delegated to a single internal instance of this executor.
 */
public class BaseExpressionEvaluator {

	public static final int NO_CACHING = 0;
	public static final int DEFAULT_CACHE_CAPACITY = 50;

	private final ParseCache expressionCache;

	private DelegateFunctionExecutor functionExecutor = new DelegateFunctionExecutor()
		.scanForFunctions(BasicFunctions.class);

	/** Creates evaluator with specified cache size - size of 0 disables caching. */
	public BaseExpressionEvaluator(int cacheCapacity) {
		if (cacheCapacity != NO_CACHING) {
			expressionCache = new LruParseCache(cacheCapacity);
		} else {
			expressionCache = DisabledParseCache.INSTANCE;
		}
	}

	/** Creates evaluator with default-sized cache. */
	public BaseExpressionEvaluator() {
		this(DEFAULT_CACHE_CAPACITY);
	}

	public BaseExpressionEvaluator addFunctionsFrom(Object functionsDelegate) {
		functionExecutor.scanForFunctions(functionsDelegate);
		return this;
	}

	public BaseExpressionEvaluator addFunction(
		String functionName, Object delegate, String methodName, Class<?>... parameterTypes)
	{
		functionExecutor.registerFunction(
			functionName, delegate, methodName, parameterTypes);
		return this;
	}

	/**
	 * Evaluates expression using provided variableResolver. This does not cache the result
	 * of expression parsing.
	 */
	public ExpressionType check(
		String expression, VariableTypeResolver variableResolver)
	{
		ParseTree parseTree = VexpressedUtils.createParseTree(expression);
		ExpressionValidatorVisitor visitor = new ExpressionValidatorVisitor(variableResolver)
			.withFunctionTypeResolver(functionExecutor);
		return visitor.visit(parseTree);
	}

	/**
	 * Evaluates expression using provided variableResolver. Caches the result
	 * of expression parsing for better performance of repeated executions.
	 */
	public Object eval(String expression, VariableResolver variableResolver) {
		ParseTree parseTree = expressionParseTree(expression);
		ParseTreeVisitor visitor = new ExpressionCalculatorVisitor(variableResolver)
			.withFunctionExecutor(functionExecutor);
		return visitor.visit(parseTree);
	}

	/** Like {@link #eval(String, VariableResolver)} but casts the result to boolean. */
	public boolean evalBoolean(String expression, VariableResolver variableResolver) {
		Object result = eval(expression, variableResolver);
		return (boolean) result;
	}

	private ParseTree expressionParseTree(String expression) {
		ParseTree parseTree = expressionCache.get(expression);
		if (parseTree == null) {
			parseTree = VexpressedUtils.createParseTree(expression);
			expressionCache.put(expression, parseTree);
		}
		return parseTree;
	}

	public Set<FunctionDefinition> functionInfo() {
		return functionExecutor.functionInfo();
	}

	private interface ParseCache {

		ParseTree get(String expression);

		ParseTree put(String expression, ParseTree parseTree);
	}

	private static class LruParseCache extends LinkedHashMap<String, ParseTree>
		implements ParseCache
	{

		private final int maxSize;

		private LruParseCache(int maxSize) {
			// max size should be like 75% loadFactor, +1 is for small caches to avoid resize
			// number 1f used for loadFactor will never be reached
			// true for access order => LRU
			super((maxSize + 1) * 4 / 3, 1f, true);
			this.maxSize = maxSize;
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry<String, ParseTree> eldest) {
			return size() > maxSize;
		}

		@Override
		public synchronized ParseTree get(String expression) {
			return super.get(expression);
		}

		@Override
		public synchronized ParseTree put(String key, ParseTree value) {
			return super.put(key, value);
		}
	}

	private static class DisabledParseCache implements ParseCache {

		private static final DisabledParseCache INSTANCE = new DisabledParseCache();

		@Override
		public ParseTree get(String expression) {
			return null;
		}

		@Override
		public ParseTree put(String expression, ParseTree parseTree) {
			return null;
		}
	}
}
