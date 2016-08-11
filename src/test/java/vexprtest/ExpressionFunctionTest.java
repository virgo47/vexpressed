package vexprtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static vexpressed.meta.ExpressionType.INTEGER;
import static vexpressed.meta.ExpressionType.STRING;

import vexpressed.core.FunctionExecutionFailed;
import vexpressed.core.VariableResolver;
import vexpressed.meta.FunctionMetadata;
import vexpressed.support.FunctionMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

public class ExpressionFunctionTest extends TestBase {

	@Test(expectedExceptions = FunctionExecutionFailed.class, expectedExceptionsMessageRegExp =
		"Cannot execute function func because no function executor was set.")
	public void functionExecutionWithoutExecutorFails() {
		eval("func()");
	}

	@Test
	public void functionExecutionWithConstantFunction() {
		functionExecutor = (fname, params) -> 1;
		assertEquals(eval("func()"), 1);
	}

	@Test
	public void functionDelegatedToObjectInvokeMethodsCorrectly() {
		TestFunctions testFunctions = new TestFunctions();
		testFunctions.invokedFlag = false;
		functionExecutor = new FunctionMapper().scanForFunctions(testFunctions).executor();
		// and it can have dot in ID
		assertEquals(eval("n.op()"), "nop");
		assertTrue(testFunctions.invokedFlag);
	}

	@Test
	public void functionCanProduceRandomNumbersAndConvertIntegerArgumentToBigDecimal() {
		functionExecutor = new FunctionMapper().scanForFunctions(new TestFunctions())
			.executor();
		BigDecimal result = (BigDecimal) eval("rand(10)");
		assertTrue(result.compareTo(BigDecimal.ZERO) != -1);
		assertTrue(result.compareTo(BigDecimal.TEN) == -1);
		assertTrue(result.compareTo((BigDecimal) eval("rand(5)")) != 0); // virtually impossible
	}

	@Test
	public void functionMethodCanBeSpecifiedExplicitly() {
		functionExecutor = new FunctionMapper()
			.registerFunction("reverse", new TestFunctions(),
				"nonAnnotatedMethod", String.class)
			.executor();
		assertEquals(eval("reverse('bomb')"), "bmob");
	}

	@Test
	public void functionCallResolvesNamedParameters() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(), "multiParamFunc",
				String.class, String.class, Integer.class)
			.executor();
		assertEquals(eval("func()"), "nullnullnull");
		assertEquals(eval("func('', 'x')"), "xnull");
		assertEquals(eval("func(NULL, 'x', 5)"), "nullx5");
		assertEquals(eval("func(arg2:5)"), "nullnull5");
		assertEquals(eval("func(arg2:5, arg1:'x', 'y')"), "yx5");
		assertEquals(eval("func('x', arg2:5, 'y')"), "xy5");
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
		assertEquals(eval("func('a','b')"), "ab");
	}

	@Test
	public void annotatedNamesOfParametersAreRecognized() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(eval("func(second: 'a', first: 'b')"), "ba");
	}

	@Test
	public void defaultValueOfParameterIsUsedIfArgumentIsNotProvided() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(eval("func(second: 'a')"), "XXXa");
	}

	@Test
	public void unstatedArgumentMeansNull() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(eval("func(first: 'a')"), "anull");
		assertEquals(eval("func('a')"), "anull");
	}

	@Test
	public void binaryFunctionWithInfixNotation() {
		functionExecutor = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class)
			.executor();
		assertEquals(eval("'a' func 'b'"), "ab");
	}

	@Test
	public void functionWithVariableResolverParameterCanAccessVariables() {
		variableResolver = var -> var.equals("x") ? 5 : null;
		functionExecutor = new FunctionMapper()
			.registerFunction("var_x", new TestFunctions(),
				"var_x", VariableResolver.class)
			.executor(variableResolver);
		assertEquals(eval("var_x() * x"), 25);
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
		//noinspection unchecked
		Map<String, Object> expr = (Map<String, Object>) eval("func()");
		assertThat(expr.get("string")).isNull();
	}
}
