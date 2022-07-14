package com.hung.CustomValidator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.hung.Models.Player;
import com.hung.Service.PlayerService;

@Configurable
public class UniquePlayerNameImp implements ConstraintValidator<UniquePlayerName, String> {

//	@Autowired
	private PlayerService playerService;

	
	public UniquePlayerNameImp() {
	}

	@Autowired
	public UniquePlayerNameImp( PlayerService playerService) {
		this.playerService = playerService;
	}

	public void initialize(UniquePlayerName  constraintAnnotation) {
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return !playerService.checkDuplication("name", value);
	}


}
