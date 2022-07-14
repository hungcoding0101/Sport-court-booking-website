package com.hung.Controllers;

import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hung.Models.Player;
import com.hung.Service.PlayerService;

@Controller
@RequestMapping(path = "/Invitation/*")
public class InvitationController {

	private PlayerService playerService;
	
	@Autowired
	public InvitationController(PlayerService playerService) {
		super();
		this.playerService = playerService;
	}

	@RequestMapping(path = "/")
	public String showInvitationPage() {
		return "MakingInvitation";
	}
	
	@RequestMapping(path = "/Inviting")
		public String Inviting(String newBieEmail, String introducerNickname,
								HttpServletRequest request,HttpSession session, Model model) throws Exception {
			
		Player introducer = (Player) session.getAttribute("verifiedPlayer");
			int introducerId = introducer.getId();
			String serverName = request.getServerName();
			
			try {
				
			playerService.sendInvitationMail(newBieEmail, introducerId, introducerNickname, serverName);
			
			}catch (ConstraintViolationException e) {
				Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
				for(ConstraintViolation<?> violation: violations) {
					String violatedField = violation.getPropertyPath().toString().replaceAll("\\.", "");
					String message = violation.getMessage();
					System.out.println("HERE: VIOLATED FIELD: " + violatedField + ": " + message);
					model.addAttribute(violatedField, message);				
				}
				return "MakingInvitation";
			}catch(Exception e) {
				throw e;
			}
			
			return "AfterMakingInvitation";
		}
	

}
