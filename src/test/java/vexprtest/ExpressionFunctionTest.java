package vexprtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static vexpressed.meta.ExpressionType.INTEGER;
import static vexpressed.meta.ExpressionType.STRING;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vexpressed.VexpressedUtils;
import vexpressed.core.FunctionExecutionFailed;
import vexpressed.core.FunctionExecutor;
import vexpressed.core.VariableResolver;
import vexpressed.meta.FunctionDefinition;
import vexpressed.support.FunctionMapper;

public class ExpressionFunctionTest {

	private VariableResolver variableResolver;
	private FunctionExecutor functionExecutor;

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
		functionExecutor = new FunctionMapper().scanForFunctions(testFunctions).executor();
		// and it can have dot in ID
		assertEquals(expr("n.op()"), "nop");
		assertTrue(testFunctions.invokedFlag);
	}

	@Test
	public void functionCanProduceRandomNumbersAndConvertIntegerArgumentToBigDecimal() {
		functionExecutor = new FunctionMapper().scanForFunctions(new TestFunctions())
			.executor();
		BigDecimal result = (BigDecimal) expr("rand(10)");
		assertTrue(result.compareTo(BigDecimal.ZERO) != -1);
		assertTrue(result.compareTo(BigDecimal.TEN) == -1);
		assertTrue(result.compareTo((BigDecimal) expr("rand(5)")) != 0); // virtually impossible
	}

	@Test
	public void functionMethodCanBeSpecifiedExplicitly() {
		functionExecutor = new FunctionMapper()
			.registerFunction("reverse", new TestFunctions(),
				"nonAnnotatedMethod", String.class)
			.executor();
		assertEquals(expr("reverse('bomb')"), "bmob");
	}

	@Test
	public void functionCallResolvesNamedParameters() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(), "multiParamFunc",
				String.class, String.class, Integer.class)
			.executor();
		assertEquals(expr("func()"), "nullnullnull");
		assertEquals(expr("func('', 'x')"), "xnull");
		assertEquals(expr("func(NULL, 'x', 5)"), "nullx5");
		assertEquals(expr("func(arg2:5)"), "nullnull5");
		assertEquals(expr("func(arg2:5, arg1:'x', 'y')"), "yx5");
		assertEquals(expr("func('x', arg2:5, 'y')"), "xy5");
	}

	@Test
	public void functionInfoReportsParamDefinitions() {
		Set<FunctionDefinition> functionDefinitions = new FunctionMapper()
			.registerFunction("func", new TestFunctions(), "multiParamFunc",
				String.class, String.class, Integer.class)
			.functionInfo();
		assertThat(functionDefinitions).hasSize(1);
		FunctionDefinition funcDef1 = functionDefinitions.iterator().next();
		assertThat(funcDef1.name).isEqualTo("func");
		assertThat(funcDef1.params).hasSize(3);
		assertThat(funcDef1.params[0].name).isEqualTo("arg0");
		assertThat(funcDef1.params[0].type).isEqualTo(STRING);
		assertThat(funcDef1.params[1].type).isEqualTo(STRING);
		assertThat(funcDef1.params[2].type).isEqualTo(INTEGER);
	}

	@Test
	public void binaryFunctionNormalNotation() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(expr("func('a','b')"), "ab");
	}

	@Test
	public void annotatedNamesOfParametersAreRecognized() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(expr("func(second: 'a', first: 'b')"), "ba");
	}

	@Test
	public void defaultValueOfParameterIsUsedIfArgumentIsNotProvided() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(expr("func(second: 'a')"), "XXXa");
	}

	@Test
	public void unstatedArgumentMeansNull() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(expr("func(first: 'a')"), "anull");
		assertEquals(expr("func('a')"), "anull");
	}

	@Test
	public void binaryFunctionWithInfixNotation() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(expr("'a' func 'b'"), "ab");
	}

	@Test
	public void functionWithVariableResolverParameterCanAccessVariables() {
		variableResolver = var -> var.equals("x") ? 5 : null;
		functionExecutor = new FunctionMapper()
			.registerFunction("var_x", new TestFunctions(),
				"var_x", VariableResolver.class)
			.executor(variableResolver);
		assertEquals(expr("var_x() * x"), 25);
	}

	@Test
	public void functionInfoWithVariableResolverDoesNotReportResolver() {
		Set<FunctionDefinition> functionDefinitions = new FunctionMapper()
			.registerFunction("fun_x", new TestFunctions(),
				"var_x", VariableResolver.class)
			.functionInfo();
		assertThat(functionDefinitions).hasSize(1);
		FunctionDefinition funcDef1 = functionDefinitions.iterator().next();
		assertThat(funcDef1.name).isEqualTo("fun_x");
		assertThat(funcDef1.params).hasSize(0);
	}

	@Test
	public void defaultValueConversionsToAllTypesExceptObjectAreSupported() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"functionWithDefaultsForVariousTypes", String.class,
				LocalDate.class, LocalDateTime.class, Instant.class,
				BigDecimal.class, Integer.class, Boolean.class)
			.executor();
		//noinspection unchecked
		Map<String, Object> expr = (Map<String, Object>) expr("func()");
		assertThat(expr.get("string")).isNull();
	}

	private Object expr(String expression) {
		return VexpressedUtils.eval(expression, variableResolver, functionExecutor);
	}
}
