package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;
import model.utils.DBConnection;
import model.utils.DateTimeConvertor;

public class UserDao {

	private static UserDao instance;
	private static Connection con = DBConnection.CON1.getConnection();

	private UserDao() {
	}

	public static synchronized UserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();
		}
		return instance;
	}

	public void insertUser(User u) throws SQLException, UserException {
		PreparedStatement ps = con.prepareStatement(
				"INSERT INTO users (username, password, email, date_creation) VALUES (?, ?, ?,?)",
				Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, u.getUsername());
		ps.setString(2, u.getPassword());
		ps.setString(3, u.getEmail());
		ps.setDate(4, java.sql.Date.valueOf(u.getDate_creation().toLocalDate()));
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		u.setUser_id(rs.getLong(1));
	}

	public void editUser(User u) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"UPDATE users SET facebook = '" + u.getFacebook() + "' WHERE user_id = " + u.getUser_id() + ";");
		ps.executeUpdate();
	}

	public boolean existsUser(User u) throws SQLException {
		Statement stmt = con.createStatement();
		String sql = "SELECT COUNT(*) as number FROM users WHERE username = '" + u.getUsername() + "';";
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			return true;
		}
		return false;

	}

	/**
	 * Get user by username
	 * 
	 * @param username
	 * @return user from database
	 * @throws SQLException
	 * @throws UserNotFoundException
	 *             - if user do not exist throws exception
	 */
	public User getUser(String username) throws SQLException, UserNotFoundException {
		Statement stmt = con.createStatement();
		String sql = "SELECT * FROM users WHERE username = '" + username + "';";
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			String name = rs.getString("username");
			if (name.equals(username)) {
				return new User(
						rs.getLong("user_id"), 
						rs.getString("username"), 
						rs.getString("password"),
						rs.getString("facebook"), 
						rs.getString("email"), 
						DateTimeConvertor.fromSqlDateTimeToLocalDateTime(rs.getString("date_creation")),  
						rs.getString("first_name"),
						rs.getString("last_name"));
			}
		}
		throw new UserNotFoundException();
	}

}
