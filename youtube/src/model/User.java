package model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;

/**
 * USER POJO Class
 * 
 * @author HP
 *
 */
public class User {
	
	private static final String DEFAULT_AVATAR_JPG = "./default_avatar.jpg";
	private static final int MIN_USERNAME_LENGTH = 3;

	private long user_id;

	private String username;
	private String password;
	private String facebook;
	private String email;
	private LocalDateTime date_creation;
	private String first_name;
	private String last_name;
	private String avatar_url;
	private String gender;

	private List<User> followers = new ArrayList<>();

	private List<User> following = new ArrayList<>();

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
	User(long user_id, String username, String password, String facebook, String email, LocalDateTime date_creation,
			String first_name, String last_name, String avatar_url, String gender) throws UserException {
		this.user_id = user_id;
		this.username = username;
		this.password = password;
		this.facebook = facebook;
		this.email = email;
		this.date_creation = date_creation;
		this.first_name = first_name;
		this.last_name = last_name;
		this.avatar_url = avatar_url;
		this.gender = gender;
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
		this.avatar_url = DEFAULT_AVATAR_JPG;
		this.date_creation = LocalDateTime.now();
	}
	public void addFollower(User u) {
		this.followers.add(u);
	}

	public void addFollowing(User u) {
		this.following.add(u);
	}
	public String getAvatar_url() {
		return avatar_url;
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
	
	public List<User> getFollowers() throws SQLException, UserNotFoundException, UserException {
		return this.followers;
	}

	public List<User> getFollowing() throws SQLException, UserNotFoundException, UserException {
		return this.following;
	}
	
	public String getGender() {
		return gender;
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

	private boolean passwordIsStrong(String password) {
		// https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
		String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		if (password.matches(pattern)) {
			return true;
		}
		return false;
	}

	public void removeFollower(User u) {
		this.followers.remove(u);
	}

	public void removeFollowing(User u) {
		this.following.remove(u);
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
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

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setLast_name(String last_name) throws UserException {
		this.last_name = last_name;
	}

	private void setPassword(String password) throws UserException {
		if (passwordIsStrong(password)) {
			this.password = password;
		} else {
			throw new UserException(UserException.PASSWORD_NOT_STRONG);
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

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", username=" + username + ", password=" + password + ", facebook="
				+ facebook + ", email=" + email + ", date_creation=" + date_creation + ", first_name=" + first_name
				+ ", last_name=" + last_name + "]\n";
	}
}