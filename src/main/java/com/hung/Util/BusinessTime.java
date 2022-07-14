package com.hung.Util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Payload;


@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusinessTime {
	
	String OpeningTime();
	String ClosingTime();

	Class<?>[] groups() default{};
	
	public abstract Class<? extends Payload>[] payload()default {};
}
