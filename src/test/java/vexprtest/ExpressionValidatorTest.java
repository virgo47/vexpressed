package vexprtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertEquals;
import static vexpressed.meta.ExpressionType.BOOLEAN;
import static vexpressed.meta.ExpressionType.DATE;
import static vexpressed.meta.ExpressionType.DECIMAL;
import static vexpressed.meta.ExpressionType.INTEGER;
import static vexpressed.meta.ExpressionType.OBJECT;
import static vexpressed.meta.ExpressionType.STRING;

import vexpressed.core.ExpressionException;
import vexpressed.support.FunctionMapper;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ExpressionValidatorTest extends TestBase {

	@BeforeMethod
	public void init() {
		variableTypeResolver = var -> null;
		functionTypeResolver = null;
	}

	@Test
	public void variableTypeIsValidated() {
		variableTypeResolver = var -> OBJECT;
		assertEquals(check("var"), OBJECT);
		variableTypeResolver = var -> STRING;
		assertEquals(check("nameIsNotImportant"), STRING);
	}

	@Test
	public void variableResolverReturnsValueForOneVarName() {
		variableTypeResolver = var -> var.equals("var") ? STRING : OBJECT;
		assertEquals(check("var"), STRING);
		assertEquals(check("otherVar"), OBJECT);
		assertEquals(check("var != null"), BOOLEAN);
		assertEquals(check("var == null"), BOOLEAN);
	}

	@Test
	public void integerLiteralStaysInteger() {
		assertEquals(check("5"), INTEGER);
	}

	@Test
	public void tooBigIntegerLiteralConvertedToBigDecimal() {
		assertEquals(check("5555555555555"), DECIMAL);
	}

	@Test
	public void fpNumberCanStartWithPoint() {
		assertEquals(check(".047"), DECIMAL);
	}

	@Test
	public void fpNumberCanContainExponent() {
		assertEquals(check("1.47E5"), DECIMAL);
	}

	@Test
	public void fpNumberCanContainExplicitlyPositiveExponent() {
		assertEquals(check(".47E+3"), DECIMAL);
	}

	@Test
	public void fpNumberCanContainNegativeExponent() {
		assertEquals(check("1.47E-1"), DECIMAL);
	}

	@Test
	public void fpNumberCanEndWithPoint() {
		assertEquals(check("3."), DECIMAL);
	}

	@Test
	public void stringConcatenation() {
		assertEquals(check("'str' + 'ing'"), STRING);
	}

	@Test
	public void booleanLiterals() {
		assertEquals(check("true"), BOOLEAN);
		assertEquals(check("false"), BOOLEAN);
	}

	@Test
	public void booleanAnd() {
		assertEquals(check("true && true"), BOOLEAN);
		assertEquals(check("true && false"), BOOLEAN);
		assertEquals(check("false && true"), BOOLEAN);
		assertEquals(check("false && false"), BOOLEAN);
	}

	@Test
	public void booleanOr() {
		assertEquals(check("true || true"), BOOLEAN);
		assertEquals(check("true || false"), BOOLEAN);
		assertEquals(check("false || true"), BOOLEAN);
		assertEquals(check("false || false"), BOOLEAN);
		assertEquals(check("true OR true"), BOOLEAN);
		assertEquals(check("true OR false"), BOOLEAN);
		assertEquals(check("false OR true"), BOOLEAN);
		assertEquals(check("false OR false"), BOOLEAN);
	}

	@Test
	public void booleanComparison() {
		assertEquals(check("true EQ true"), BOOLEAN);
		assertEquals(check("true == false"), BOOLEAN);
		assertEquals(check("true > false"), BOOLEAN);
		assertEquals(check("true >= false"), BOOLEAN);
		assertEquals(check("true <= false"), BOOLEAN);
		assertEquals(check("true < false"), BOOLEAN);
	}

	@Test
	public void numberComparison() {
		assertEquals(check("5 > 1"), BOOLEAN);
		assertEquals(check("1 > 5"), BOOLEAN);
		assertEquals(check("5 > 5"), BOOLEAN);
		assertEquals(check("5 < 1"), BOOLEAN);
		assertEquals(check("1 < 5"), BOOLEAN);
		assertEquals(check("5 < 5"), BOOLEAN);
		assertEquals(check("+5 < -7"), BOOLEAN);
		assertEquals(check("-5 < -(3)"), BOOLEAN);
		assertEquals(check("5 == 1"), BOOLEAN);
		assertEquals(check("5 == 5"), BOOLEAN);
		assertEquals(check("5 != 5"), BOOLEAN);
		assertEquals(check("5 != 3"), BOOLEAN);
		// mixing BigDecimal and Integer on either side
		assertEquals(check("5. == 5"), BOOLEAN);
		assertEquals(check("5 < 5.1"), BOOLEAN);
	}

	@Test
	public void invalidComparisons() {
		assertThatThrownBy(() -> check("5 > ''"))
			.isInstanceOf(ExpressionException.class)
			.hasMessage("Invalid comparison/relation operation between type INTEGER and STRING");
		assertThatThrownBy(() -> check("1 < true"))
			.isInstanceOf(ExpressionException.class)
			.hasMessage("Invalid comparison/relation operation between type INTEGER and BOOLEAN");
	}

	@Test
	public void unarySign() {
		assertEquals(check("-(-5)"), INTEGER);
		assertThatThrownBy(() -> check("-'nono'"))
			.isInstanceOf(ExpressionException.class);
	}

	@Test
	public void nullComparison() {
		assertEquals(check("null == null"), BOOLEAN);
		assertEquals(check("null != null"), BOOLEAN);
		assertEquals(check("5 != null"), BOOLEAN);
		assertEquals(check("5 == null"), BOOLEAN);
		assertEquals(check("null != 5"), BOOLEAN);
		assertEquals(check("null == 5"), BOOLEAN);
		assertEquals(check("null > null"), BOOLEAN);
		assertEquals(check("null < null"), BOOLEAN);
		assertEquals(check("null <= null"), BOOLEAN);
		assertEquals(check("null >= null"), BOOLEAN);
	}

	@Test
	public void arithmeticOperations() {
		assertEquals(check("5. / 2"), DECIMAL);
		assertEquals(check("1"), INTEGER);
		assertEquals(check("5 + 1"), INTEGER);
		// any non-integer promotes the whole result to BigDecimal, also notice how scale is set
		assertEquals(check("5 + 1."), DECIMAL);
		assertEquals(check("5 + 1.0"), DECIMAL);
		assertEquals(check("1 - 1.050"), DECIMAL);
		assertEquals(check("5 - 6"), INTEGER);
		// integer division
		assertEquals(check("5 / 2"), INTEGER);
		assertEquals(check("5 % 2"), INTEGER);
		assertThatThrownBy(() -> check("5 + true"))
			.isInstanceOf(ExpressionException.class);
		assertThatThrownBy(() -> check("true + 5"))
			.isInstanceOf(ExpressionException.class);
		assertThatThrownBy(() -> check("5 + 'str'"))
			.isInstanceOf(ExpressionException.class);
	}

	@Test
	public void arithmeticOnDates() {
		variableTypeResolver = val -> DATE;
		assertEquals(check("val + 1"), DATE);
		assertEquals(check("val + '1y'"), DATE);
		assertEquals(check("val - 1"), DATE);
		assertEquals(check("val - '1d'"), DATE);
	}

	@Test
	public void nullValueForArithmeticThrowsException() {
		assertThatThrownBy(() -> check("null + 5"))
			.isInstanceOf(ExpressionException.class)
			.hasMessageContaining("Null value not allowed here");
	}

	@Test
	public void binaryFunctionWithInfixNotationReturnsString() {
		functionTypeResolver = new FunctionMapper()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class);
		assertEquals(check("'a' func 'b'"), STRING);
	}

	@Test
	public void randFunctionReturnsDecimal() {
		functionTypeResolver = new FunctionMapper()
			.scanForFunctions(new TestFunctions());
		assertEquals(check("rand(10)"), DECIMAL);
	}

	@Test
	public void combinedFunctionsForArithmetic() {
		functionTypeResolver = new FunctionMapper()
			.scanForFunctions(new TestFunctions())
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class);
		assertEquals(check("func('','') + rand(5)"), STRING);
		assertThatThrownBy(() -> check("rand(5) + func('','')"))
			.isInstanceOf(ExpressionException.class)
			.hasMessage("Arithmetic operation + not supported for types DECIMAL and STRING");
	}
}
