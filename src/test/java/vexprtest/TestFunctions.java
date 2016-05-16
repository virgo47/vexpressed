package vexprtest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import vexpressed.core.VariableResolver;
import vexpressed.support.ExpressionFunction;
import vexpressed.support.ExpressionParam;

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
		@ExpressionParam(name = "first", defaultValue = "XXX") String arg0,
		@ExpressionParam(name = "second") String arg1)
	{
		return arg0 + arg1;
	}

	public String multiParamFunc(String arg0, String arg1, Integer arg2) {
		return arg0 + arg1 + arg2;
	}

	public Map functionWithDefaultsForVariousTypes(
		@ExpressionParam(defaultValue = "") String string, // still treated as null
		@ExpressionParam(defaultValue = "2016-09-06") LocalDate date,
		@ExpressionParam(defaultValue = "2016-09-06 23:24:47") LocalDateTime dateTime, // or with T
		@ExpressionParam(defaultValue = "2016-09-06T23:24:47Z") Instant instant,
		@ExpressionParam(defaultValue = "47.3") BigDecimal bigDecimal,
		@ExpressionParam(defaultValue = "47") Integer integer,
		@ExpressionParam(defaultValue = "true") Boolean boolObject)
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
		return (Integer) variableResolver.resolve("x");
	}
}
