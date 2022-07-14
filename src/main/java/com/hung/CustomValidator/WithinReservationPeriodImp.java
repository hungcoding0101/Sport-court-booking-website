package com.hung.CustomValidator;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Component;

import com.hung.Service.ReservationService;
import com.hung.Service.ReservationServiceImp;

@Configurable
public class WithinReservationPeriodImp implements ConstraintValidator<WithinReservationPeriod, LocalDate>{

	
	ReservationService reservationService;

	public WithinReservationPeriodImp() {
		super();
	}
	
	@Autowired
	public WithinReservationPeriodImp(ReservationService reservationService) {
		super();
		this.reservationService = reservationService;
	}

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		LocalDate[] boundaries = {};


		boundaries = reservationService.getDateBoundaries();

		System.out.println("HERE: WithinReservationPeriodImp: " + boundaries[0] + "||" + boundaries[1]);
		if(!value.isBefore(boundaries[0]) && !value.isAfter(boundaries[1])) {
			return true;
		}

		return false;
	}

}