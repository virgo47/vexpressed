package com.virgo47.vexpressed.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testng.Assert.assertEquals;

import com.virgo47.vexpressed.core.ExpressionCalculatorVisitor;
import com.virgo47.vexpressed.core.ExpressionException;

import java.math.BigDecimal;

import org.testng.annotations.Test;

public class MathOperatorTest extends TestBase {

	@Test
	public void arithmeticOperations() {
		assertEquals(evalAsObject("1"), 1);
		assertEquals(evalAsObject("5 + 1"), 6);
		// any non-integer promotes the whole result to BigDecimal, also notice how scale is set
		assertEquals(evalAsObject("5 + 1."), new BigDecimal("6.0"));
		assertEquals(evalAsObject("5 + 1.0"), new BigDecimal("6.0"));
		assertEquals(evalAsObject("1 - 1.050"), new BigDecimal("-0.050"));
		assertEquals(evalAsObject("5 - 6"), -1);
		// integer division
		assertEquals(evalAsObject("5 // 2"), 2);
		assertEquals(evalAsObject("5 % 2"), 1);
		assertEquals(evalAsObject("5. % 2"), new BigDecimal("1.0"));
		assertEquals(evalAsObject("5.1 // 2.6"), 1);
		// floating point division
		assertEquals(evalAsObject("5 / 2"), new BigDecimal("2.5"));
		assertEquals(evalAsObject("4 / 2"), new BigDecimal("2.0"));
		assertEquals(evalAsObject("5. / 2"), new BigDecimal("2.5"));
		assertEquals(evalAsObject("4. / 2"), new BigDecimal("2.0"));
		assertEquals(evalAsObject("5.1 % 2.6"), new BigDecimal("2.5"));
	}

	@Test
	public void integerOverflowReturnsBigDecimal() {
		assertThat(evalAsObject("2_000_000_000 + 2_000_000_000")).isEqualTo(new BigDecimal("4000000000.0"));
		assertThat(evalAsObject(Integer.MAX_VALUE + " * " + Integer.MAX_VALUE))
			.isEqualTo(new BigDecimal(Integer.MAX_VALUE)
				.multiply(new BigDecimal(Integer.MAX_VALUE))
				.setScale(1, BigDecimal.ROUND_DOWN));
		assertThat(evalAsObject("0 - " + Integer.MIN_VALUE))
			.isEqualTo(new BigDecimal(((long) Integer.MAX_VALUE) + 1)
				.setScale(1, BigDecimal.ROUND_DOWN));
	}

	@Test
	public void arithmeticScale() {
		assertEquals(((BigDecimal) evalAsObject("1 / 3.")).scale(),
			ExpressionCalculatorVisitor.DEFAULT_MAX_RESULT_SCALE);
		assertEquals(evalAsObject("1 / 4."), new BigDecimal("0.25")); // trailing zeroes are always trimmed
		assertEquals(evalAsObject("3 / 1"), new BigDecimal("3.0")); // but minimal scale for decimal is 1
	}

	@Test
	public void power() {
		assertEquals(evalAsObject("1^1"), 1);

		// if the result is too big, it will be decimal type
		assertEquals(evalAsObject("10^10"), new BigDecimal("10000000000.0"));

		// any decimal pow on either side results in decimal (even if result is integer)
		assertEquals(evalAsObject("4^0.5"), new BigDecimal("2.0"));
		assertEquals(evalAsObject("0.5^2"), new BigDecimal("0.25"));

		// right associativity test - this is 2^8=256, not 4^3=64
		assertEquals(evalAsObject("2^2^3"), 256);
		// combining with * (has lower precedence) - this is 4*3=12, not 2^6=64)
		assertEquals(evalAsObject("2^2*3"), 12);
		assertEquals(evalAsObject("3*2^2"), 12);

		// ** is just alias for ^
		assertEquals(evalAsObject("2**2"), 4);
		assertEquals(evalAsObject("3*2**2"), 12);
		assertEquals(evalAsObject("2**2*3"), 12);
		assertThat(evalAsObject("2**2**3")).isEqualTo(256);
	}

	@Test
	public void nullValueForArithmeticThrowsException() {
		assertThatThrownBy(() -> evalAsObject("null + 5"))
			.isInstanceOf(ExpressionException.class)
			.hasMessageContaining("Null value not allowed here");
	}
}
