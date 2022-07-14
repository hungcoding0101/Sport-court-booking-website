package com.hung.CustomValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintViolation;

import org.hibernate.mapping.Column;

import com.hung.Models.Player;
import com.hung.Models.Reservation;

public class MinOfDouble implements ConstraintValidator<MinimumOfDouble, Double>{

	public MinOfDouble() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext context) {
		MinimumOfDouble minimumOfDouble = null;
		try {
			
			minimumOfDouble = Reservation.class.getDeclaredField("duration").getAnnotation(MinimumOfDouble.class);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		if(value >= minimumOfDouble.value()) {
			return true;
		}
		return false;
	}

}
