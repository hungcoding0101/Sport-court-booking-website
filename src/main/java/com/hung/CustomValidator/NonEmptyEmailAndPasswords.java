package com.hung.CustomValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NonEmptyEmailAndPasswordsImp.class)
@Documented
public @interface NonEmptyEmailAndPasswords {
	
	String message() default "This field must not be empty";
	
	Class<?>[] groups() default{};
	
	public abstract Class<? extends Payload>[] payload()default {};
}
