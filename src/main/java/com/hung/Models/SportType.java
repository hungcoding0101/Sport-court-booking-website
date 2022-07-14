package com.hung.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.lang.NonNull;

import com.hung.CustomValidator.SportTypeNameUnique;


@Entity
//@NamedEntityGraph(name = "SportType.court.forview",
//attributeNodes = {@NamedAttributeNode("courts")})
public class SportType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull(message = "Name can not be null")
	@SportTypeNameUnique
	@Column(unique = true)
	private String name;
	  
	@OneToMany(mappedBy = "sportType", fetch = FetchType.LAZY)
	private List<Court> courts;

	
	@OneToMany(mappedBy = "sportType", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
	@UniqueElements
	private List<Reservation> reservations;
	
	//--------------------------------------------------------- 
	public SportType() {
		this.reservations = new ArrayList<>();
		this.courts = new ArrayList<>();
	}


	private SportType(int id, String name, List<Court> courts, List<Reservation> reservations) {
		this.id = id;
		this.name = name;
		this.courts = courts;
		this.reservations = reservations;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public List<Court> getCourts() {
		return courts;
	}

	public void setCourts(List<Court> courts) {
		this.courts = courts;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}



	@Override
	public String toString() {
		return "SportType [id=" + id + ", name=" + name + ", Courts: " + this.courts+ "Reservations: "+ this.reservations+"]";
	}
	 
	 
}
