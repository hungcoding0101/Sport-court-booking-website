package com.hung.Models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.UniqueElements;

@Entity
public class Court {
	
	
	@Id
	private String code;
	
	@Min(value = 0)
	private double hourlyPrice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private SportType  sportType;
	
	@OneToMany(mappedBy = "court", fetch = FetchType.LAZY)
	@OrderBy(value = "date ASC, StartTime ASC")
	@UniqueElements
	private List<Reservation> reservations;
	
	
	public Court() {
		this.sportType = new SportType();
		this.reservations = new ArrayList<>();
		this.hourlyPrice = 0d;
	}

	
	public Court(String code, Double hourlyPrice, SportType sportType, List<Reservation> reservations) {
		super();
		this.code = code;
		this.hourlyPrice = hourlyPrice;
		this.sportType = sportType;
		this.reservations = reservations;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public double getHourlyPrice() {
		return hourlyPrice;
	}

	
	public void setHourlyPrice(double hourlyPrice) {
		this.hourlyPrice = hourlyPrice;
	}


	public SportType getSportType() {
		return sportType;
	}


	public void setSportType(SportType sportType) {
		this.sportType = sportType;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}


	@Override
	public String toString() {
		return "Court [sportType=" + sportType + ", code=" + code + ", hourlyPrice=" + hourlyPrice + ", reservations="
				+ reservations + "]";
	}

	
	
}
