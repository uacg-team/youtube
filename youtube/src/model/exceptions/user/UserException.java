package model.exceptions.user;

public class UserException extends Exception {

	public static final String INVALID_USERNAME_LENGTH = "Invalid username length";
	public static final String INVALID_USERNAME = "Invalid username";
	public static final String INVALID_PASSWORD_LENGTH = "Invalid password";
	public static final String INVALID_PASSWORD = "Invalid password length";
	public static final String INVALID_NAME = "Invalid name";
	public static final String INVALID_NAME_LENGTH = "Invalid name length";
	public static final String INVALID_FACEBOOK = "Invalid facebook";
	public static final String INVALID_EMAIL = "Invalid email";
	public static final String INVALID_ID = "Invalid user id";
	
	public UserException(String message) {
		super(message);
	}
}
