package com.hung.Interceptor;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class Interceptor implements HandlerInterceptor{
	private static final Logger LOG = Logger.getLogger(Interceptor.class);
	
	@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
				throws Exception {
			long StartTime = System.currentTimeMillis();
			request.setAttribute("startTime", StartTime);
			
			if(request.getRequestURI().contains("/resources")) {
				System.out.println("HERE: THIS IS STATIC RESOURCE REQUEST");
				return true;
			}
			
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setDateHeader ("Expires", System.currentTimeMillis() - 1000000);
			
			return true;
		}
	
	@Override
		 public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
				 ModelAndView modelAndView) throws Exception {
		
			String queryString = (request.getQueryString() == null) ? "" : "?" + request.getQueryString();
			String path = request.getRequestURL() + queryString;
			
			Long startTime = (Long) request.getAttribute("startTime");
			
			Long endTime = System.currentTimeMillis();
			
			LOG.info(String.format("%s mili seconds to process the request %s", (endTime - startTime), path));

			System.out.println("\n");
		}
	
	@Override
	 public void afterCompletion(HttpServletRequest request,HttpServletResponse response, 
			 Object handler, Exception ex) throws Exception {
		
		}
}
