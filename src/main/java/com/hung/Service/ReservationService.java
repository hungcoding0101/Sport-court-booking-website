package com.hung.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.mail.MessagingException;

import org.springframework.validation.BindingResult;

import com.hung.Models.*;
import com.hung.Util.AccountOverDrawnException;


public interface ReservationService {

	public Reservation findById(int id, boolean fetchOrNot);
	public Reservation findByCourt_Date_Time(Court court, LocalDate date, LocalTime startingTime, boolean fetchOrNot);
	public List<Reservation> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending, boolean fetchOrNot);
	public List<Reservation> findByProperties(Object[][] conditions, boolean fetchOrNot) throws IllegalArgumentException;
	public List<Reservation> findAll(boolean fetchOrNot);
	public void save(Reservation theOne);
	public void delete(int id);
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr);
	public Reservation update(Reservation reservation);
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField, Object newValue);
	public Map<String, Object> makingReservation(Reservation reservation, String courtCode, Player player, 
									BindingResult binding) throws Exception;
	public LocalDate[] getDateBoundaries();
	public LocalTime[] getTimeBoundaries();
	public void sendConfirmationMail(Reservation reservation, String serverName) throws Exception;
	public Reservation ConfirmingReservation(int id) throws Exception;
	public int deleteUnComfirmed();
	public int checkDueReservations();
	public int checkFinishedReservations();
}
