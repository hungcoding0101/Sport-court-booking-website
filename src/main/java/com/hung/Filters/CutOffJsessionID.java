package com.hung.Filters;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;

@WebFilter(urlPatterns = {"/"}, dispatcherTypes = DispatcherType.REQUEST )
public class CutOffJsessionID extends OncePerRequestFilter{

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
										throws IOException, ServletException {
		
		System.out.println("HERE: CutOffJsession TRIGGERED");
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		if(httpServletRequest.isRequestedSessionIdFromURL()) {
			HttpSession session = httpServletRequest.getSession();
			if(session != null) {
				session.invalidate();
			}
		}
		
		HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(httpServletResponse) {
			@Override
			public String encodeRedirectUrl(String url) {
				return url;
			}
			
			@Override
			public String encodeRedirectURL(String url) {
				return url;
			}
			
			@Override
			public String encodeUrl(String url) {
				return url;
			}
			
			@Override
			public String encodeURL(String url) {
				return url;
			}
		}; 
		
		chain.doFilter(request, responseWrapper);
	}
}
