package model;

import java.time.LocalDateTime;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import model.exceptions.user.InvalidEmailException;
import model.exceptions.user.InvalidFacebookException;
import model.exceptions.user.InvalidNameException;
import model.exceptions.user.InvalidNameLengthException;
import model.exceptions.user.InvalidPasswordException;
import model.exceptions.user.InvalidPasswordLengthException;
import model.exceptions.user.InvalidUsernameException;
import model.exceptions.user.InvalidUsernameLengthException;
import model.exceptions.user.UserException;
import model.utils.Hash;

/**
 * USER POJO Class
 * 
 * @author Home
 *
 */
public class User {

	private static final int MIN_FIRST_NAME_LENGTH = 3;
	private static final int MIN_LAST_NAME_LENGTH = 3;
	private static final int MIN_USERNAME_LENGTH = 3;
	private static final int MIN_PASSWORD_LENGTH = 6;

	private long user_id;
	private String username;
	private String password;
	private String facebook;
	private String email;
	private LocalDateTime date_creation;
	private String first_name;
	private String last_name;

	// all fields
	public User(long user_id, String username, String hashed_password, String facebook, String email,
			LocalDateTime date_creation, String first_name, String last_name) {
		this.user_id = user_id;
		this.username = username;
		this.password = hashed_password;
		this.facebook = facebook;
		this.email = email;
		this.date_creation = date_creation;
		this.first_name = first_name;
		this.last_name = last_name;
	}

	// register user
	public User(String username, String password, String email) throws UserException {
		setUsername(username);
		setPassword(password);
		setEmail(email);
		this.date_creation = LocalDateTime.now();
	}

	private void setEmail(String email) throws InvalidEmailException {
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			throw new InvalidEmailException();
		}
		this.email = email;
	}

	private void setPassword(String password) throws UserException {
		if (password == null || password.isEmpty()) {
			throw new InvalidPasswordException();
		}
		if (password.length() < MIN_PASSWORD_LENGTH) {
			throw new InvalidPasswordLengthException();
		}
		// TODO: Check for strong password
		this.password = Hash.getHashPass(password);
	}

	private void setUsername(String username) throws UserException {
		if (username == null || username.isEmpty()) {
			throw new InvalidUsernameException();
		}
		if (username.length() < MIN_USERNAME_LENGTH) {
			throw new InvalidUsernameLengthException();
		}
		this.username = username;
	}

	public LocalDateTime getDate_creation() {
		return date_creation;
	}

	public String getEmail() {
		return email;
	}

	public String getFacebook() {
		return facebook;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getPassword() {
		return password;
	}

	public long getUser_id() {
		return user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setFacebook(String facebook) throws InvalidFacebookException {
		String fbProfileRegex = "((http|https):\\/\\/)?(www[.])?facebook.com\\/.+";
		if (facebook.matches(fbProfileRegex)) {
			this.facebook = facebook;
		}
		throw new InvalidFacebookException();
	}

	public void setFirst_name(String first_name) throws UserException {
		if (first_name == null || first_name.isEmpty()) {
			throw new InvalidNameException();
		}
		if (first_name.length() < MIN_FIRST_NAME_LENGTH) {
			throw new InvalidNameLengthException();
		}
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) throws UserException {
		if (last_name == null || first_name.isEmpty()) {
			throw new InvalidNameException();
		}
		if (last_name.length() < MIN_LAST_NAME_LENGTH) {
			throw new InvalidNameLengthException();
		}
		this.last_name = last_name;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
}
