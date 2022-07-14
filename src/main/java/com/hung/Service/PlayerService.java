package com.hung.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.naming.Binding;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

import com.hung.CustomValidator.NonEmptyEmailAndPasswords;
import com.hung.CustomValidator.UniquePlayerEmail;
import com.hung.Models.Court;
import com.hung.Models.Player;
import com.hung.Models.Reservation;

public interface PlayerService {
	public Player findById(int id, boolean fetchOrNot);
	public Player findByNameAndPassword(String name, String password, boolean fetchOrNot);
	public List<Player> findAll(boolean fetchOrNot);
	public List<Player> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending, boolean fetchOrNot);
	public List<Player> findByProperties(Object[][] conditions, boolean fetchOrNot) throws IllegalArgumentException;
	public int save(Player theOne);
	public void reconnect(Player player);
	public Player update(Player player);
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField, Object newValue);
	public void delete(int id);
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr);
	
	public int signUp(Player newPlayer) throws ConstraintViolationException;
	public Player VerifyingPlayer(String name, String passwords);
	public Boolean checkDuplication(String field, Object value);
	
	@NonEmptyEmailAndPasswords
	public Player changeInfo(@Email(message = "Please enter a valid email address")  String newEmail,  String newPasswords,
										@NotEmpty (message = "This field must not be empty") @Pattern(regexp = ".{8,}", message = "Password must be at least 8 characters long")  String currentPasswords, int playerId);
	
	public Player cancellingReservations( @NotEmpty(message = "") String[] cancelled, int playerId);
	
	public void sendInvitationMail(
			@NotEmpty (message = "This field must not be empty") @Email (message = "Please enter a valid email address") String newBieEmail,
			int introducerId, @NotEmpty (message = "This field must not be empty") String introducerNickname, 
			String serverName) throws MessagingException;
	
	public Player rewardingIntroducer(int id, String newBieName, String serverName) throws MessagingException;
}
