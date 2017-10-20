package com.virgo47.vexpressed;

import com.virgo47.vexpressed.core.ExpressionCalculatorVisitor;
import com.virgo47.vexpressed.core.VariableResolver;
import com.virgo47.vexpressed.meta.ExpressionType;
import com.virgo47.vexpressed.meta.FunctionMetadata;
import com.virgo47.vexpressed.support.FunctionMapper;
import com.virgo47.vexpressed.validation.ExpressionValidatorVisitor;
import com.virgo47.vexpressed.validation.VariableTypeResolver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Default/base expression evaluator with caching, including {@link BasicFunctions}.
 * Whenever repeated expression evaluation is expected with the same set of functions
 * this can be instantiated once and reused. Additional function definitions can
 * added with {@link #addFunctionsFrom(Object)} (scanning) or with {@link #addFunction(String,
 * Object, String, Class[])} (explicit function). See {@link FunctionMapper} for more
 * as both methods are delegated to a single internal instance of this executor.
 * <p>
 * TODO: Right now there is kind of asymmetry here, because this class knows FunctionMapper
 * from support, but doesn't need VariableMapper, only VariableResolver. It should either:
 * - use both and eval would take the target object (holding values) instead of VariableResolver
 * - use none, eval would take both VariableResolver and FunctionExecutor (this is like enhanced
 * VexpressedUtils.eval(expr, varesolver, funexecutor), because it caches, etc.)
 * - split into two classes, one would implement lighter version (with caching, but without
 * support), and the other class would go to support (PREFERRED)
 */
public class BaseExpressionEvaluator {

	public static final int NO_CACHING = 0;
	public static final int DEFAULT_CACHE_CAPACITY = 50;

	private final ParseCache expressionCache;

	private final FunctionMapper functionMapper = new FunctionMapper()
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
		functionMapper.scanForFunctions(functionsDelegate);
		return this;
	}

	public BaseExpressionEvaluator addFunction(
		String functionName, Object delegate, String methodName, Class<?>... parameterTypes)
	{
		functionMapper.registerFunction(
			functionName, delegate, methodName, parameterTypes);
		return this;
	}

	/**
	 * Evaluates expression using provided variableResolver. This does not cache the result
	 * of expression parsing.
	 */
	public ExpressionType check(
		String expression, VariableTypeResolver variableTypeResolver)
	{
		ParseTree parseTree = VexpressedUtils.createParseTree(expression);
		ExpressionValidatorVisitor visitor = new ExpressionValidatorVisitor(variableTypeResolver)
			.withFunctionTypeResolver(functionMapper);
		return visitor.visit(parseTree);
	}

	/**
	 * Evaluates expression using provided variableResolver. Caches the result
	 * of expression parsing for better performance of repeated executions.
	 */
	public Object eval(String expression, VariableResolver variableResolver) {
		ParseTree parseTree = expressionParseTree(expression);
		ExpressionCalculatorVisitor visitor = new ExpressionCalculatorVisitor(variableResolver)
			.withFunctionExecutor(functionMapper.executor(variableResolver));
		adjustCalculatorVisitor(visitor);
		return visitor.visit(parseTree);
	}

	public void adjustCalculatorVisitor(ExpressionCalculatorVisitor expressionCalculatorVisitor) {
		// nothing by default, can be overridden
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

	public Set<FunctionMetadata> functionMetadata() {
		return functionMapper.functionMetadata();
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
