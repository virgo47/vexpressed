package vexpressed;

import vexpressed.support.ExpressionFunction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class BasicFunctions {

	@ExpressionFunction(paramNames = {"collection", "item"})
	public static boolean contains(Collection collection, Object item) {
		return collection.contains(item);
	}

	@ExpressionFunction(paramNames = {"value"})
	public static String asString(Object value) {
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

	@ExpressionFunction(paramNames = {"value"})
	public static BigDecimal asBigDecimal(Object value) {
		if (!(value instanceof BigDecimal)) {
			return new BigDecimal(value.toString());
		}
		return (BigDecimal) value;
	}

	@ExpressionFunction(paramNames = {"value"})
	public static LocalDate asLocalDate(Object value) {
		if (!(value instanceof LocalDate)) {
			return LocalDate.parse(value.toString());
		}
		return (LocalDate) value;
	}

	public static void main(String[] args) {
		System.out.println("(5 % 3) = " + (5 % -3));
	}
}
