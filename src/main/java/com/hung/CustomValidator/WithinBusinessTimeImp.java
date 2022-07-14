package com.hung.CustomValidator;

import java.time.LocalTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.hung.Models.Reservation;
import com.hung.Util.BusinessTime;

public class WithinBusinessTimeImp implements ConstraintValidator<WithinBusinessTime, LocalTime>{


	@Override
	public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
		
		BusinessTime within = null;
		
		within = Reservation.class.getAnnotation(BusinessTime.class);

		LocalTime openingTime = LocalTime.parse(within.OpeningTime());
		LocalTime closingTime = LocalTime.parse(within.ClosingTime());
		
		System.out.println("HERE: WithinBusinessTimeImp : " + openingTime + " --- " + closingTime);
		if(value.isBefore(openingTime) || value.isAfter(closingTime)) {
			return false;
		}
		return true;
	}

}
