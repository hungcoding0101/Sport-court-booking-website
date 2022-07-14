package com.hung.CustomValidator;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.hung.Models.Player;


@Component
public class CombineJSR303AndSpringValidation implements Validator{

	@Autowired
	private javax.validation.Validator beanValidator;
	
	 Class<?> group;
	
	private Set<Validator> SpringValidators;
	
	
	public CombineJSR303AndSpringValidation() {
		SpringValidators = new HashSet<Validator>();
	}
	

	public void setSpringValidators(Set<Validator> springValidators) {
		this.SpringValidators = springValidators;
	}

	
	@Override
	public boolean supports(Class<?> clazz) {
		return Player.class.isAssignableFrom(clazz);
	}

	public void setGroup(Class<?> group) {
		this.group = group;
	}
	
	@Override
	public void validate(Object target, Errors errors) throws ConstraintViolationException{
		
		Set<ConstraintViolation<Object>> constraintViolations = beanValidator.validate(target, group);
		
		if(!constraintViolations.isEmpty()) {
		for(ConstraintViolation<Object> violation: constraintViolations) {
			String propertyPath = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			errors.rejectValue(propertyPath, "", message);
			System.out.println("HERE: CombineJSR303AndSpring: " + propertyPath + "\n"
//			+ violation.getInvalidValue().toString()
			);
		}
		throw new ConstraintViolationException("Error occurred when binding ", constraintViolations);
	}	
		for(Validator validator: SpringValidators) {
			validator.validate(target, errors);
		}
	}

}
