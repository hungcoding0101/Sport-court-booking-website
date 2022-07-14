package com.hung.CustomValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalTime;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WithinBusinessTimeImp.class)
@Documented
public @interface WithinBusinessTime {
	
	String message() default "{CustomValidation.WithinBusinessTime}";
	
	Class<?>[] groups() default{};
	
	public abstract Class<? extends Payload>[] payload()default {};
}
