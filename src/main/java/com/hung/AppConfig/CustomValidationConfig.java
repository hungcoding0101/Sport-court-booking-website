package com.hung.AppConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.FilterType;

import com.hung.Service.PlayerService;
import com.hung.Service.PlayerServiceImp;
import com.hung.Service.ReservationService;
import com.hung.Service.ReservationServiceImp;

@Configuration
@ComponentScan(basePackages = {"com.hung.CustomValidator", "com.hung.Service"})
@EnableLoadTimeWeaving
public class CustomValidationConfig {
	
}
