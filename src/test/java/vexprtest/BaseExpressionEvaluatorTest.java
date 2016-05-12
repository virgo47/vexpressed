package vexprtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertEquals;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vexpressed.BaseExpressionEvaluator;
import vexpressed.func.FunctionExecutionFailed;

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
	public void testContainsFunction(
		String needle, boolean expectedResult)
	{
		boolean result = new BaseExpressionEvaluator()
			.evalBoolean("contains(haystack, needle)", var ->
				var.equals("haystack") ? Arrays.asList("one", "two")
					: var.equals("needle") ? needle
					: null);

		assertThat(result).isEqualTo(expectedResult);
	}

	@Test
	public void instantVariableIsConvertedToIsoString() {
		Instant now = Instant.now();
		Object result = new BaseExpressionEvaluator().eval("asString(var)", var -> now);
		assertEquals(result, now.toString());
	}

	@Test
	public void localDateIsConvertedToIsoString() {
		assertEquals(
			new BaseExpressionEvaluator()
				.eval("asString(var)", var -> LocalDate.of(2015, 8, 26)),
			"2015-08-26");
	}

	@Test
	public void localDateTimeIsConvertedToIsoString() {
		assertEquals(
			new BaseExpressionEvaluator()
				.eval("asString(var)", var -> LocalDateTime.of(2015, 8, 26, 22, 37)),
			"2015-08-26T22:37:00");
	}

	@Test
	public void nonexistentFunctionThrowsException() {
		assertThatThrownBy(() -> new BaseExpressionEvaluator().eval("fun()", var -> null))
			.isInstanceOf(FunctionExecutionFailed.class)
			.hasMessageMatching("Function 'fun' was not registered");
	}
}
