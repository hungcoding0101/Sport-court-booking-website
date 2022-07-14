package com.hung.CustomValidator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hung.Models.Player;
import com.hung.Service.PlayerService;


@Component
public class NameDuplicationSpringValidation implements Validator
{
	

	PlayerService playerService;

	@Autowired
	public NameDuplicationSpringValidation(PlayerService playerService) {
		super();
		this.playerService = playerService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Player.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
//		Player player = (Player) target;
//		
//		if(playerService.checkDuplication("name", player.getName())) {
//			errors.rejectValue("name", "Duplication.Player.name.validation");
//		}

	}

}
