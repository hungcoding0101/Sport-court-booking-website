package com.hung.AppConfig;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.hung.AppConfig.StartUPDestroy;
import com.hung.Filters.CheckLogInFilter;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[] {RepositoryConfig.class, ServiceConfig.class, SecurityConfig.class, CustomValidationConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[] {mvcConfig.class, CustomValidationConfig.class, SessionConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return new String[] {"/"};
	}
	
	
	
//	@Override
//	protected Filter[] getServletFilters() {
//		return new Filter[] {new DemoFilter()};
//	}
}
