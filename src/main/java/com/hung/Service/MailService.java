package com.hung.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.mail.MessagingException;

import org.springframework.scheduling.annotation.AsyncResult;

public interface MailService {
	public CompletableFuture<String> sendMail(String from, String subject, String toAddresses,
										String ccAddresses, String bccAddresses, String body) throws MessagingException ;
}
