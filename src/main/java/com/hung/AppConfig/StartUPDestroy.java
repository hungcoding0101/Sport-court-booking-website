package com.hung.AppConfig;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;



public class StartUPDestroy extends ContextLoaderListener{
	
	private static final Logger LOG = Logger.getLogger(StartUPDestroy.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {		
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();
	      final Enumeration<Driver> drivers = DriverManager.getDrivers();
	      while (drivers.hasMoreElements()) {
	         final Driver driver = drivers.nextElement();
	         // We deregister only the classes loaded by this application's classloader
	         if (driver.getClass().getClassLoader() == cl) {
	        	// This driver was registered by the webapp's ClassLoader, so deregister it:
	            try {
	            	LOG.info("Deregistering JDBC driver {}");
	               DriverManager.deregisterDriver(driver);
	            } catch (SQLException e) {
	               event.getServletContext().log("JDBC Driver deregistration problem.", e);
	            }
	         }
	         
	         else {
	        	 // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
	        	 LOG.trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader");
	         }
	      }
	}	

}
