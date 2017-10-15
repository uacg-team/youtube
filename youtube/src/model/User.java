package model;

import java.time.LocalDateTime;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import model.exceptions.user.UserException;
import model.utils.Hash;

/**
 * USER POJO Class
 * 
 * @author HP
 *
 */
public class User {
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", username=" + username + ", password=" + password + ", facebook="
				+ facebook + ", email=" + email + ", date_creation=" + date_creation + ", first_name=" + first_name
				+ ", last_name=" + last_name + "]";
	}

	private static final int MIN_FIRST_NAME_LENGTH = 3;
	private static final int MIN_LAST_NAME_LENGTH = 3;
	private static final int MIN_USERNAME_LENGTH = 3;
	private static final int MIN_PASSWORD_LENGTH = 3;

	private long user_id;
	private String username;
	private String password;
	private String facebook;
	private String email;
	private LocalDateTime date_creation;
	private String first_name;
	private String last_name;

	/**
	 * Constructor for creating object user with all the fields e.g. get user from
	 * database
	 * 
	 * @param user_id
	 * @param username
	 * @param hashed_password
	 * @param facebook
	 * @param email
	 * @param date_creation
	 * @param first_name
	 * @param last_name
	 * @throws UserException
	 */
	public User(long user_id, String username, String hashed_password, String facebook, String email,
			LocalDateTime date_creation, String first_name, String last_name) throws UserException {
		setUser_id(user_id);
		setUsername(username);
		setPassword(hashed_password);
		setFacebook(facebook);
		setEmail(email);
		this.date_creation = date_creation;
		setFirst_name(first_name);
		setLast_name(last_name);
	}

	/**
	 * Constructor for creating object user with all mandatory the fields e.g.
	 * registering user
	 * 
	 * @param username
	 * @param password
	 * @param email
	 * @throws UserException
	 */
	public User(String username, String password, String email) throws UserException {
		setUsername(username);
		setPassword(password);
		setEmail(email);
		this.date_creation = LocalDateTime.now();
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

	private void setEmail(String email) throws UserException {
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			throw new UserException(UserException.INVALID_EMAIL);
		}
		this.email = email;
	}

	public void setFacebook(String facebook) throws UserException {
		String fbProfileRegex = "((http|https):\\/\\/)?(www[.])?facebook.com\\/.+";
		if (facebook == null) {
			this.facebook = facebook;
			return;
		}

		if (facebook.matches(fbProfileRegex)) {
			this.facebook = facebook;
		}
		throw new UserException(UserException.INVALID_FACEBOOK);
	}

	public void setFirst_name(String first_name) throws UserException {
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) throws UserException {
		this.last_name = last_name;
	}

	private void setPassword(String password) throws UserException {
		if (password == null || password.isEmpty()) {
			throw new UserException(UserException.INVALID_PASSWORD);
		}
		if (password.length() < MIN_PASSWORD_LENGTH) {
			throw new UserException(UserException.INVALID_PASSWORD_LENGTH);
		}
		// TODO: Check for strong password
		if (password.length() == 64) {
			// already hashed
			this.password = password;
		} else {
			this.password = Hash.getHashPass(password);
		}
	}

	public void setUser_id(long user_id) throws UserException {
		if (user_id < 1) {
			throw new UserException(UserException.INVALID_ID);
		}
		this.user_id = user_id;
	}

	private void setUsername(String username) throws UserException {
		if (username == null || username.isEmpty()) {
			throw new UserException(UserException.INVALID_USERNAME);
		}
		if (username.length() < MIN_USERNAME_LENGTH) {
			throw new UserException(UserException.INVALID_USERNAME_LENGTH);
		}
		this.username = username;
	}
}