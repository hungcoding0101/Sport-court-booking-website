package com.hung.CustomValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.hung.DAO.SportTypeDAO;
import com.hung.Models.SportType;
import com.hung.Service.SportTypeService;

public class SportTypeNameUniqueImp implements ConstraintValidator<SportTypeNameUnique, String> {

	@Autowired
	SportTypeService service;
	
	
	public SportTypeNameUniqueImp() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		SportType sp = service.find(value);
		if(sp == null) {return true;}
		return false;
	}

}
