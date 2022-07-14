package com.hung.CustomValidator;

import java.time.LocalTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDurationImp implements ConstraintValidator<ValidDuration, LocalTime>{

	@Override
	public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
		boolean emptyDuration = value.getHour() == 0 && value.getMinute() == 0;
		boolean sporadicDuration = value.getMinute()%30 != 0;
		
		if(emptyDuration || sporadicDuration) {
			return false;
		}
		
		return true;
	}

}
