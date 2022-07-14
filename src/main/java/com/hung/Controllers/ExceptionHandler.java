package com.hung.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.el.MethodNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.jasper.JasperException;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hung.Models.Reservation;
import com.hung.Util.UnLoggedInUserException;


@ControllerAdvice
public class ExceptionHandler{
		
		@org.springframework.web.bind.annotation.ExceptionHandler
			public String DefaultHandler(Exception e, Model model, HandlerMethod method) {
				e.printStackTrace();
				model.addAttribute("message", e.getLocalizedMessage());
				return "GeneralError";
			}
		
		@org.springframework.web.bind.annotation.ExceptionHandler(UnLoggedInUserException.class)
			public String UnLoggedInUserExceptionHandler(UnLoggedInUserException e, HandlerMethod method, RedirectAttributes redirect) {
				redirect.addFlashAttribute 	("errorMessage", e.getMessage());
				return "redirect:/entrance/";
		}
		
		
		@org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
			public String ConstraintViolationExceptionHandler(ConstraintViolationException e, Model model, HandlerMethod method) {
				
				Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	
				model.addAttribute("violations", violations);
				return "ConstrainVaiolationError";
			}
			
		

}
