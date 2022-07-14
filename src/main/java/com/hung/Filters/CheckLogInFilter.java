package com.hung.Filters;

import java.io.IOException;

import javax.annotation.Priority;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hung.Util.UnLoggedInUserException;

@WebFilter(urlPatterns = {"/reservation/*", "/Invitation/*", "/Player/*"})
@Component
public class CheckLogInFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("HERE: CHECK LOG IN FILTER GOT TRIGGERED"); 
		
		
		HttpServletRequest httpRequest = (HttpServletRequest) request; 
		HttpSession session = httpRequest.getSession(true);
		
		if(httpRequest.getRequestURI().matches("/MVCJavaConfig/reservation/reservation_confirm")) {
			chain.doFilter(request, response);
		}
		
		else {

			if(session == null || session.getAttribute("verifiedPlayer") == null) {
				session.setAttribute("errorMessage", "YOU MUST LOG IN OR SIGN UP TO PERFORM THIS ACTIVITY");
				HttpServletResponse httpServletResponse = (HttpServletResponse) response;
				httpServletResponse.sendRedirect(httpRequest.getContextPath()+"/");
				return;
			}
			
			chain.doFilter(request, response);
		}
	}

}
