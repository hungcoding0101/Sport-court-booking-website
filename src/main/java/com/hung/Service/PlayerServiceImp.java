package com.hung.Service;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.hung.CustomValidator.SignUpConstraints;
import com.hung.DAO.PlayerDAO;
import com.hung.Models.Player;
import com.hung.Models.Reservation;
import com.hung.Models.ReservationStatus;


@Service
@Validated
@Transactional
@EnableTransactionManagement
public class PlayerServiceImp implements PlayerService{

	
	private PlayerDAO playerDAO;	
	private javax.validation.Validator validator;
	private MailService mailService;
	
	@Autowired
	public PlayerServiceImp(PlayerDAO playerDAO, MailService mailService,javax.validation.Validator validator) {
		super();
		this.validator = validator;
		this.playerDAO = playerDAO;
		this.mailService = mailService;
	}


	@Transactional(readOnly = true)
	@Override
		public List<Player> findAll(boolean fetchOrNot) {
			return playerDAO.findAll(fetchOrNot);
		}

	
	@Transactional(readOnly = true)
	@Override
		public Player findById(int id, boolean fetchOrNot) {
			return playerDAO.findById(id, fetchOrNot);
		}

	
	@Transactional(readOnly = true)
	@Override
		public Player findByNameAndPassword(String name, String password, boolean fetchOrNot) {
			return playerDAO.findByNameAndPassword(name, password, fetchOrNot);
		}
	
	
	@Transactional(readOnly = true)
	@Override
		public List<Player> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
																	boolean fetchOrNot) {
			return playerDAO.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
		}
	
	
	@Transactional(readOnly = true)
	@Override
		public List<Player> findByProperties(Object[][] conditions, boolean fetchOrNot) throws IllegalArgumentException {
			return playerDAO.findByProperties(conditions, fetchOrNot);
		}
	
	@Override
		public int save(Player theOne) {
			return playerDAO.save(theOne);
		}
	
	@Override
		public void reconnect(Player player) {
			playerDAO.reconnect(player);
		}
	
	@Override
		public Player update(Player player) {
			return playerDAO.update(player);
		}
	
	@Override
		public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
				Object newValue) {
			return playerDAO.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
		}
	
	@Override
		public void delete(int id) {
			playerDAO.delete(id);
		}
	
	@Override
		public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
			return playerDAO.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
		}
	
	@Override
		public int signUp(Player newPlayer) throws ConstraintViolationException {
			
		try {
			
			Set<ConstraintViolation<Player>> constraintViolations = validator.validate(newPlayer, SignUpConstraints.class);
			System.out.println("HERE: CUSTOM PLAYER VALIDATION STARTED" );
			
			if(!constraintViolations.isEmpty()) {
				for(ConstraintViolation<Player> violation: constraintViolations) {
					String propertyPath = violation.getPropertyPath().toString();
					System.out.println("HERE: WRONG VALUE:" + propertyPath + "\n" + violation.getInvalidValue().toString());
				}
				throw new ConstraintViolationException("Error occurred when binding ", constraintViolations);
				}
			
				newPlayer.setCoins(50);
				
				return playerDAO.save(newPlayer);
			
			}catch (ConstraintViolationException e) {
				throw e;
			}
			
		}


//	@Override
//	@Cacheable(value = "player", key = "{#name, #passwords}")
	@Transactional(readOnly = true)
		public Player VerifyingPlayer(String name, String passwords) {
		System.out.println("HERE: VERIFYING...");
			Player playerMatched = findByNameAndPassword(name, passwords, true);
				return playerMatched;
	}

	@Override
	@Transactional(readOnly = true)
		public Boolean checkDuplication(String field, Object value) {
			
				List<Player> playersWithThisField = playerDAO.findByProperty(field, value, true, false);
			
				if(playersWithThisField.isEmpty()) {return false;}
			
				return true;
			}
		
//	@Caching(evict = @CacheEvict(value = "player", key = "{#result.getName(), #currentPasswords}"), 
//						put = @CachePut(value = "player", key = "{#result.getName(), #newPasswords}"))
	@Override
		public Player changeInfo( String newEmail, String newPasswords, String currentPasswords, int playerId) {
			
		Player updatedPlayer = findById(playerId, true);

			if(updatedPlayer.getPassword().equals(currentPasswords)) {
					if(newEmail != null && !newEmail.isEmpty()) {
						updatedPlayer.setEmail(newEmail);
					}
					
					if(newPasswords != null && !newPasswords.isEmpty()) {
						updatedPlayer.setPassword(newPasswords);
					}
					
					return updatedPlayer;
			}
			
			else {throw new InputMismatchException("Wrong password");}
		}
	
	
	@Override
		public Player cancellingReservations( String[] cancelled, int playerId ){
				
		Player updatedPlayer = findById(playerId, true);
			List<Reservation> reservations = updatedPlayer.getReservations();
			
			for(String s: cancelled) {
				for(int i = 0; i < reservations.size(); i++) {
					if(reservations.get(i).getReservationCode().equals(s)) {
						reservations.get(i).setStatus(ReservationStatus.CANCELLED);
					}
				}
			}
			return updatedPlayer;
		}
	
	@Override
	public void sendInvitationMail(String newBieEmail, int introducerId, String introducerNickname, String serverName)throws MessagingException {
		try {

			String subject = "HUNG'S SPORT CENTER - Invitation For membership From Your Friend \"" + introducerNickname + "\"";
			String from = "doctythanlong@gmail.com";
			String to = newBieEmail;
			String link = "http://" + serverName + ":8080/MVCJavaConfig2/?introducerId=" + introducerId + "&newBieEmail=" + newBieEmail;
			String content = "<h3 style=\"color: MidnightBlue\">"
										+ "Hi,<br><br>"
										+ "Your friend \"" + introducerNickname + "\" is a member of our sport center, and now he/she would like to send an invitation for membership to you.<br><br>"
										+"If you are interested,  please click the link below to join with us: <br><br>"
										+ "<a href=\"" + link
										+"\">" + link + "</a> <br><br>"
										+"Thank you for reading this mail.<br> We hope you have a good day with good health!"
										+ "<h3>";
			
			
				System.out.println("HERE: RESERVATION SERVICE Thread name: " + Thread.currentThread().getName());
				
				CompletableFuture<String> result = mailService.sendMail(from, subject, to, null, null, content);
				
				result.thenRun(() -> System.out.println("HERE: INVITATION MAIL SENT!")); 
				
			} catch (MessagingException e) {
				e.printStackTrace();
				throw e;
			}catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			
		}
	
	@Override
		public Player rewardingIntroducer(int id, String newBieName, String serverName) throws MessagingException {
			Player introducer = findById(id, true);
			
			if(introducer != null) {
			introducer.setCoins(introducer.getCoins() + 30d);
			
			String subject = "HUNG'S SPORT CENTER - Announcement about your invitation to \"" + newBieName + "\" ";
			String from = "doctythanlong@gmail.com";
			String to = introducer.getEmail();
			String content = "<h3 style=\"color: MidnightBlue\">"
										+ "Hi \"" + introducer.getName() + "\",<br><br>"
										+ "Your friend \"" +  newBieName + "\" has successfully joined our communication, thanks to your invitation.<br><br>"
										+"You has been reward 30$ as a token of gratitude <br><br>"
										+"Thank you very much!<br> We hope you have a good day with good health!"
										+ "<h3>";
			CompletableFuture<String> result = mailService.sendMail(from, subject, to, null, null, content);
			result.thenRun(() -> System.out.println("HERE: ANNOUNCEMENT MAIL SENT!")); 
			}
			
			return introducer;	
		}

}
