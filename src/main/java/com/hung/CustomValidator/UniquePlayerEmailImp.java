package com.hung.CustomValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.hung.Service.PlayerService;

@Configurable
public class UniquePlayerEmailImp implements ConstraintValidator<UniquePlayerEmail, String>{

	PlayerService playerService;

	@Autowired
	public UniquePlayerEmailImp(PlayerService playerService) {
		this.playerService = playerService;
	}

	public void initialize(UniquePlayerEmail  constraintAnnotation) {
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !playerService.checkDuplication("email", value); 
	}

}
