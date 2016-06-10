package vexprtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import vexpressed.core.ExpressionException;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ExpressionBasicTest extends TestBase {

	@Test
	public void primitiveVariableResolverReturnsTheSameValueForAnyVarName() {
		variableResolver = var -> 5;
		assertEquals(eval("var"), 5);
		assertEquals(eval("anyvarworksnow"), 5);
	}

	@Test
	public void variableResolverReturnsValueForOneVarName() {
		variableResolver = var -> var.equals("var") ? 5 : null;
		assertEquals(eval("var"), 5);
		assertEquals(eval("var != null"), true);
		assertEquals(eval("var == null"), false);

		assertEquals(eval("anyvarworksnow"), null);
		assertEquals(eval("anyvarworksnow == null"), true);
	}

	@Test
	public void integerLiteralStaysInteger() {
		assertEquals(eval("5"), 5);
	}

	@Test
	public void tooBigIntegerLiteralConvertedToBigDecimal() {
		assertEquals(eval("5555555555555"), new BigDecimal("5555555555555"));
	}

	@Test
	public void fpNumberCanStartWithPoint() {
		assertEquals(eval(".047"), new BigDecimal("0.047"));
	}

	@Test
	public void fpNumberCanContainExponent() {
		assertEquals(eval("1.47E5"), new BigDecimal("147000"));
	}

	@Test
	public void fpNumberCanContainExplicitlyPositiveExponent() {
		assertEquals(eval(".47E+3"), new BigDecimal("470"));
	}

	@Test
	public void fpNumberCanContainNegativeExponent() {
		assertEquals(eval("1.47E-1"), new BigDecimal("0.147"));
	}

	@Test
	public void fpNumberCanEndWithPoint() {
		assertEquals(eval("3."), new BigDecimal("3"));
	}

	@Test
	public void shortVariableIsConvertedToInteger() {
		variableResolver = var -> (short) 5;
		assertEquals(eval("var").getClass(), Integer.class);
	}

	@Test
	public void smallLongIsConvertedToInteger() {
		variableResolver = var -> 5L;
		assertEquals(eval("var").getClass(), Integer.class);
	}

	@Test
	public void valueOutOfIntegerRangeIsConvertedToBigDecimal() {
		variableResolver = var -> 5555555555555L;
		assertEquals(eval("var").getClass(), BigDecimal.class);
	}

	@Test
	public void fpValueConvertedToBigDecimal() {
		variableResolver = var -> 5.1;
		assertEquals(eval("var"), new BigDecimal("5.1"));
	}

	@Test
	public void booleanVariableStaysBoolean() {
		variableResolver = var -> true;
		assertEquals(eval("var"), true);
	}

	@Test
	public void stringVariableStaysString() {
		variableResolver = var -> "str'val";
		assertEquals(eval("var"), "str'val");
	}

	@Test
	public void stringLiteralEscapedQuoteInterpretedProperly() {
		assertEquals(eval("'str''val'"), "str'val");
	}

	@Test
	public void stringConcatenation() {
		assertEquals(eval("'str' + 'ing'"), "string");
		assertEquals(eval("'str' + 47"), "str47");
	}

	@Test
	public void stringCompare() {
		variableResolver = var -> "str'val";
		assertEquals(eval("var == 'str''val'"), true);
	}

	@Test
	public void booleanLiterals() {
		assertEquals(eval("true"), true);
		assertEquals(eval("false"), false);
	}

	@Test
	public void booleanNot() {
		assertEquals(eval("!true"), false);
		assertEquals(eval("NOT TRUE"), false);
		assertEquals(eval("!false"), true);
		assertEquals(eval("not false"), true);
		assertEquals(eval("!not true"), true);
		assertEquals(eval("not!true"), true);
		assertEquals(eval("! !true"), true);
	}

	@Test
	public void booleanAnd() {
		assertEquals(eval("true && true"), true);
		// keyword is case-insensitive
		assertEquals(eval("TRUE && false"), false);
		assertEquals(eval("FALSE && true"), false);
		assertEquals(eval("false && false"), false);
	}

	@Test
	public void booleanOr() {
		assertEquals(eval("true || true"), true);
		assertEquals(eval("true || false"), true);
		assertEquals(eval("false || true"), true);
		assertEquals(eval("false || false"), false);
		assertEquals(eval("true OR true"), true);
		assertEquals(eval("true OR false"), true);
		assertEquals(eval("false OR true"), true);
		assertEquals(eval("false OR false"), false);
	}

	@DataProvider(name = "test-andor-data")
	public Object[][] testDataProvider() {
		return new Object[][] {
			{false, false, false},
			{false, false, true},
			{false, true, false},
			{false, true, true},
			{true, false, false},
			{true, false, true},
			{true, true, false},
			{true, true, true},
		};
	}

	@Test(dataProvider = "test-andor-data")
	public void booleanAndOrCombination(boolean x, boolean y, boolean z) {
		assertEquals(eval(x + " || " + y + " && " + z),
			eval(x + " || (" + y + " && " + z + ')'));
		assertEquals(eval(x + " && " + y + " || " + z),
			eval("(" + x + " && " + y + ") || " + z));
	}

		@Test
	public void booleanComparison() {
		assertEquals(eval("true EQ true"), true);
		assertEquals(eval("true == false"), false);
		assertEquals(eval("true > false"), true);
		assertEquals(eval("true >= false"), true);
		assertEquals(eval("true <= false"), false);
		assertEquals(eval("true < false"), false);
	}

	@Test
	public void numberComparison() {
		assertEquals(eval("5 > 1"), true);
		assertEquals(eval("1 > 5"), false);
		assertEquals(eval("5 > 5"), false);
		assertEquals(eval("5 < 1"), false);
		assertEquals(eval("1 < 5"), true);
		assertEquals(eval("5 < 5"), false);
		assertEquals(eval("+5 < -7"), false);
		assertEquals(eval("-5 < -(3)"), true);
		assertEquals(eval("5 == 1"), false);
		assertEquals(eval("5 == 5"), true);
		assertEquals(eval("5 != 5"), false);
		assertEquals(eval("5 != 3"), true);
		// mixing BigDecimal and Integer on either side
		assertEquals(eval("5. == 5"), true);
		assertEquals(eval("5 < 5.1"), true);
	}

	@Test
	public void nullComparison() {
		assertEquals(eval("null == null"), true);
		assertEquals(eval("null != null"), false);
		assertEquals(eval("5 != null"), true);
		assertEquals(eval("5 == null"), false);
		assertEquals(eval("null != 5"), true);
		assertEquals(eval("null == 5"), false);
		assertEquals(eval("null > null"), false);
		assertEquals(eval("null < null"), false);
		assertEquals(eval("null <= null"), false);
		assertEquals(eval("null >= null"), false);
	}

	@Test
	public void arithmeticOnDates() {
		variableResolver = val -> LocalDate.of(2016, 2, 29);
		assertEquals(eval("val + 1"), LocalDate.of(2016, 3, 1));
		assertEquals(eval("val + '1d'"), LocalDate.of(2016, 3, 1));
		assertEquals(eval("val + '1m'"), LocalDate.of(2016, 3, 29));
		assertEquals(eval("val + '1y'"), LocalDate.of(2017, 2, 28));
		variableResolver = val -> LocalDate.of(2016, 3, 1);
		assertEquals(eval("val - 1"), LocalDate.of(2016, 2, 29));
		assertEquals(eval("val - '1d'"), LocalDate.of(2016, 2, 29));
		assertEquals(eval("val - '1m'"), LocalDate.of(2016, 2, 1));
		assertEquals(eval("val - '1y'"), LocalDate.of(2015, 3, 1));
	}

	@Test
	public void commentsAreIgnoredProperly() {
		assertFalse((boolean) eval("true AND false"));
		assertFalse((boolean) eval("/*true AND*/ false"));
		assertFalse((boolean) eval("/*true \nAND*/ false"));
		assertTrue((boolean) eval("true /* AND \nfalse */"));
		assertTrue((boolean) eval("true // AND false"));
		assertTrue((boolean) eval("false // AND false\nOR true"));
	}

	@Test
	public void invalidExpressionThrowsException() {
		assertThatThrownBy(() -> eval("+"))
			.isInstanceOf(ExpressionException.class)
			.hasMessageContaining("Expression parse failed at");
	}
}
