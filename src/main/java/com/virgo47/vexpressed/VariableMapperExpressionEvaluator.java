package com.virgo47.vexpressed;

import com.virgo47.vexpressed.meta.ExpressionMetadata;
import com.virgo47.vexpressed.meta.ExpressionType;
import com.virgo47.vexpressed.support.VariableMapper;

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

	public <RT> RT eval(String expression, T evalContext) {
		return evaluator.eval(expression, variableMapper.resolverFor(evalContext));
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
