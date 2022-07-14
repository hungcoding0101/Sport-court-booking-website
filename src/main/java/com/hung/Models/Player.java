package com.hung.Models;

import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Value;

import com.hung.CustomValidator.UniquePlayerName;
import com.hung.Controllers.FirstController;
import com.hung.CustomValidator.LoginConstraints;
import com.hung.CustomValidator.MinimumOfDouble;
import com.hung.CustomValidator.SignUpConstraints;
import com.hung.CustomValidator.UniquePlayerEmail;

@Entity
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	@Pattern(regexp = "[\\w&&[\\D]]{2,}" , message = "{Player.name.validation}",
			groups = {SignUpConstraints.class, LoginConstraints.class})
	@NotEmpty (message = "{Notnull.Player.name&password&email.validation}",
					groups =  {SignUpConstraints.class, LoginConstraints.class})
	@UniquePlayerName(groups = SignUpConstraints.class)
	@Column(unique = true)
	private String name;
	
	
	@Email(message = "{Player.email.validation}", groups = SignUpConstraints.class)
	@NotEmpty (message = "{Notnull.Player.name&password&email.validation}", groups = SignUpConstraints.class)
	@UniquePlayerEmail (groups = SignUpConstraints.class)
	@Column(unique = true)
	private String email; 
	
	@Pattern(regexp = ".{8,}", message = "{Player.password.validation}",
			groups =  {SignUpConstraints.class, LoginConstraints.class})
	@NotEmpty (message = "{Notnull.Player.name&password&email.validation}",
			groups = {SignUpConstraints.class, LoginConstraints.class})
	private String password;
	 
	
	 @OneToMany(mappedBy = "player", orphanRemoval = true)
	 @OrderBy(value = "date ASC, StartTime ASC")
	 private List<Reservation> reservations;
	 
	 
	 @MinimumOfDouble(value = 0d)
	 private double coins;
	 
	 
	public Player() {
		super();
	}


	public Player(int id, @Pattern(regexp = "[\\w&&[\\D]]{2,}", message = "{Player.name.validation}", groups = {
			SignUpConstraints.class,
			LoginConstraints.class }) @NotEmpty(message = "{Notnull.Player.name&password&email.validation}", groups = {
					SignUpConstraints.class, LoginConstraints.class }) String name,
			@Email(message = "{Player.email.validation}", groups = SignUpConstraints.class) @NotEmpty(message = "{Notnull.Player.name&password&email.validation}", groups = SignUpConstraints.class) String email,
			@Pattern(regexp = ".{8,}", message = "{Player.password.validation}", groups = { SignUpConstraints.class,
					LoginConstraints.class }) @NotEmpty(message = "{Notnull.Player.name&password&email.validation}", groups = {
							SignUpConstraints.class, LoginConstraints.class }) String password,
			List<Reservation> reservations, double coins) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.reservations = reservations;
		this.coins = coins;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
	public double getCoins() {
		return coins;
	}


	public void setCoins(double coins) {
		this.coins = coins;
	}


	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", reservations="
				+ reservations + ", coins=" + coins + "]";
	}
	 
	 
}
