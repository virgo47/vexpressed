package vexprtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertEquals;
import static vexpressed.ExpressionType.BOOLEAN;
import static vexpressed.ExpressionType.DATE;
import static vexpressed.ExpressionType.DECIMAL;
import static vexpressed.ExpressionType.INTEGER;
import static vexpressed.ExpressionType.OBJECT;
import static vexpressed.ExpressionType.STRING;

import org.antlr.v4.runtime.tree.ParseTree;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vexpressed.ExpressionException;
import vexpressed.ExpressionType;
import vexpressed.ExpressionUtils;
import vexpressed.ExpressionValidatorVisitor;
import vexpressed.func.DelegateFunctionExecutor;
import vexpressed.func.ExpressionFunctionTypeResolver;
import vexpressed.vars.ExpressionVariableTypeResolver;

public class ExpressionValidatorTest {

	private ExpressionVariableTypeResolver variableTypeResolver;
	private ExpressionFunctionTypeResolver functionTypeResolver;

	@BeforeMethod
	public void init() {
		variableTypeResolver = var -> null;
		functionTypeResolver = null;
	}

	@Test
	public void variableTypeIsValidated() {
		variableTypeResolver = var -> OBJECT;
		assertEquals(expr("var"), OBJECT);
		variableTypeResolver = var -> STRING;
		assertEquals(expr("nameIsNotImportant"), STRING);
	}

	@Test
	public void variableResolverReturnsValueForOneVarName() {
		variableTypeResolver = var -> var.equals("var") ? STRING : OBJECT;
		assertEquals(expr("var"), STRING);
		assertEquals(expr("otherVar"), OBJECT);
		assertEquals(expr("var != null"), BOOLEAN);
		assertEquals(expr("var == null"), BOOLEAN);
		assertEquals(expr("var is null"), BOOLEAN);
		assertEquals(expr("var is not null"), BOOLEAN);
	}

	@Test
	public void isNullComparison() {
		assertEquals(expr("null is null"), BOOLEAN);
		assertEquals(expr("null is not null"), BOOLEAN);
		assertEquals(expr("'' is null"), BOOLEAN);
		assertEquals(expr("'' is not null"), BOOLEAN);
	}

	@Test
	public void integerLiteralStaysInteger() {
		assertEquals(expr("5"), INTEGER);
	}

	@Test
	public void tooBigIntegerLiteralConvertedToBigDecimal() {
		assertEquals(expr("5555555555555"), DECIMAL);
	}

	@Test
	public void fpNumberCanStartWithPoint() {
		assertEquals(expr(".047"), DECIMAL);
	}

	@Test
	public void fpNumberCanContainExponent() {
		assertEquals(expr("1.47E5"), DECIMAL);
	}

	@Test
	public void fpNumberCanContainExplicitlyPositiveExponent() {
		assertEquals(expr(".47E+3"), DECIMAL);
	}

	@Test
	public void fpNumberCanContainNegativeExponent() {
		assertEquals(expr("1.47E-1"), DECIMAL);
	}

	@Test
	public void fpNumberCanEndWithPoint() {
		assertEquals(expr("3."), DECIMAL);
	}

	@Test
	public void stringConcatenation() {
		assertEquals(expr("'str' + 'ing'"), STRING);
	}

	@Test
	public void booleanLiterals() {
		assertEquals(expr("true"), BOOLEAN);
		assertEquals(expr("false"), BOOLEAN);
		assertEquals(expr("T"), BOOLEAN);
		assertEquals(expr("F"), BOOLEAN);
	}

	@Test
	public void booleanAnd() {
		assertEquals(expr("true && true"), BOOLEAN);
		assertEquals(expr("true && false"), BOOLEAN);
		assertEquals(expr("false && true"), BOOLEAN);
		assertEquals(expr("false && false"), BOOLEAN);
		assertEquals(expr("true AND T"), BOOLEAN);
		assertEquals(expr("true AND F"), BOOLEAN);
		// keyword is case-insensitive
		assertEquals(expr("false and T"), BOOLEAN);
		assertEquals(expr("false and F"), BOOLEAN);
	}

	@Test
	public void booleanOr() {
		assertEquals(expr("true || true"), BOOLEAN);
		assertEquals(expr("true || false"), BOOLEAN);
		assertEquals(expr("false || true"), BOOLEAN);
		assertEquals(expr("false || false"), BOOLEAN);
		assertEquals(expr("true OR true"), BOOLEAN);
		assertEquals(expr("true OR false"), BOOLEAN);
		assertEquals(expr("false OR true"), BOOLEAN);
		assertEquals(expr("false OR false"), BOOLEAN);
	}

	@Test
	public void booleanComparison() {
		assertEquals(expr("true EQ true"), BOOLEAN);
		assertEquals(expr("true == false"), BOOLEAN);
		assertEquals(expr("true > false"), BOOLEAN);
		assertEquals(expr("true >= false"), BOOLEAN);
		assertEquals(expr("true <= false"), BOOLEAN);
		assertEquals(expr("true < false"), BOOLEAN);
	}

	@Test
	public void numberComparison() {
		assertEquals(expr("5 > 1"), BOOLEAN);
		assertEquals(expr("1 > 5"), BOOLEAN);
		assertEquals(expr("5 > 5"), BOOLEAN);
		assertEquals(expr("5 < 1"), BOOLEAN);
		assertEquals(expr("1 < 5"), BOOLEAN);
		assertEquals(expr("5 < 5"), BOOLEAN);
		assertEquals(expr("+5 < -7"), BOOLEAN);
		assertEquals(expr("-5 < -(3)"), BOOLEAN);
		assertEquals(expr("5 == 1"), BOOLEAN);
		assertEquals(expr("5 == 5"), BOOLEAN);
		assertEquals(expr("5 != 5"), BOOLEAN);
		assertEquals(expr("5 != 3"), BOOLEAN);
		// mixing BigDecimal and Integer on either side
		assertEquals(expr("5. == 5"), BOOLEAN);
		assertEquals(expr("5 < 5.1"), BOOLEAN);
	}

	@Test
	public void invalidComparisons() {
		assertThatThrownBy(() -> expr("5 > ''"))
			.isInstanceOf(ExpressionException.class)
			.hasMessage("Invalid comparison/relation operation between type INTEGER and STRING");
		assertThatThrownBy(() -> expr("1 < true"))
			.isInstanceOf(ExpressionException.class)
		.hasMessage("Invalid comparison/relation operation between type INTEGER and BOOLEAN");
	}

	@Test
	public void unarySign() {
		assertEquals(expr("-(-5)"), INTEGER);
		assertThatThrownBy(() -> expr("-'nono'"))
			.isInstanceOf(ExpressionException.class);
	}

	@Test
	public void nullComparison() {
		assertEquals(expr("null == null"), BOOLEAN);
		assertEquals(expr("null != null"), BOOLEAN);
		assertEquals(expr("5 != null"), BOOLEAN);
		assertEquals(expr("5 == null"), BOOLEAN);
		assertEquals(expr("null != 5"), BOOLEAN);
		assertEquals(expr("null == 5"), BOOLEAN);
		assertEquals(expr("null > null"), BOOLEAN);
		assertEquals(expr("null < null"), BOOLEAN);
		assertEquals(expr("null <= null"), BOOLEAN);
		assertEquals(expr("null >= null"), BOOLEAN);
	}

	@Test
	public void arithmeticOperations() {
		assertEquals(expr("5. / 2"), DECIMAL);
		assertEquals(expr("1"), INTEGER);
		assertEquals(expr("5 + 1"), INTEGER);
		// any non-integer promotes the whole result to BigDecimal, also notice how scale is set
		assertEquals(expr("5 + 1."), DECIMAL);
		assertEquals(expr("5 + 1.0"), DECIMAL);
		assertEquals(expr("1 - 1.050"), DECIMAL);
		assertEquals(expr("5 - 6"), INTEGER);
		// integer division
		assertEquals(expr("5 / 2"), INTEGER);
		assertEquals(expr("5 % 2"), INTEGER);
		assertThatThrownBy(() -> expr("5 + true"))
			.isInstanceOf(ExpressionException.class);
		assertThatThrownBy(() -> expr("true + 5"))
			.isInstanceOf(ExpressionException.class);
		assertThatThrownBy(() -> expr("5 + 'str'"))
			.isInstanceOf(ExpressionException.class);
	}

	@Test
	public void arithmeticOnDates() {
		variableTypeResolver = val -> DATE;
		assertEquals(expr("val + 1"), DATE);
		assertEquals(expr("val + '1y'"), DATE);
		assertEquals(expr("val - 1"), DATE);
		assertEquals(expr("val - '1d'"), DATE);
	}

	@Test
	public void nullValueForArithmeticThrowsException() {
		assertThatThrownBy(() -> expr("null + 5"))
			.isInstanceOf(ExpressionException.class)
			.hasMessageContaining("Null value not allowed here");
	}

	@Test
	public void binaryFunctionWithInfixNotationReturnsString() {
		functionTypeResolver = new DelegateFunctionExecutor()
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class);
		assertEquals(expr("'a' func 'b'"), STRING);
	}

	@Test
	public void randFunctionReturnsDecimal() {
		functionTypeResolver = new DelegateFunctionExecutor()
			.scanForFunctions(new TestFunctions());
		assertEquals(expr("rand(10)"), DECIMAL);
	}

	@Test
	public void combinedFunctionsForArithmetic() {
		functionTypeResolver = new DelegateFunctionExecutor()
			.scanForFunctions(new TestFunctions())
			.registerFunction("func", new TestFunctions(),
				"binaryFunc", String.class, String.class);
		assertEquals(expr("func('','') + rand(5)"), STRING);
		assertThatThrownBy(() -> expr("rand(5) + func('','')"))
			.isInstanceOf(ExpressionException.class)
			.hasMessage("Arithmetic operation + not supported for types DECIMAL and STRING");
	}

	private ExpressionType expr(String expression) {
		ParseTree parseTree = ExpressionUtils.createParseTree(expression);
		return new ExpressionValidatorVisitor(variableTypeResolver)
			.withFunctionTypeResolver(functionTypeResolver)
			.visit(parseTree);
	}
}
