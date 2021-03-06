package com.virgo47.vexpressed.test;

import static com.virgo47.vexpressed.meta.ExpressionType.INTEGER;
import static com.virgo47.vexpressed.meta.ExpressionType.STRING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import com.virgo47.vexpressed.core.ExpressionException;
import com.virgo47.vexpressed.core.VariableResolver;
import com.virgo47.vexpressed.meta.FunctionMetadata;
import com.virgo47.vexpressed.support.FunctionMapper;
import org.testng.annotations.Test;

public class ExpressionFunctionTest extends TestBase {

	@Test(expectedExceptions = ExpressionException.class, expectedExceptionsMessageRegExp =
		"Cannot execute function func - no function executor provided!")
	public void functionExecutionWithoutExecutorFails() {
		evalAsObject("func()");
	}

	@Test
	public void functionExecutionWithConstantFunction() {
		functionExecutor = (fname, params) -> 1;
		assertEquals(evalAsObject("func()"), 1);
	}

	@Test
	public void functionDelegatedToObjectInvokeMethodsCorrectly() {
		TestFunctions testFunctions = new TestFunctions();
		testFunctions.invokedFlag = false;
		functionExecutor = new FunctionMapper().scanForFunctions(testFunctions).executor();
		// and it can have dot in ID
		assertEquals(evalAsObject("n.op()"), "nop");
		assertTrue(testFunctions.invokedFlag);
	}

	@Test
	public void functionCanProduceRandomNumbersAndConvertIntegerArgumentToBigDecimal() {
		functionExecutor = new FunctionMapper().scanForFunctions(new TestFunctions())
			.executor();
		BigDecimal result = (BigDecimal) evalAsObject("rand(10)");
		assertTrue(result.compareTo(BigDecimal.ZERO) >= 0);
		assertTrue(result.compareTo(BigDecimal.TEN) < 0);
		assertTrue(result.compareTo((BigDecimal) evalAsObject("rand(5)")) != 0); // virtually impossible
	}

	@Test
	public void functionMethodCanBeSpecifiedExplicitly() {
		functionExecutor = new FunctionMapper()
			.registerFunction("reverse", new TestFunctions(),
				"nonAnnotatedMethod", String.class)
			.executor();
		assertEquals(evalAsObject("reverse('bomb')"), "bmob");
	}

	@Test
	public void functionCallResolvesNamedParameters() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(), "multiParamFunc",
				String.class, String.class, Integer.class)
			.executor();
		assertEquals(evalAsObject("func()"), "nullnullnull");
		assertEquals(evalAsObject("func('', 'x')"), "xnull");
		assertEquals(evalAsObject("func(NULL, 'x', 5)"), "nullx5");
		assertEquals(evalAsObject("func(arg2:5)"), "nullnull5");
		assertEquals(evalAsObject("func(arg2:5, arg1:'x', 'y')"), "yx5");
		assertEquals(evalAsObject("func('x', arg2:5, 'y')"), "xy5");
	}

	@Test
	public void functionInfoReportsParamDefinitions() {
		Set<FunctionMetadata> functionMetadata = new FunctionMapper()
			.registerFunction("func", new TestFunctions(), "multiParamFunc",
				String.class, String.class, Integer.class)
			.functionMetadata();
		assertThat(functionMetadata).hasSize(1);
		FunctionMetadata funcDef1 = functionMetadata.iterator().next();
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
		assertEquals(evalAsObject("func('a','b')"), "ab");
	}

	@Test
	public void annotatedNamesOfParametersAreRecognized() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(evalAsObject("func(second: 'a', first: 'b')"), "ba");
	}

	@Test
	public void defaultValueOfParameterIsUsedIfArgumentIsNotProvided() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(evalAsObject("func(second: 'a')"), "XXXa");
	}

	@Test
	public void unstatedArgumentMeansNull() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(evalAsObject("func(first: 'a')"), "anull");
		assertEquals(evalAsObject("func('a')"), "anull");
	}

	@Test
	public void binaryFunctionWithInfixNotation() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(evalAsObject("'a' func 'b'"), "ab");
	}

	@Test
	public void functionWithVariableResolverParameterCanAccessVariables() {
		variableResolver = var -> var.equals("x") ? 5 : null;
		functionExecutor = new FunctionMapper()
			.registerFunction("var_x", new TestFunctions(),
				"var_x", VariableResolver.class)
			.executor(variableResolver);
		assertEquals(evalAsObject("var_x() * x"), 25);
	}

	@Test
	public void functionInfoWithVariableResolverDoesNotReportResolver() {
		Set<FunctionMetadata> functionMetadata = new FunctionMapper()
			.registerFunction("fun_x", new TestFunctions(),
				"var_x", VariableResolver.class)
			.functionMetadata();
		assertThat(functionMetadata).hasSize(1);
		FunctionMetadata funcDef1 = functionMetadata.iterator().next();
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
		Map<String, Object> expr = eval("func()");
		assertThat(expr.get("string")).isNull();
	}
}
