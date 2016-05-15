package vexprtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.antlr.v4.runtime.tree.ParseTree;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vexpressed.core.ExpressionCalculatorVisitor;
import vexpressed.core.ExpressionException;
import vexpressed.VexpressedUtils;
import vexpressed.core.VariableResolver;

public class ExpressionBasicTest {

	private VariableResolver variableResolver;

	@BeforeMethod
	public void init() {
		variableResolver = var -> null;
	}

	@Test
	public void primitiveVariableResolverReturnsTheSameValueForAnyVarName() {
		variableResolver = var -> 5;
		assertEquals(expr("var"), 5);
		assertEquals(expr("anyvarworksnow"), 5);
	}

	@Test
	public void variableResolverReturnsValueForOneVarName() {
		variableResolver = var -> var.equals("var") ? 5 : null;
		assertEquals(expr("var"), 5);
		assertEquals(expr("var != null"), true);
		assertEquals(expr("var == null"), false);
		assertEquals(expr("var is not null"), true);

		assertEquals(expr("anyvarworksnow"), null);
		assertEquals(expr("anyvarworksnow == null"), true);
		assertEquals(expr("anyvarworksnow is null"), true);
	}

	@Test
	public void isNullComparison() {
		assertTrue((Boolean) expr("null is null"));
		assertFalse((Boolean) expr("null is not null"));
		assertFalse((Boolean) expr("'' is null"));
		assertTrue((Boolean) expr("'' is not null"));
	}

	@Test
	public void integerLiteralStaysInteger() {
		assertEquals(expr("5"), 5);
	}

	@Test
	public void tooBigIntegerLiteralConvertedToBigDecimal() {
		assertEquals(expr("5555555555555"), new BigDecimal("5555555555555"));
	}

	@Test
	public void fpNumberCanStartWithPoint() {
		assertEquals(expr(".047"), new BigDecimal("0.047"));
	}

	@Test
	public void fpNumberCanContainExponent() {
		assertEquals(expr("1.47E5"), new BigDecimal("147000"));
	}

	@Test
	public void fpNumberCanContainExplicitlyPositiveExponent() {
		assertEquals(expr(".47E+3"), new BigDecimal("470"));
	}

	@Test
	public void fpNumberCanContainNegativeExponent() {
		assertEquals(expr("1.47E-1"), new BigDecimal("0.147"));
	}

	@Test
	public void fpNumberCanEndWithPoint() {
		assertEquals(expr("3."), new BigDecimal("3"));
	}

	@Test
	public void shortVariableIsConvertedToInteger() {
		variableResolver = var -> (short) 5;
		assertEquals(expr("var").getClass(), Integer.class);
	}

	@Test
	public void smallLongIsConvertedToInteger() {
		variableResolver = var -> 5L;
		assertEquals(expr("var").getClass(), Integer.class);
	}

	@Test
	public void valueOutOfIntegerRangeIsConvertedToBigDecimal() {
		variableResolver = var -> 5555555555555L;
		assertEquals(expr("var").getClass(), BigDecimal.class);
	}

	@Test
	public void fpValueConvertedToBigDecimal() {
		variableResolver = var -> 5.1;
		assertEquals(expr("var"), new BigDecimal("5.1"));
	}

	@Test
	public void booleanVariableStaysBoolean() {
		variableResolver = var -> true;
		assertEquals(expr("var"), true);
	}

	@Test
	public void stringVariableStaysString() {
		variableResolver = var -> "str'val";
		assertEquals(expr("var"), "str'val");
	}

	@Test
	public void stringLiteralEscapedQuoteInterpretedProperly() {
		assertEquals(expr("'str''val'"), "str'val");
	}

	@Test
	public void stringConcatenation() {
		assertEquals(expr("'str' + 'ing'"), "string");
		assertEquals(expr("'str' + 47"), "str47");
	}

	@Test
	public void stringCompare() {
		variableResolver = var -> "str'val";
		assertEquals(expr("var == 'str''val'"), true);
	}

	@Test
	public void booleanLiterals() {
		assertEquals(expr("true"), true);
		assertEquals(expr("false"), false);
		assertEquals(expr("T"), true);
		assertEquals(expr("F"), false);
	}

	@Test
	public void booleanAnd() {
		assertEquals(expr("true && true"), true);
		assertEquals(expr("true && false"), false);
		assertEquals(expr("false && true"), false);
		assertEquals(expr("false && false"), false);
		assertEquals(expr("true AND T"), true);
		assertEquals(expr("true AND F"), false);
		// keyword is case-insensitive
		assertEquals(expr("false and T"), false);
		assertEquals(expr("false and F"), false);
	}

	@Test
	public void booleanOr() {
		assertEquals(expr("true || true"), true);
		assertEquals(expr("true || false"), true);
		assertEquals(expr("false || true"), true);
		assertEquals(expr("false || false"), false);
		assertEquals(expr("true OR true"), true);
		assertEquals(expr("true OR false"), true);
		assertEquals(expr("false OR true"), true);
		assertEquals(expr("false OR false"), false);
	}

	@Test
	public void booleanComparison() {
		assertEquals(expr("true EQ true"), true);
		assertEquals(expr("true == false"), false);
		assertEquals(expr("true > false"), true);
		assertEquals(expr("true >= false"), true);
		assertEquals(expr("true <= false"), false);
		assertEquals(expr("true < false"), false);
	}

	@Test
	public void numberComparison() {
		assertEquals(expr("5 > 1"), true);
		assertEquals(expr("1 > 5"), false);
		assertEquals(expr("5 > 5"), false);
		assertEquals(expr("5 < 1"), false);
		assertEquals(expr("1 < 5"), true);
		assertEquals(expr("5 < 5"), false);
		assertEquals(expr("+5 < -7"), false);
		assertEquals(expr("-5 < -(3)"), true);
		assertEquals(expr("5 == 1"), false);
		assertEquals(expr("5 == 5"), true);
		assertEquals(expr("5 != 5"), false);
		assertEquals(expr("5 != 3"), true);
		// mixing BigDecimal and Integer on either side
		assertEquals(expr("5. == 5"), true);
		assertEquals(expr("5 < 5.1"), true);
	}

	@Test
	public void unarySign() {
		assertEquals(expr("-(-5)"), 5);
		// next case colides with custom operators
		assertThatThrownBy(() -> expr("--5)"))
			.isInstanceOf(ExpressionException.class)
			.hasMessageMatching("(?s)Expression parse failed at 1:0.*'--'.*");
		assertThatThrownBy(() -> expr("-'nono'"))
			.isInstanceOf(ExpressionException.class);
	}

	@Test
	public void nullComparison() {
		assertEquals(expr("null == null"), true);
		assertEquals(expr("null != null"), false);
		assertEquals(expr("5 != null"), true);
		assertEquals(expr("5 == null"), false);
		assertEquals(expr("null != 5"), true);
		assertEquals(expr("null == 5"), false);
		assertEquals(expr("null > null"), false);
		assertEquals(expr("null < null"), false);
		assertEquals(expr("null <= null"), false);
		assertEquals(expr("null >= null"), false);
	}

	@Test
	public void arithmeticOperations() {
		assertEquals(expr("5. / 2"), new BigDecimal("2.5"));
		assertEquals(expr("1"), 1);
		assertEquals(expr("5 + 1"), 6);
		// any non-integer promotes the whole result to BigDecimal, also notice how scale is set
		assertEquals(expr("5 + 1."), new BigDecimal("6"));
		assertEquals(expr("5 + 1.0"), new BigDecimal("6.0"));
		assertEquals(expr("1 - 1.050"), new BigDecimal("-0.050"));
		assertEquals(expr("5 - 6"), -1);
		// integer division
		assertEquals(expr("5 / 2"), 2);
		assertEquals(expr("5 % 2"), 1);
		// floating point division
	}

	@Test
	public void arithmeticOnDates() {
		variableResolver = val -> LocalDate.of(2016, 2, 29);
		assertEquals(expr("val + 1"), LocalDate.of(2016, 3, 1));
		assertEquals(expr("val + '1d'"), LocalDate.of(2016, 3, 1));
		assertEquals(expr("val + '1m'"), LocalDate.of(2016, 3, 29));
		assertEquals(expr("val + '1y'"), LocalDate.of(2017, 2, 28));
		variableResolver = val -> LocalDate.of(2016, 3, 1);
		assertEquals(expr("val - 1"), LocalDate.of(2016, 2, 29));
		assertEquals(expr("val - '1d'"), LocalDate.of(2016, 2, 29));
		assertEquals(expr("val - '1m'"), LocalDate.of(2016, 2, 1));
		assertEquals(expr("val - '1y'"), LocalDate.of(2015, 3, 1));
	}

	@Test
	public void arithmeticScale() {
		assertEquals(expr("1 / 3"), 0); // integer result
		assertEquals(((BigDecimal) expr("1 / 3.")).scale(),
			ExpressionCalculatorVisitor.DEFAULT_MAX_RESULT_SCALE);
		assertEquals(expr("1 / 4."), new BigDecimal("0.25")); // trailing zeroes are always trimmed
	}

	@Test
	public void commentsAreIgnoredProperly() {
		assertFalse((boolean) expr("true AND false"));
		assertFalse((boolean) expr("/*true AND*/ false"));
		assertFalse((boolean) expr("/*true \nAND*/ false"));
		assertTrue((boolean) expr("true /* AND \nfalse */"));
		assertTrue((boolean) expr("true // AND false"));
		assertTrue((boolean) expr("false // AND false\nOR true"));
	}

	@Test
	public void invalidExpressionThrowsException() {
		assertThatThrownBy(() -> expr("+"))
			.isInstanceOf(ExpressionException.class)
			.hasMessageContaining("Expression parse failed at");
	}

	@Test
	public void nullValueForArithmeticThrowsException() {
		assertThatThrownBy(() -> expr("null + 5"))
			.isInstanceOf(ExpressionException.class)
			.hasMessageContaining("Null value not allowed here");
	}

	private Object expr(String expression) {
		ParseTree parseTree = VexpressedUtils.createParseTree(expression);
		return new ExpressionCalculatorVisitor(variableResolver)
			.visit(parseTree);
	}
}
