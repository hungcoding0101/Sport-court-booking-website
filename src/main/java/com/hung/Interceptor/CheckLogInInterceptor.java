package com.hung.Interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hung.Util.UnLoggedInUserException;

@Component
public class CheckLogInInterceptor implements HandlerInterceptor{
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
			throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request; 
		HttpSession session = httpRequest.getSession(false);
		if(session == null || session.getAttribute("verifiedPlayer") == null) {
			throw new UnLoggedInUserException("YOU NEED TO LOG IN OR SIGN UP TO PERFORM THIS ACTIVITY");
		}
		return true;
	}
	
	
}
