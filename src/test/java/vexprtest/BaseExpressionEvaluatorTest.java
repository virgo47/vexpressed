package vexprtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertEquals;

import vexpressed.BaseExpressionEvaluator;
import vexpressed.core.FunctionExecutionFailed;
import vexpressed.core.VariableResolver;
import vexpressed.support.VariableBinding;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/** This tests also */
public class BaseExpressionEvaluatorTest {

	@DataProvider(name = "test-contains-data")
	public Object[][] testDataProvider() {
		return new Object[][] {
			{"one", true},
			{"three", false},
			{null, false}
		};
	}

	@Test(dataProvider = "test-contains-data")
	public void testContainsFunction(String needle, boolean expectedResult) {
		boolean result = new BaseExpressionEvaluator()
			.evalBoolean("contains(haystack, needle)",
				new VariableBinding()
					.add("haystack", Arrays.asList("one", "two"))
					.add("needle", needle));

		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void asIntegerTest() {
		assertEquals(eval("asInteger(5)"), 5);
		assertEquals(eval("asInteger(5.5)"), 5);
		assertEquals(eval("asInteger(4_000_000_000/4_000_000_001)"), 0);
		assertThatThrownBy(() -> eval("asInteger(4_000_000_000)"))
			.isInstanceOf(FunctionExecutionFailed.class)
			.hasRootCauseInstanceOf(ArithmeticException.class);
	}

	@Test
	public void instantAsStringReturnsIsoString() {
		Instant now = Instant.now();
		Object result = eval("asString(var)", var -> now);
		assertEquals(result, now.toString());
	}

	@Test
	public void localDateAsStringReturnsIsoString() {
		assertEquals(
			eval("asString(var)", var -> LocalDate.of(2015, 8, 26)),
			"2015-08-26");
	}

	@Test
	public void localDateTimeAsStringReturnsIsoString() {
		assertEquals(
			eval("asString(var)", var -> LocalDateTime.of(2015, 8, 26, 22, 37)),
			"2015-08-26T22:37:00");
	}

	@Test
	public void nonexistentFunctionThrowsException() {
		assertThatThrownBy(() -> eval("fun()", var -> null))
			.isInstanceOf(FunctionExecutionFailed.class)
			.hasMessageMatching("Function 'fun' was not registered");
	}

	private Object eval(String expression) {
		return eval(expression, VariableResolver.NULL_VARIABLE_RESOLVER);
	}

	private Object eval(String expression, VariableResolver variableResolver) {
		return new BaseExpressionEvaluator().eval(expression, variableResolver);
	}
}
