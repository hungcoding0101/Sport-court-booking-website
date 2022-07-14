package com.hung.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hung.Models.Player;

@Configuration
public class SessionConfig {

	@Bean
		public HttpSessionListener httpSessionListener() {
			return new SessionListener();
	}
}
