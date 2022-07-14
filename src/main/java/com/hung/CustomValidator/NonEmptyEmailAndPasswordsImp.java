package com.hung.CustomValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class NonEmptyEmailAndPasswordsImp implements ConstraintValidator<NonEmptyEmailAndPasswords, Object[]>{

	@Override
		public boolean isValid(Object[] value, ConstraintValidatorContext context) {
			String newEmail = (String) value[0];
			String oldPass = (String) value[1];
			if((newEmail == null || newEmail.isEmpty())&&(oldPass == null || oldPass.isEmpty())) {
				context.buildConstraintViolationWithTemplate("You must provide either new email or new passwords").addParameterNode(0).addConstraintViolation();
				context.buildConstraintViolationWithTemplate("You must provide either new email or new passwords").addParameterNode(1).addConstraintViolation();
				return false;
			}
			
			String pattern = ".{8,}";
			if(!oldPass.isEmpty()) {
				if(!oldPass.matches(pattern)) {
					context.buildConstraintViolationWithTemplate("Password must be at least 8 characters long").addParameterNode(1).addConstraintViolation();
					return false;
				}
			}
			
			return true;
		}

}
