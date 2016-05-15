package vexprtest;

import java.math.BigDecimal;

import vexpressed.support.ExpressionFunction;

/** @noinspection WeakerAccess*/
public class TestFunctions {

	@ExpressionFunction
	public BigDecimal rand(BigDecimal max) {
		return new BigDecimal(Math.random()).multiply(max);
	}

	boolean invokedFlag;

	@ExpressionFunction("n.op")
	public String emptyFunctionWithNameOverriddenInAnnotation() {
		invokedFlag = true;
		return "nop";
	}

	/** @noinspection unused */
	public String nonAnnotatedMethod(String in) {
		return new StringBuilder(in).reverse().toString();
	}

	/** @noinspection unused - arg names should work without debug info */
	public String binaryFunc(String arg0, String arg1) {
		return arg0 + arg1;
	}

	/** @noinspection unused - arg names should work without debug info */
	public String multiParamFunc(String arg0, String arg1, Integer arg2) {
		return arg0 + arg1 + arg2;
	}
}
