package model;

import java.time.LocalDateTime;

/**
 * USER POJO Class
 * 
 * @author Home
 *
 */
public class User {
	private long user_id;
	private String username;
	private String password;
	private String facebook;
	private String email;
	private LocalDateTime date_creation;
	private String first_name;
	private String last_name;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
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

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setUser_id(long l) {
		this.user_id = l;
	}
}
