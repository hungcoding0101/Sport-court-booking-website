package com.hung.Controllers;

import java.util.InputMismatchException;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hung.Models.Player;
import com.hung.Service.PlayerService;
import com.hung.Service.ReservationService;

@Controller
@RequestMapping(path = "/Player/*")
public class PlayerController {

	
	private PlayerService playerService;
	private ReservationService reservationService;
	
	@Autowired
		public PlayerController( PlayerService playerService, ReservationService reservationService) {
			this.playerService = playerService;
			this.reservationService = reservationService;
		}
	
	@RequestMapping("/")
		public String greeting(){
			return "PlayerProfile";
		}
	
	@RequestMapping("/changingInfo")
		public String changingInfo( @RequestParam(required = false) String newEmail,
													@RequestParam(required =  false) String newPasswords,
													@RequestParam(required =  false) String currentPasswords,
													HttpSession session, Model model) {
		
		Player thisPlayer = (Player) session.getAttribute("verifiedPlayer");
		try {
			playerService.changeInfo(newEmail, newPasswords, currentPasswords, thisPlayer.getId());
			Player updatedPlayer = playerService.VerifyingPlayer(thisPlayer.getName(), newPasswords);
			session.setAttribute("verifiedPlayer", updatedPlayer); 
		}catch (ConstraintViolationException e) {

				Set<ConstraintViolation<?>> violations = ((ConstraintViolationException)e).getConstraintViolations();
				for(ConstraintViolation<?> violation: violations) {
					String violatedField = violation.getPropertyPath().toString().replaceAll("\\.", "");
					String message = violation.getMessage();
					System.out.println("HERE: VIOLATED FIELD: " + violatedField + ": " + message);
					model.addAttribute(violatedField, message);			
			}
			return "PlayerProfile";
		}catch (InputMismatchException e) {
			model.addAttribute("wrongPasswords", e.getMessage());
			return "PlayerProfile";
		}
			model.addAttribute("Message", "Your information has been updated successfully!");
			return "PlayerProfile";
	}
	
	@RequestMapping("/CancellingReservation")
		public String cancellingReservations(@RequestParam(name = "cancelled", required = false) String[] cancelledReservations,
																		HttpSession session) {
		
		if(cancelledReservations != null) {
			Player thisPlayer = (Player) session.getAttribute("verifiedPlayer");
			Player updatedPlayer = playerService.cancellingReservations(cancelledReservations, thisPlayer.getId());
			session.setAttribute("verifiedPlayer", updatedPlayer);  
		}
		
		return "redirect:/Player/";
	}
}
