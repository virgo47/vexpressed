package com.virgo47.vexpressed;

import com.virgo47.vexpressed.support.ExpressionFunction;
import com.virgo47.vexpressed.support.FunctionParam;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class BasicFunctions {

	@ExpressionFunction
	public static boolean contains(
		@FunctionParam(name = "collection") Collection collection,
		@FunctionParam(name = "item") Object item)
	{
		return collection.contains(item);
	}

	@ExpressionFunction
	public static String asString(@FunctionParam(name = "value") Object value) {
		if (value instanceof LocalDate) {
			return DateTimeFormatter.ISO_LOCAL_DATE.format((LocalDate) value);
		}
		if (value instanceof LocalDateTime) {
			return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format((LocalDateTime) value);
		}
		if (value instanceof Instant) {
			return DateTimeFormatter.ISO_INSTANT.format((Instant) value);
		}

		return value.toString();
	}

	@ExpressionFunction
	public static Boolean asBoolean(@FunctionParam(name = "value") Object value) {
		if (value instanceof Boolean) {
			return (Boolean) value;
		}

		return Boolean.valueOf(value.toString());
	}

	@ExpressionFunction
	public static Integer asInteger(@FunctionParam(name = "value") Object value) {
		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value instanceof BigDecimal) {
			// we want to floor it, but also check that it fits int
			return ((BigDecimal) value).setScale(0, BigDecimal.ROUND_FLOOR).intValueExact();
		}

		return new Integer(value.toString());
	}

	@ExpressionFunction
	public static BigDecimal asDecimal(@FunctionParam(name = "value") Object value) {
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		}

		return new BigDecimal(value.toString());
	}

	// TODO write tests for these
	@ExpressionFunction
	public static LocalDate asDate(@FunctionParam(name = "value") Object value) {
		if (value instanceof LocalDate) {
			return (LocalDate) value;
		}

		return LocalDate.parse(value.toString());
	}

	@ExpressionFunction
	public static LocalDateTime asDateTime(@FunctionParam(name = "value") Object value) {
		if (value instanceof LocalDateTime) {
			return (LocalDateTime) value;
		}

		return LocalDateTime.parse(value.toString());
	}

	@ExpressionFunction
	public static Instant asTimestamp(@FunctionParam(name = "value") Object value) {
		if (value instanceof Instant) {
			return (Instant) value;
		}

		return Instant.parse(value.toString());
	}
}
