package vexprtest;

import static org.testng.Assert.assertEquals;

import org.antlr.v4.runtime.tree.ParseTree;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vexpressed.ExpressionCalculatorVisitor;
import vexpressed.ExpressionUtils;
import vexpressed.func.ExpressionFunctionExecutor;
import vexpressed.vars.ExpressionVariableResolver;

public class ExpressionWithCustomOperatorsTest {

	private ExpressionVariableResolver variableResolver;
	private ExpressionFunctionExecutor functionExecutor;

	@BeforeMethod
	public void init() {
		variableResolver = var -> null;
		functionExecutor = null;
	}

	@Test
	public void functionExecutionWithoutExecutorFails() {
		// TODO failure
		expr("1 +*+ 2");
	}

	@Test
	public void functionExecutionWithConstantFunction() {
		functionExecutor = (fname, params) -> 1;
		// TODO evaluation
		assertEquals(expr("1 #// 2"), 2);
	}

	private Object expr(String expression) {
		ParseTree parseTree = ExpressionUtils.createParseTree(expression);
		return new ExpressionCalculatorVisitor(variableResolver)
			.withFunctionExecutor(functionExecutor)
			.visit(parseTree);
	}
}
