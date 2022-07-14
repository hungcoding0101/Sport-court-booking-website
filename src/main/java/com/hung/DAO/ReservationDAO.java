package com.hung.DAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.hung.Models.Court;
import com.hung.Models.Reservation;
public interface ReservationDAO {

	
	public Reservation findById(int id, boolean fetchOrNot);
	public Reservation findByCourt_Date_Time(Court court, LocalDate date, LocalTime startingTime, boolean fetchOrNot);
	public List<Reservation> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending, boolean fetchOrNot);
	public List<Reservation> findByProperties(Object[][] conditions, boolean fetchOrNot) throws IllegalArgumentException;
	public List<Reservation> findAll(boolean fetchOrNot);
	public void save(Reservation reservation);
	public Reservation update(Reservation reservation);
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField, Object newValue);
	public void delete(int id);
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr);

}
