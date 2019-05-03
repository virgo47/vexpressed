package com.virgo47.vexpressed.test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.virgo47.vexpressed.core.ExpressionException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ExpressionBasicTest extends TestBase {

	@Test
	public void primitiveVariableResolverReturnsTheSameValueForAnyVarName() {
		variableResolver = var -> 5;
		assertEquals(evalAsObject("var"), 5);
		assertEquals(evalAsObject("anyvarworksnow"), 5);
	}

	@Test
	public void variableResolverReturnsValueForOneVarName() {
		variableResolver = var -> var.equals("var") ? 5 : null;
		assertEquals(evalAsObject("var"), 5);
		assertEquals(evalAsObject("var != null"), true);
		assertEquals(evalAsObject("var == null"), false);

		assertNull(eval("anyvarworksnow"));
		assertTrue(eval("anyvarworksnow == null"));
	}

	@Test
	public void integerLiteralStaysInteger() {
		assertEquals(evalAsObject("5"), 5);
	}

	@Test
	public void tooBigIntegerLiteralConvertedToBigDecimal() {
		assertEquals(eval("5555555555555"), new BigDecimal("5555555555555.0"));
	}

	@Test
	public void fpNumberCanStartWithPoint() {
		assertEquals(eval(".047"), new BigDecimal("0.047"));
	}

	@Test
	public void fpNumberCanContainExponent() {
		assertEquals(eval("1.47E5"), new BigDecimal("147000.0"));
	}

	@Test
	public void fpNumberCanContainExplicitlyPositiveExponent() {
		assertEquals(eval(".47E+3"), new BigDecimal("470.0"));
	}

	@Test
	public void fpNumberCanContainNegativeExponent() {
		assertEquals(eval("1.47E-1"), new BigDecimal("0.147"));
	}

	@Test
	public void fpNumberCanEndWithPoint() {
		assertEquals(eval("3."), new BigDecimal("3.0"));
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
	public void numbersCanContainUnderscores() {
		assertEquals(evalAsObject("5_0"), 50);
		assertEquals(evalAsObject("5_"), 5);
		assertEquals(eval("5_.1_"), new BigDecimal("5.1"));
		assertEquals(eval("5_.1_E1_"), new BigDecimal("51.0"));
		// note that _5 is valid variable name, so is _5.5 (dot is part of valid ID)
		// _5E+1 would resolve _5E variable and add it to 1
	}

	@Test
	public void booleanVariableStaysBoolean() {
		variableResolver = var -> true;
		assertTrue(eval("var"));
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
	public void stringLiteralCanContainComments() {
		assertEquals(eval("'#/*does nothing*/'"), "#/*does nothing*/");
	}

	@Test
	public void stringConcatenation() {
		assertEquals(eval("'str' + 'ing'"), "string");
		assertEquals(eval("'str' + 47"), "str47");
	}

	@Test
	public void stringCompare() {
		variableResolver = var -> "str'val";
		assertTrue(eval("var == 'str''val'"));
	}

	@Test
	public void booleanLiterals() {
		assertTrue(eval("true"));
		assertFalse(eval("false"));
	}

	@Test
	public void booleanNot() {
		assertEquals(evalAsObject("!true"), false);
		assertEquals(evalAsObject("NOT TRUE"), false);
		assertEquals(evalAsObject("!false"), true);
		assertEquals(evalAsObject("not false"), true);
		assertEquals(evalAsObject("!not true"), true);
		assertEquals(evalAsObject("not!true"), true);
		assertEquals(evalAsObject("! !true"), true);
	}

	@Test
	public void booleanAnd() {
		assertEquals(evalAsObject("true && true"), true);
		// keyword is case-insensitive
		assertEquals(evalAsObject("TRUE && false"), false);
		assertEquals(evalAsObject("FALSE && true"), false);
		assertEquals(evalAsObject("false && false"), false);
	}

	@Test
	public void booleanOr() {
		assertEquals(evalAsObject("true || true"), true);
		assertEquals(evalAsObject("true || false"), true);
		assertEquals(evalAsObject("false || true"), true);
		assertEquals(evalAsObject("false || false"), false);
		assertEquals(evalAsObject("true OR true"), true);
		assertEquals(evalAsObject("true OR false"), true);
		assertEquals(evalAsObject("false OR true"), true);
		assertEquals(evalAsObject("false OR false"), false);
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
		assertEquals(evalAsObject(x + " || " + y + " && " + z),
			evalAsObject(x + " || (" + y + " && " + z + ')'));
		assertEquals(evalAsObject(x + " && " + y + " || " + z),
			evalAsObject("(" + x + " && " + y + ") || " + z));
	}

	@Test
	public void booleanComparison() {
		assertEquals(evalAsObject("true EQ true"), true);
		assertEquals(evalAsObject("true == false"), false);
		assertEquals(evalAsObject("true > false"), true);
		assertEquals(evalAsObject("true >= false"), true);
		assertEquals(evalAsObject("true <= false"), false);
		assertEquals(evalAsObject("true < false"), false);
	}

	@Test
	public void numberComparison() {
		assertEquals(evalAsObject("5 > 1"), true);
		assertEquals(evalAsObject("1 > 5"), false);
		assertEquals(evalAsObject("5 > 5"), false);
		assertEquals(evalAsObject("5 < 1"), false);
		assertEquals(evalAsObject("1 < 5"), true);
		assertEquals(evalAsObject("5 < 5"), false);
		assertEquals(evalAsObject("+5 < -7"), false);
		assertEquals(evalAsObject("-5 < -(3)"), true);
		assertEquals(evalAsObject("5 == 1"), false);
		assertEquals(evalAsObject("5 == 5"), true);
		assertEquals(evalAsObject("5 != 5"), false);
		assertEquals(evalAsObject("5 != 3"), true);
		// mixing BigDecimal and Integer on either side
		assertEquals(evalAsObject("5. == 5"), true);
		assertEquals(evalAsObject("5 < 5.1"), true);
	}

	@Test
	public void dateComparison() {
		variableResolver = val -> val.equals("a")
			? LocalDate.of(2016, 2, 29) : LocalDate.of(2016, 3, 1);
		assertEquals(evalAsObject("a > b"), false);
		assertEquals(evalAsObject("a >= b"), false);
		assertEquals(evalAsObject("a < b"), true);
		assertEquals(evalAsObject("a <= b"), true);
		assertEquals(evalAsObject("a == b"), false);
		assertEquals(evalAsObject("a != b"), true);
		assertEquals(evalAsObject("a == a"), true);
		assertEquals(evalAsObject("a != a"), false);
		assertEquals(evalAsObject("a >= a"), true);
		assertEquals(evalAsObject("a <= a"), true);
		assertEquals(evalAsObject("a < a"), false);
		assertEquals(evalAsObject("a > a"), false);

		// mixing types is not supported
		variableResolver = val -> val.equals("a")
			? LocalDate.of(2016, 3, 1) : LocalDateTime.of(2016, 3, 1, 12, 47);
		assertThatThrownBy(() -> evalAsObject("a > b"))
			.isInstanceOf(ClassCastException.class);
	}

	@Test
	public void nullComparison() {
		assertEquals(evalAsObject("null == null"), true);
		assertEquals(evalAsObject("null != null"), false);
		assertEquals(evalAsObject("5 != null"), true);
		assertEquals(evalAsObject("5 == null"), false);
		assertEquals(evalAsObject("null != 5"), true);
		assertEquals(evalAsObject("null == 5"), false);
		assertEquals(evalAsObject("null > null"), false);
		assertEquals(evalAsObject("null < null"), false);
		assertEquals(evalAsObject("null <= null"), false);
		assertEquals(evalAsObject("null >= null"), false);
	}

	@Test
	public void arithmeticOnDates() {
		variableResolver = val -> LocalDate.of(2016, 2, 29);
		assertEquals(evalAsObject("val + 1"), LocalDate.of(2016, 3, 1));
		assertEquals(evalAsObject("val + '1d'"), LocalDate.of(2016, 3, 1));
		assertEquals(evalAsObject("val + '1w'"), LocalDate.of(2016, 3, 7));
		assertEquals(evalAsObject("val + '1m'"), LocalDate.of(2016, 3, 29));
		assertEquals(evalAsObject("val + '1y'"), LocalDate.of(2017, 2, 28));
		variableResolver = val -> LocalDate.of(2016, 3, 1);
		assertEquals(evalAsObject("val - 1"), LocalDate.of(2016, 2, 29));
		assertEquals(evalAsObject("val - '1d'"), LocalDate.of(2016, 2, 29));
		assertEquals(evalAsObject("val - '1w'"), LocalDate.of(2016, 2, 23));
		assertEquals(evalAsObject("val - '1m'"), LocalDate.of(2016, 2, 1));
		assertEquals(evalAsObject("val - '1y'"), LocalDate.of(2015, 3, 1));

		// combined specification is not supported directly
		assertThatThrownBy(() -> evalAsObject("val + '2w3d'"))
			.isInstanceOf(ExpressionException.class)
			.hasMessage("Cannot parse temporal amount: 2w3d");
		// only split to more strings
		assertEquals(evalAsObject("val + '2w' + '3d'"), LocalDate.of(2016, 3, 18));

		variableResolver = val -> LocalDateTime.of(2016, 3, 1, 12, 47);
		assertEquals(evalAsObject("val + 1"), LocalDateTime.of(2016, 3, 2, 12, 47));
		variableResolver = val -> Instant.ofEpochMilli(1000000);
		assertEquals(evalAsObject("val + 1"), Instant.ofEpochMilli(1000000 + TimeUnit.DAYS.toMillis(1)));
	}

	@Test
	public void commentsAreIgnoredProperly() {
		assertFalse((boolean) evalAsObject("true AND false"));
		assertFalse((boolean) evalAsObject("/*true AND*/ false"));
		assertFalse((boolean) evalAsObject("/*true \nAND*/ false"));
		assertTrue((boolean) evalAsObject("true /* AND \nfalse */"));
		assertTrue((boolean) evalAsObject("true # AND false"));
		assertTrue((boolean) evalAsObject("false # AND false\nOR true"));
	}

	@Test
	public void invalidExpressionThrowsException() {
		assertThatThrownBy(() -> evalAsObject("+"))
			.isInstanceOf(ExpressionException.class)
			.hasMessageContaining("Expression parse failed at");
	}
}
