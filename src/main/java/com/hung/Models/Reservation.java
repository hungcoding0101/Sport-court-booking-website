package com.hung.Models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.google.protobuf.Duration;
import com.hung.CustomValidator.MinimumOfDouble;
import com.hung.CustomValidator.ValidDuration;
import com.hung.CustomValidator.WithinBusinessTime;
import com.hung.CustomValidator.WithinReservationPeriod;
import com.hung.Util.BusinessTime;
import com.hung.Util.DateTimeUtil;



@Entity
@BusinessTime(OpeningTime = "08:00", ClosingTime = "21:00")
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(updatable = false, nullable = false)
	private int id;
	
	@Column(unique = true)
	private String reservationCode;
	
	@NotNull(message = "This field must not be left blank")
	@ManyToOne(fetch = FetchType.LAZY)
	private Court court = new Court();
	
	@NotNull(message = "Please choose the sport you like")
	@ManyToOne(fetch = FetchType.LAZY)
	private SportType sportType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Player player;
	
	@NotNull(message = "You must choose a specific date for this reservation")
	@Future(message = "The date must be in future")
	@WithinReservationPeriod
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	@NotNull(message = "You must choose a specific time for this reservation")
	@WithinBusinessTime
	private LocalTime StartTime;
	
	@NotNull(message = "This field must not be left blank")
	@ValidDuration
	private LocalTime duration;
	
	private LocalTime EndTime;

	private double rental;

	@Enumerated(EnumType.STRING)
	private ReservationStatus  status = ReservationStatus.FRESH;
	
	public Reservation() {		
		this.player = new Player();
		this.sportType = new SportType();
		this.date = LocalDate.now().plusDays(1);
		this.StartTime = LocalTime.now();
		this.duration = LocalTime.of(1, 0);
	}


	public Reservation(int id, String reservationCode, @NotNull(message = "This field must not be left blank") Court court,
			@NotNull(message = "Please choose the sport you like") SportType sportType, Player player,
			@NotNull(message = "You must choose a specific date for this reservation") @Future(message = "The date must be in future") LocalDate date,
			@NotNull(message = "You must choose a specific time for this reservation") LocalTime startTime,
			@NotNull(message = "This field must not be left blank") LocalTime duration, LocalTime endTime,
			double rental, ReservationStatus status) {
		super();
		this.id = id;
		this.reservationCode = reservationCode;
		this.court = court;
		this.sportType = sportType;
		this.player = player;
		this.date = date;
		StartTime = startTime;
		this.duration = duration;
		EndTime = endTime;
		this.rental = rental;
		this.status = status;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReservationCode() {
		return reservationCode;
	}


	public void setReservationCode(String reservationCode) {
		this.reservationCode = reservationCode;
	}


	public Court getCourt() {
		return court;
	}

	public void setCourt(Court court) {
		this.court = court;
		this.updateRental();
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getDuration() {
		return duration;
	}

	public void setDuration(LocalTime duration) {
		this.duration = duration;
		this.updateRental();
		this.updateEndTime();
	}
	

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public SportType getSportType() {
		return sportType;
	}

	public void setSportType(SportType sportType) {
		this.sportType = sportType;
	}

	
	public LocalTime getStartTime() {
		return StartTime;
	}


	public void setStartTime(LocalTime startTime) {
		StartTime = startTime;
		updateEndTime();
	}


	public LocalTime getEndTime() {
		return EndTime;
	}


	public void setEndTime(LocalTime endTime) {
		EndTime = endTime;
		updateRental();
	}

	public LocalTime updateEndTime() {
		this.EndTime = StartTime.plusHours(duration.getHour()).plusMinutes(duration.getMinute());
		return EndTime;
	}
	
	public double getRental() {
		return this.rental;
	}

	public void setRental(double rental) {
		this.rental = rental;
	}

	public double updateRental() {
		this.rental = court.getHourlyPrice() * (duration.getHour() + (double)duration.getMinute()/60);
		return this.rental;
	}
	
	
	public ReservationStatus  getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus  status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "Reservation [id=" + id + ", reservationCode=" + reservationCode + ", court=" + court.getCode() +
				", sportType=" + sportType.getName() + ", player=" + player.getName() + ", date=" + date +
				", StartTime=" + StartTime + ", duration=" + duration
				+ ", EndTime=" + EndTime + ", rental=" + rental + ", status=" + status.toString() + "]";
	}
	
	
}
