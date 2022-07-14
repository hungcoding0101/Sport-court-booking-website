package com.hung.Service;



import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.hung.CustomValidator.ValidTimeFrame;
import com.hung.DAO.ReservationDAO;

import com.hung.Models.Player;
import com.hung.Models.Reservation;
import com.hung.Models.ReservationStatus;
import com.hung.Util.AccountOverDrawnException;
import com.hung.Util.BusinessTime;
import com.hung.Models.Court;



@Service
@Transactional
public class ReservationServiceImp implements ReservationService {

	private ReservationDAO rsDAO;
	
	private CourtService courtService;
	
	private PlayerService playerService;

	private javax.validation.Validator validator;
	
	private ValidTimeFrame validTimeFrame;
	
	private MailService MailService;
	
	public ReservationServiceImp() {
		super();
	}

	@Autowired
	public ReservationServiceImp(ReservationDAO rsDAO, CourtService courtService, PlayerService playerService,
													Validator validator, ValidTimeFrame validTimeFrame, MailService MailService) {
		super();
		this.rsDAO = rsDAO;
		this.courtService = courtService;
		this.playerService = playerService;
		this.validator = validator;
		this.validTimeFrame = validTimeFrame;
		this.MailService = MailService;
	}

	@Override
	@Transactional(readOnly = true)
		public List<Reservation> findAll(boolean fetchOrNot) {
			return rsDAO.findAll(fetchOrNot);
		}

	@Override
		public List<Reservation> findByProperties(Object[][] conditions, boolean fetchOrNot) throws IllegalArgumentException{
			return rsDAO.findByProperties(conditions, fetchOrNot);
		}
	
	@Override
	@Transactional(readOnly = true)
	public Reservation findById(int id, boolean fetchOrNot) {
		return rsDAO.findById(id, fetchOrNot);
	}
	

	
	@Override
	@Transactional(readOnly = true)
	public Reservation findByCourt_Date_Time(Court court, LocalDate date, LocalTime startingTime, boolean fetchOrNot){
		return rsDAO.findByCourt_Date_Time(court, date, startingTime, fetchOrNot);
	}

	
	@Override
	@Transactional(readOnly = true)
	public List<Reservation> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending, boolean fetchOrNot) {
		return rsDAO.findByProperty(propertyName, value, trueIsAscending_falseIsDescending, fetchOrNot);
	}

	@Override
	public void save(Reservation theOne) {
		rsDAO.save(theOne);
	}

	@Override
	public void delete(int id) {
		rsDAO.delete(id);
	}

	@Override
		public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {		
			return rsDAO.deleteByProperties(conditions, TrueIsAnd_FalseIsOr);
		}
	
	@Override
	public Reservation update(Reservation reservation) {
		return rsDAO.update(reservation);		
	}

	@Override
		public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, 
				String targetField, Object newValue) {
			
		return rsDAO.updateByProperties(conditions, TrueIsAnd_FalseIsOr, targetField, newValue);
		}

	
	@Override
		public Map<String, Object> makingReservation(Reservation reservation, String courtCode, Player player, BindingResult binding) 
																			throws Exception{		
			Court court = courtService.find(courtCode);
			 player = playerService.findById( player.getId(), true);
			
			reservation.setCourt(court);
			
			if(player.getCoins() < reservation.updateRental()) {
				throw new AccountOverDrawnException("You don't have enough coins to make the reservation");}
			
			
			reservation.setSportType(court.getSportType());
			reservation.setPlayer(player);
			
			Map<String, Object> result = new HashMap<String, Object>();
			
			// First, validate using JSR annotation
			try {
				System.out.println("HERE: CUSTOM VALIDATION ABOUT TO START" );
				Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);
				System.out.println("HERE: CUSTOM VALIDATION STARTED" );
				
				if(!constraintViolations.isEmpty()) {
				for(ConstraintViolation<Reservation> violation: constraintViolations) {
						String propertyPath = violation.getPropertyPath().toString();
						String message = violation.getMessage();
						binding.rejectValue(propertyPath, "", message);
						System.out.println("HERE: WRONG VALUE:" + propertyPath + "\n" + violation.getInvalidValue().toString());
					}
					return null;
				}
	
				//Then validate using Spring validator
				validTimeFrame.validate(reservation, binding);
				
				// In case of any violation
				if(binding.hasErrors()) {
					return null;
				}
				
				// In case of no violation, we set the reservation's status to "CREATED", then save it
				System.out.println("HERE: RESERVATION " + reservation);
	
				String code = courtCode + "."+ reservation.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
										+"."+reservation.getStartTime().toString();
				reservation.setReservationCode(code);
				reservation.setStatus(ReservationStatus.UNCONFIRMED);
				save(reservation);
				double accountBalance = reservation.getPlayer().getCoins() - reservation.getRental();
				reservation.getPlayer().setCoins(accountBalance);
				player.getReservations().add(reservation);
				
			}catch (Exception e) {
					throw e;
			}
			
			// Everything is fine, saved successfully
			
			result.put("successfulReservation", findById(reservation.getId(), true));
			result.put("updatedPlayer", player);
			return result;
		}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
//	@Async("TaskExecutor_In_serviceConfig")
		public void sendConfirmationMail(Reservation reservation, String serverName) throws Exception {
			try {
			String subject = "HUNG'S SPORT CENTER - Confirmation For Your Reservations";
			String from = "doctythanlong@gmail.com";
			String to = reservation.getPlayer().getEmail();
			String content = "<h3 style=\"color: MidnightBlue\">"
										+ "Reservation information:<br>"
										+ "Court: " + reservation.getCourt().getCode() + "<br>"
										+"Date: " + reservation.getDate() + "<br>"
										+ "From: " + reservation.getStartTime() +  "   To: " + reservation.getEndTime() + "<br>"
										+ "Rental: " + reservation.getRental()+ " $ <br>"
										+ "<a href=\"http://" + serverName + ":8080/MVCJavaConfig/reservation/reservation_confirm?reservation_id="
										+ reservation.getId()+"&player_id="+reservation.getPlayer().getId()
										+"\">Please click this link to confirm your reservation</a>"
										+ "<h3>";
			
			
				System.out.println("HERE: RESERVATION SERVICE Thread name: " + Thread.currentThread().getName());
				
				CompletableFuture<String> result = MailService.sendMail(from, subject, to, null, null, content);
				
				result.thenRun(() -> System.out.println("HERE: MAIL SENT!")); 
				
			} catch (MessagingException e) {
				e.printStackTrace();
				throw e;
			}catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
	
		}
	
	@Override
		public Reservation ConfirmingReservation(int id) throws Exception{	
			Reservation reservation;
			try {
			reservation= findById(id, true);
			}catch (Exception e) {
				throw e;
			}
			if(reservation == null || !reservation.getStatus().equals(ReservationStatus.UNCONFIRMED) ) {return null;}
			reservation.setStatus(ReservationStatus.CONFIRMED);
			return reservation;
		}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
		public LocalDate[] getDateBoundaries() {
			LocalDate today = LocalDate.now();
			LocalDate soonest = today.plusDays(2);
			LocalDate latest = today.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
			return new LocalDate[] {soonest, latest};
		}


	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
		public LocalTime[] getTimeBoundaries() {
			LocalTime openingTime = LocalTime.parse(Reservation.class.getAnnotation(BusinessTime.class).OpeningTime());
			LocalTime closingTime = LocalTime.parse(Reservation.class.getAnnotation(BusinessTime.class).ClosingTime());
			
			return new LocalTime[] {openingTime, closingTime};
		}
	
	
	@Scheduled(cron = "0 0 18 * * ?")
	@Override
		public int deleteUnComfirmed() {
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("date", LocalDate.now().plusDays(1));
			conditions.put("status", ReservationStatus.UNCONFIRMED);
			int rowsUpdated = updateByProperties(conditions, true, "status", ReservationStatus.CANCELLED);
			System.out.println("HERE: " + rowsUpdated+" UPDATED : ");
			
			return rowsUpdated;
		}


	
	@Scheduled(cron = "0 0,30 * * * ?")
	@Override
		public int checkDueReservations() {
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("date", LocalDate.now());
			conditions.put("StartTime", LocalTime.now());
			conditions.put("status", ReservationStatus.CONFIRMED);
//			try {
//				Thread.currentThread().sleep(0);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			int rowsUpdated = updateByProperties(conditions, true, "status", ReservationStatus.ONGOING);
			System.out.println("HERE: " + rowsUpdated+" UPDATED : ");
			return rowsUpdated;
		}
	
	@Scheduled(cron = "0 0,30 * * * ?")
	@Override
		public int checkFinishedReservations() {
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("date", LocalDate.now());
			conditions.put("EndTime", LocalTime.now());
			conditions.put("status", ReservationStatus.ONGOING);
			
			int rowsUpdated = updateByProperties(conditions, true, "status", ReservationStatus.FINISHED);
			System.out.println("HERE: " + rowsUpdated+" UPDATED : ");
			return rowsUpdated;
		}
			
}
