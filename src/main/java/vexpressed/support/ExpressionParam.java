package vexpressed.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;

/** Annotates the method parameter. */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpressionParam {

	/** Name of the parameter - if empty, {@link Parameter#getName()} is used. */
	String name() default "";

	// TODO not used yet
	/** Default value (as string) if the argument is not provided. */
	String defaultValue();
}
