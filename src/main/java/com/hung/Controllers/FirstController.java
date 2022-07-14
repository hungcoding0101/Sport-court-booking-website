package com.hung.Controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.hung.AppConfig.SessionListener;
import com.hung.CustomValidator.CombineJSR303AndSpringValidation;
import com.hung.CustomValidator.LoginConstraints;
import com.hung.CustomValidator.UniquePlayerName;
import com.hung.CustomValidator.NameDuplicationSpringValidation;
import com.hung.CustomValidator.SignUpConstraints;
import com.hung.Models.Player;
import com.hung.Service.PlayerService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@Controller
@RequestMapping(path = { "/"})
 public class FirstController {

	
	private PlayerService playerService;
	
	private CombineJSR303AndSpringValidation combineJSR303AndSpringValidation;
	
	private HttpSessionListener httpSessionListener ;
	
	
	@Autowired
		public FirstController(PlayerService playerService, CombineJSR303AndSpringValidation combineJSR303AndSpringValidation,
												HttpSessionListener httpSessionListener) {
			super();
			this.playerService = playerService;
			this.combineJSR303AndSpringValidation = combineJSR303AndSpringValidation;
			this.httpSessionListener = httpSessionListener;
		}

	
	@GetMapping(path = "/")
	public String showLoginPage(@RequestParam(name = "introducerId", required = false) Integer introducerId,
													@RequestParam(name = "newBieEmail", required = false) String newBieEmail, 
														Model model, HttpServletRequest request, HttpSession session) {
		System.out.println("HERE: IP: " + request.getRemoteAddr() + " || " + request.getHeader("X-FORWARDED-FOR"));
		Player player = new Player();
		Player creatingPlayer = new Player();
		System.out.println("HERE: newBieEmail=" + newBieEmail+ "introducerId=" + introducerId);
		Map<String, Player> map = new HashMap<>();
		map.put("player",player);
		map.put("creatingPlayer", creatingPlayer);
		model.addAllAttributes(map); 
		
		if(newBieEmail != null && introducerId != null) {
			session.setAttribute("newBieEmail", newBieEmail);
			session.setAttribute("introducerId", introducerId);
		}
		
		return "Entrance";
	}
	
	@RequestMapping (path = "/login")
		public String Login( @ModelAttribute("player") @Validated(LoginConstraints.class)  Player player,
										BindingResult binding,@ModelAttribute("creatingPlayer") Player creatingPlayer,
										Model model, HttpSession session, HttpServletRequest request) {
		
		System.out.println("HERE: IP: " + request.getRemoteAddr() + " || " + request.getHeader("X-FORWARDED-FOR"));
			
			session.removeAttribute("errorMessage"); 
			String[] suppressedFields = binding.getSuppressedFields();
			 
			if(suppressedFields.length > 0) {
				throw new RuntimeException("You were trying to add forbiden fields: " +
			StringUtils.arrayToCommaDelimitedString(suppressedFields));
			}	
			
			Player verifiedPlayer = playerService.VerifyingPlayer(player.getName(), player.getPassword());

			if(verifiedPlayer != null) {
				session.setAttribute("verifiedPlayer", verifiedPlayer);
				return "redirect:/home";
			}
			
				model.addAttribute("errorMessage", "Wrong player name or password");
				return "Entrance";

	}
	
	@RequestMapping(path = "/logout")
		public String Logout(HttpSession session) {
			session.invalidate();
			return "redirect:/";		
		}
	
	
	@RequestMapping (path = "/creatingPlayer")
		public String signUp(@ModelAttribute("creatingPlayer")  Player createdPlayer,BindingResult binding,  
											@ModelAttribute("player") Player player,
											HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		
		System.out.println("HERE: IP: " + request.getRemoteAddr() + " || " + request.getHeader("X-FORWARDED-FOR"));
		
		session.removeAttribute("errorMessage");
			int id;
			
			try {
				id = playerService.signUp(createdPlayer);
				createdPlayer.setId(id);
				session.setAttribute("verifiedPlayer", createdPlayer);
				String newBieEmail = (String) session.getAttribute("newBieEmail");
				Integer introducerId = (Integer) session.getAttribute("introducerId");
				
				//check if this newbie has been invited by a current member
				if(newBieEmail != null && introducerId != null && newBieEmail.equals(createdPlayer.getEmail())) {
					Player updatedIntroducer = playerService.rewardingIntroducer(introducerId, createdPlayer.getName(), request.getServerName());
					List<HttpSession> allSessions = ((SessionListener)httpSessionListener).getActiveSessions();

					if(updatedIntroducer  != null) {
						stop_looping_sessions:
						for(int i = 0; i < allSessions.size(); i++) {
							HttpSession thisSession = allSessions.get(i);
							Player thisPlayer = (Player) thisSession.getAttribute("verifiedPlayer");
							
							if(thisPlayer != null && thisPlayer.getId() == introducerId.intValue()) {
									thisSession.setAttribute("verifiedPlayer", updatedIntroducer);
									break stop_looping_sessions;
							}
						}
					}
					session.removeAttribute("introducerId");
					session.removeAttribute("newBieEmail");
				}
			} catch (Exception e) {
				e.printStackTrace();
				
				if(e instanceof ConstraintViolationException) {
					for(ConstraintViolation<?> violation: ((ConstraintViolationException) e).getConstraintViolations()) {
						String propertyPath = violation.getPropertyPath().toString();
						String message = violation.getMessage();
						binding.rejectValue(propertyPath, "", message);
					}
					return "Entrance";
				}
				throw e;
			}
			
			redirectAttributes.addFlashAttribute("justRegisterred", true);
			return "redirect:/home";	
	}
	
	@PostMapping("/Upload")
		public void Upload(HttpServletRequest request, @RequestParam("name") String name,
				@RequestParam("pic") MultipartFile file) {
	
			String originalName = file.getOriginalFilename();
			File path = new File("D:\\Config\\" + name+"."+originalName.substring(originalName.lastIndexOf(".")+1));
	
			try {
				file.transferTo(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	@InitBinder
	public void restrictBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
//		binder.setValidator(combineJSR303AndSpringValidation);
	}
}
