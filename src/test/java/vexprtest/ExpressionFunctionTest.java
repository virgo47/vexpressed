package vexprtest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;

import org.antlr.v4.runtime.tree.ParseTree;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vexpressed.ExpressionCalculatorVisitor;
import vexpressed.ExpressionUtils;
import vexpressed.func.DelegateFunctionExecutor;
import vexpressed.func.ExpressionFunctionExecutor;
import vexpressed.func.FunctionExecutionFailed;
import vexpressed.vars.ExpressionVariableResolver;

public class ExpressionFunctionTest {

	private ExpressionVariableResolver variableResolver;
	private ExpressionFunctionExecutor functionExecutor;

	@BeforeMethod
	public void init() {
		variableResolver = var -> null;
		functionExecutor = null;
	}

	@Test(expectedExceptions = FunctionExecutionFailed.class, expectedExceptionsMessageRegExp =
		"Cannot execute function func because no function executor was set.")
	public void functionExecutionWithoutExecutorFails() {
		expr("func()");
	}

	@Test
	public void functionExecutionWithConstantFunction() {
		functionExecutor = (fname, params) -> 1;
		assertEquals(expr("func()"), 1);
	}

	@Test
	public void functionDelegatedToObjectInvokeMethodsCorrectly() {
		TestFunctions testFunctions = new TestFunctions();
		testFunctions.invokedFlag = false;
		functionExecutor = new DelegateFunctionExecutor().scanForFunctions(testFunctions);
		// and it can have dot in ID
		assertEquals(expr("n.op()"), "nop");
		assertTrue(testFunctions.invokedFlag);
	}

	@Test
	public void functionCanProduceRandomNumbersAndConvertIntegerArgumentToBigDecimal() {
		functionExecutor = new DelegateFunctionExecutor().scanForFunctions(new TestFunctions());
		BigDecimal result = (BigDecimal) expr("rand(10)");
		assertTrue(result.compareTo(BigDecimal.ZERO) != -1);
		assertTrue(result.compareTo(BigDecimal.TEN) == -1);
		assertTrue(result.compareTo((BigDecimal) expr("rand(5)")) != 0); // virtually impossible
	}

	@Test
	public void functionMethodCanBeSpecifiedExplicitly() {
		functionExecutor = new DelegateFunctionExecutor()
			.registerFunction("reverse", new TestFunctions(),
				"nonAnnotatedMethod", String.class);
		assertEquals(expr("reverse('bomb')"), "bmob");
	}

	@Test
	public void functionCallResolvesNamedParameters() {
		functionExecutor = new DelegateFunctionExecutor()
			.registerFunction("func", new TestFunctions(), "multiParamFunc",
				String.class, String.class, Integer.class);
		assertEquals(expr("func()"), "nullnullnull");
		assertEquals(expr("func('', 'x')"), "xnull");
		assertEquals(expr("func(NULL, 'x', 5)"), "nullx5");
		assertEquals(expr("func(arg2:5)"), "nullnull5");
		assertEquals(expr("func(arg2:5, arg1:'x', 'y')"), "yx5");
		assertEquals(expr("func('x', arg2:5, 'y')"), "xy5");
	}

	@Test
	public void binaryFunctionWithInfixNotation() {
		functionExecutor = new DelegateFunctionExecutor()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class);
		assertEquals(expr("'a' func 'b'"), "ab");
	}

	private Object expr(String expression) {
		ParseTree parseTree = ExpressionUtils.createParseTree(expression);
		return new ExpressionCalculatorVisitor(variableResolver)
			.withFunctionExecutor(functionExecutor)
			.visit(parseTree);
	}
}
