package vexprtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.testng.annotations.Test;
import vexpressed.core.ExpressionCalculatorVisitor;
import vexpressed.core.ExpressionException;

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
	public void unarySign() {
		assertEquals(eval("-(-5)"), 5);
		// next case colides with custom operators
		assertThatThrownBy(() -> eval("--5)"))
			.isInstanceOf(ExpressionException.class)
			.hasMessageMatching("(?s)Expression parse failed at 1:0.*'--'.*");
		assertThatThrownBy(() -> eval("-'nono'"))
			.isInstanceOf(ExpressionException.class);
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
	public void arithmeticOperations() {
		assertEquals(eval("5. / 2"), new BigDecimal("2.5"));
		assertEquals(eval("1"), 1);
		assertEquals(eval("5 + 1"), 6);
		// any non-integer promotes the whole result to BigDecimal, also notice how scale is set
		assertEquals(eval("5 + 1."), new BigDecimal("6"));
		assertEquals(eval("5 + 1.0"), new BigDecimal("6.0"));
		assertEquals(eval("1 - 1.050"), new BigDecimal("-0.050"));
		assertEquals(eval("5 - 6"), -1);
		// integer division
		assertEquals(eval("5 / 2"), 2);
		assertEquals(eval("5 % 2"), 1);
		// floating point division
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
	public void arithmeticScale() {
		assertEquals(eval("1 / 3"), 0); // integer result
		assertEquals(((BigDecimal) eval("1 / 3.")).scale(),
			ExpressionCalculatorVisitor.DEFAULT_MAX_RESULT_SCALE);
		assertEquals(eval("1 / 4."), new BigDecimal("0.25")); // trailing zeroes are always trimmed
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

	@Test
	public void nullValueForArithmeticThrowsException() {
		assertThatThrownBy(() -> eval("null + 5"))
			.isInstanceOf(ExpressionException.class)
			.hasMessageContaining("Null value not allowed here");
	}
}
