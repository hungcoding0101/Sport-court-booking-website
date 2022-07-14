package com.hung.Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hung.AppConfig.SessionListener;
import com.hung.Models.Court;
import com.hung.Models.Player;
import com.hung.Models.Reservation;
import com.hung.Models.ReservationStatus;
import com.hung.Models.SportType;
import com.hung.Service.CourtService;
import com.hung.Service.MailService;
import com.hung.Service.ReservationService;
import com.hung.Service.SportTypeService;
import com.hung.Util.AccountOverDrawnException;

@Controller
@RequestMapping(path = "/reservation/*")
public class ReservationController {
	
	
	private SportTypeService sportService;
	
	
	private CourtService courtService;
	
	
	private ReservationService reservationService;

	private HttpSessionListener  httpSessionListener ;
	

	@Autowired
		public ReservationController(SportTypeService sportService, CourtService courtService,
												ReservationService reservationService, HttpSessionListener httpSessionListener) {
			super();
			this.sportService = sportService;
			this.courtService = courtService;
			this.reservationService = reservationService;
			this.httpSessionListener = httpSessionListener;
		}



	@RequestMapping(path = "/1")
		public String greeting(HttpSession session) {
		
			List<SportType> availableSports = sportService.findAll();
			session.setAttribute("availableSports",availableSports);
			session.setAttribute("previousReservationPage", 1);
				return "Reservation_1";
		}
	
	
	
	@RequestMapping(path = "/2")
		public String reservation_1( String chosen_Sport, HttpSession session) {
			
		Integer lastPage =  (Integer) session.getAttribute("previousReservationPage");
		System.out.println("HERE: last page=" + lastPage);
		if(lastPage == null) {return "redirect:/reservation/1";}
		
		SportType chosenSport = sportService.findEagerly(chosen_Sport);	
			List<Court> availableCourts = chosenSport.getCourts();
			session.setAttribute("chosenSport", chosenSport);
			session.setAttribute("availableCourts", availableCourts);
			session.setAttribute("previousReservationPage", lastPage+1);
			return "Reservation_2"; 
	}
	
	
	@RequestMapping(path = "/3")
		public String reservation_2(String chosenCourt, Model model, HttpSession session, HttpServletRequest request) {
		
		Integer lastPage =  (Integer) session.getAttribute("previousReservationPage");
		System.out.println("HERE: last page=" + lastPage);
		if(lastPage == null || lastPage < 2) {return "redirect:/reservation/1";}
		
			Court thisCourt = courtService.findEagerly(chosenCourt);
			
			Reservation reservation = new Reservation();
			
			LocalDate[] dateBoundaries = reservationService.getDateBoundaries();
			
			LocalTime[] timeBoundaries = reservationService.getTimeBoundaries();
			
			session.setAttribute("dateBoundaries", dateBoundaries);
			session.setAttribute("timeBoundaries", timeBoundaries);
			session.setAttribute("chosenCourt", thisCourt);
			
			model.addAttribute("reservation", reservation);
			session.setAttribute("previousReservationPage", lastPage+1);
			return "Reservation_3";
	}
	
	@RequestMapping(path = "/4")
		public String reservation_3( @ModelAttribute Reservation reservation, BindingResult binding,
											HttpSession session, HttpServletRequest request, Model model) throws Exception {
		
		Integer lastPage =  (Integer) session.getAttribute("previousReservationPage");
		System.out.println("HERE: last page=" + lastPage);
		if(lastPage == null || lastPage < 3) {return "redirect:/reservation/1";}
		
		Reservation successfulReservation;
		Player updatedPlayer;
		Map<String, Object> result;
			try {
				Court chosenCourt = (Court) session.getAttribute("chosenCourt");
				Player thisPlayer = (Player) session.getAttribute("verifiedPlayer");
				result = reservationService.makingReservation(reservation,
																chosenCourt.getCode(), thisPlayer, binding);
			}catch (Exception e) {
				if(e instanceof AccountOverDrawnException) {
					model.addAttribute("errorMessage", e.getMessage());
					return "Reservation_3";
				}
				throw e;
			}	
			
			
			
			if(result == null) {
				return "Reservation_3";
			}
			
			successfulReservation = (Reservation) result.get("successfulReservation");
			updatedPlayer = (Player) result.get("updatedPlayer");
			
			request.setAttribute("successfulReservation",successfulReservation);
			session.setAttribute("verifiedPlayer", updatedPlayer);
			System.out.println("HERE: CONTROLLER Thread name: " + Thread.currentThread().getName());
			String serverName = request.getServerName();
			reservationService.sendConfirmationMail(successfulReservation, serverName);

			
			session.removeAttribute("dateBoundaries");
			session.removeAttribute("timeBoundaries");
			session.removeAttribute("chosenCourt");
			session.removeAttribute("availableSports");
			session.removeAttribute("chosenSport");
			session.removeAttribute("availableCourts");
			session.removeAttribute("previousReservationPage");
			return "ReservationResult";

	}
	

	@RequestMapping(path = "/reservation_confirm", params = {"reservation_id", "player_id"})
		public String confirmingReservation(Integer reservation_id, Integer player_id, Model model) throws Exception {
				
				if(reservation_id == null || player_id == null) {return "redirect:/reservation/1";}
		
				Reservation reservation = reservationService.ConfirmingReservation(reservation_id);
				if(reservation != null) {
					System.out.println("HERE: RESERVATION: " +reservation_id + " || PLAYER: " + player_id);
					List<HttpSession> sessions = ((SessionListener)httpSessionListener).getActiveSessions();
					
					stop_looping_sessions:
					for(int i=0; i < sessions.size(); i++) {
						Player currentPlayer = (Player) sessions.get(i).getAttribute("verifiedPlayer");
						System.out.println("HERE: CURRENT SESSION: " +sessions.get(i));
						if(currentPlayer != null && currentPlayer.getId() == player_id.intValue()) {
							List<Reservation> reservations = currentPlayer.getReservations();
							for(int j=0; j<reservations.size(); j++) {
								if(reservations.get(j).getId() == reservation_id.intValue()) {
									reservations.get(j).setStatus(ReservationStatus.CONFIRMED);
									break stop_looping_sessions;
								}
							}
						}
					}
				}
				model.addAttribute("reservation", reservation);
				return "ReservationConfirmation";	
		}
	
	@RequestMapping(path = "/rollback")
		public String rollback(String target) {
			return target;
		}
	
	@RequestMapping(path = "/cancel")
		public String cancelReservation(HttpSession session) {
		
		session.removeAttribute("dateBoundaries");
		session.removeAttribute("timeBoundaries");
		session.removeAttribute("chosenCourt");
		session.removeAttribute("availableSports");
		session.removeAttribute("chosenSport");
		session.removeAttribute("availableCourts");
		session.removeAttribute("previousReservationPage");
				return "redirect:/";
		}
}
