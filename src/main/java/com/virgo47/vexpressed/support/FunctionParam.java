package com.virgo47.vexpressed.support;

import com.virgo47.vexpressed.meta.ExpressionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;

/**
 * Annotates the method parameter - without parameters it is not necessary, as any method
 * parameter that is not VariableResolver is considered as possible function parameter.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionParam {

	/** Name of the parameter - if empty, {@link Parameter#getName()} is used. */
	String name() default "";

	/**
	 * Default value (as string) if the argument is not provided. Default value is
	 * supported for all recognized types of {@link ExpressionType} values except for
	 * {@link ExpressionType#OBJECT}.
	 */
	// TODO what should "" mean for strings? Is it null? Can't we specify "" as default value?
	String defaultValue() default "";

	// TODO not used yet, but obviously doesn't go well with defaultValue :-)
	boolean required() default false;
}
