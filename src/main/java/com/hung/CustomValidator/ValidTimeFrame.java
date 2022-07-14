package com.hung.CustomValidator;

import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hung.Models.Reservation;
import com.hung.Service.ReservationService;
import com.hung.Util.BusinessTime;

@Component
public class ValidTimeFrame implements Validator{

	private ReservationService reservationService;
	private MessageSource messageSource;
	
	@Autowired
	public ValidTimeFrame(@Lazy ReservationService reservationService, MessageSource messageSource) {
		this.reservationService = reservationService;
		this.messageSource = messageSource;
	}

	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Reservation.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Reservation reservation = (Reservation) target;
		
		LocalTime startTime = reservation.getStartTime();
		LocalTime endTime = reservation.getEndTime();
		
		LocalTime openingTime = reservationService.getTimeBoundaries()[0];
		LocalTime closingTime = reservationService.getTimeBoundaries()[1];
	
		
		// Check if endTime is within business time
		if(endTime.isBefore(openingTime)||endTime.isAfter(closingTime)) {
			errors.rejectValue("duration", "", messageSource.getMessage("CustomValidation.WithinBusinessTime", null, Locale.ENGLISH));
			return;
		}
		
		List<Reservation> reservations = reservationService.findByProperty("court", reservation.getCourt(), true, false);
		
		//Then check if this reservation overlaps any current reservation
		for(Reservation madeReservation: reservations) {
			if(madeReservation.getDate().equals(reservation.getDate())) {
				boolean bothBeforeOldStartTime = (endTime.isBefore(madeReservation.getStartTime()) ||
																			endTime.equals((madeReservation.getStartTime())));
				
				boolean bothAfterOldEndTime = (startTime.isAfter(madeReservation.getEndTime()) ||
																		startTime.equals(madeReservation.getEndTime()));
				
				if(!(bothBeforeOldStartTime||bothAfterOldEndTime)) {
						errors.rejectValue("duration", "", messageSource.getMessage("CustomValidation.InvalidReservationTimeFrame", null, Locale.ENGLISH));
						return;
				}
			}
		}
	}
}
