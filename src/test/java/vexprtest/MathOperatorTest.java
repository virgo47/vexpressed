package vexprtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertEquals;

import vexpressed.core.ExpressionCalculatorVisitor;
import vexpressed.core.ExpressionException;

import java.math.BigDecimal;

import org.testng.annotations.Test;

public class MathOperatorTest extends TestBase {

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
		assertEquals(eval("5. % 2"), BigDecimal.ONE);
		assertEquals(eval("5.1 % 2.6"), new BigDecimal("2.5"));
		// floating point division
		// TODO
	}

	@Test
	public void integerOverflowReturnsBigDecimal() {
		assertThat(eval("2_000_000_000 + 2_000_000_000")).isEqualTo(new BigDecimal(4_000_000_000L));
		assertThat(eval(Integer.MAX_VALUE + " * " + Integer.MAX_VALUE))
			.isEqualTo(new BigDecimal(Integer.MAX_VALUE)
				.multiply(new BigDecimal(Integer.MAX_VALUE)));
		assertThat(eval("0 - " + Integer.MIN_VALUE))
			.isEqualTo(new BigDecimal(((long) Integer.MAX_VALUE) + 1));

		// after overflow the same expression may change behaviour
		variableResolver = var -> 2000;
		assertThat(eval("(a+a)/(a+1)")).isEqualTo(1);
		assertThat(eval("(a+a)%(a+1)")).isEqualTo(1999);
		// overflow lets decimal math kick in
		variableResolver = var -> 2_000_000_000;
		assertThat(eval("(a+a)/(a+1)")).isEqualTo(new BigDecimal("2.000000")); // small precision
		assertThat(eval("(a+a)%(a+1)")).isEqualTo(new BigDecimal("1999999999"));
	}

	@Test
	public void arithmeticScale() {
		assertEquals(eval("1 / 3"), 0); // integer result
		assertEquals(((BigDecimal) eval("1 / 3.")).scale(),
			ExpressionCalculatorVisitor.DEFAULT_MAX_RESULT_SCALE);
		assertEquals(eval("1 / 4."), new BigDecimal("0.25")); // trailing zeroes are always trimmed
	}

	@Test
	public void power() {
		assertEquals(eval("1^1"), 1);

		// if the result is too big, it will be decimal type
		assertEquals(eval("10^10"), new BigDecimal("10000000000"));

		// any decimal pow on either side results in decimal (even if result is integer)
		assertEquals(eval("4^0.5"), new BigDecimal("2.0"));
		assertEquals(eval("0.5^2"), new BigDecimal("0.25"));

		// right associativity test - this is 2^8=256, not 4^3=64
		assertEquals(eval("2^2^3"), 256);
		// combining with * (has lower precedence) - this is 4*3=12, not 2^6=64)
		assertEquals(eval("2^2*3"), 12);
		assertEquals(eval("3*2^2"), 12);
	}

	@Test
	public void nullValueForArithmeticThrowsException() {
		assertThatThrownBy(() -> eval("null + 5"))
			.isInstanceOf(ExpressionException.class)
			.hasMessageContaining("Null value not allowed here");
	}
}
