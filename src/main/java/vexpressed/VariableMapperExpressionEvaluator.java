package vexpressed;

import vexpressed.meta.ExpressionMetadata;
import vexpressed.meta.ExpressionType;
import vexpressed.support.VariableMapper;

/** Combines {@link BaseExpressionEvaluator} with predefined {@link VariableMapper}. */
public class VariableMapperExpressionEvaluator<T> {

	private final BaseExpressionEvaluator evaluator;
	private final VariableMapper<T> variableMapper;

	public VariableMapperExpressionEvaluator(VariableMapper<T> variableMapper, int cacheCapacity) {
		evaluator = new BaseExpressionEvaluator(cacheCapacity);
		this.variableMapper = variableMapper;
	}

	public VariableMapperExpressionEvaluator(VariableMapper<T> variableMapper) {
		evaluator = new BaseExpressionEvaluator();
		this.variableMapper = variableMapper;
	}

	public Object eval(String expression, T object) {
		return evaluator.eval(expression, variableMapper.resolverFor(object));
	}

	public boolean evalBoolean(String expression, T object) {
		return (boolean) eval(expression, object);
	}

	public ExpressionType check(String expression) {
		return evaluator.check(expression, variableMapper);
	}

	public ExpressionMetadata expressionMetadata() {
		return new ExpressionMetadata(
			variableMapper.variableMetadata(),
			evaluator.functionMetadata());
	}
}
