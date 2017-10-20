package com.virgo47.vexpressed.test;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ExpressionWithCustomOperatorsTest extends TestBase {

	@Test
	public void functionExecutionWithoutExecutorFails() {
		// TODO failure
		eval("1 +*+ 2");
	}

	@Test
	public void functionExecutionWithConstantFunction() {
		functionExecutor = (fname, params) -> 1;
		// TODO evaluation
		assertEquals(eval("1 +*+ 2"), 2);
	}
}
