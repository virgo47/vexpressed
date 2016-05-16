package vexpressed.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotates the method as a function. */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpressionFunction {

	/** Name of the function - if empty, method name is used. */
	String value() default "";

	/**
	 * Array of parameter names in the order of method parameters.
	 * If used, all parameters must be listed.
	 */
	@Deprecated // will be replaced by @ExpressionParam
	String[] paramNames() default {};
}
