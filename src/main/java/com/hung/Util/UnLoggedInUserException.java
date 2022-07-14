package com.hung.Util;

public class UnLoggedInUserException extends Exception{

	public UnLoggedInUserException(String errorMessage) {
		super(errorMessage);
	}
}
