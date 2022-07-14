package com.hung.Util;

public class AccountOverDrawnException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountOverDrawnException(String errorMessage) {
		super(errorMessage);
	}

}
