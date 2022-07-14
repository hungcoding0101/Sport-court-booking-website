package com.hung.AppConfig;


import java.util.Properties;
import java.util.concurrent.Executor;

import javax.mail.internet.MimeMessage;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;


@Configuration
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@PropertySource("classpath:service.properties")
@ComponentScan(basePackages = {"com.hung.Service", "com.hung.CustomValidator"})
public class ServiceConfig implements AsyncConfigurer{
	
	@Autowired
	 private Environment environment;

	@Bean
		public Validator validator() {
			return new LocalValidatorFactoryBean();
		}
	
	@Bean
		public MethodValidationPostProcessor methodValidationPostProcessor() {
			return new MethodValidationPostProcessor();
		}
	
	@Bean(name = "threadPoolTaskScheduler")
		public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(5); 
		return scheduler;
	}
	
	@Bean(name = "TaskExecutor_In_serviceConfig")
		public Executor threadPoolTaskExecutor() {
			ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setThreadGroupName("mvc_Async");
			return executor;
	}

	@Bean
	public JavaMailSenderImpl gmailMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername(environment.getRequiredProperty("email"));
		mailSender.setPassword(environment.getRequiredProperty("email_pass"));
		
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false"); 
        props.put("spring.mail.properties.mail.smtp.ssl.enable", "true");
        
        return mailSender;
}
	
	
	
}
