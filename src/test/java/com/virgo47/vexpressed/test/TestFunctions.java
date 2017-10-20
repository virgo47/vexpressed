package com.virgo47.vexpressed.test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.virgo47.vexpressed.core.VariableResolver;
import com.virgo47.vexpressed.support.ExpressionFunction;
import com.virgo47.vexpressed.support.FunctionParam;

/** @noinspection WeakerAccess, unused */
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

	public String nonAnnotatedMethod(String in) {
		return new StringBuilder(in).reverse().toString();
	}

	public String binaryFunc(
		@FunctionParam(name = "first", defaultValue = "XXX") String arg0,
		@FunctionParam(name = "second") String arg1)
	{
		return arg0 + arg1;
	}

	public String multiParamFunc(String arg0, String arg1, Integer arg2) {
		return arg0 + arg1 + arg2;
	}

	public Map functionWithDefaultsForVariousTypes(
		@FunctionParam(defaultValue = "") String string, // still treated as null
		@FunctionParam(defaultValue = "2016-09-06") LocalDate date,
		@FunctionParam(defaultValue = "2016-09-06 23:24:47") LocalDateTime dateTime, // or with T
		@FunctionParam(defaultValue = "2016-09-06T23:24:47Z") Instant instant,
		@FunctionParam(defaultValue = "47.3") BigDecimal bigDecimal,
		@FunctionParam(defaultValue = "47") Integer integer,
		@FunctionParam(defaultValue = "true") Boolean boolObject)
	{
		Map<String, Object> map = new HashMap<>();
		map.put("string", string);
		map.put("date", date);
		map.put("dateTime", dateTime);
		map.put("instant", instant);
		map.put("bigDecimal", bigDecimal);
		map.put("integer", integer);
		map.put("boolObject", boolObject);
		return map;
	}

	public Integer var_x(VariableResolver variableResolver) {
		return variableResolver.resolveSafe("x");
	}
}
