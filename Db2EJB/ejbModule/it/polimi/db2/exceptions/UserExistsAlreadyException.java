package it.polimi.db2.exceptions;

public class UserExistsAlreadyException extends Exception{
	private static final long serialVersionUID = 1L;
	public UserExistsAlreadyException(String message) {
		super(message);
	}
}

