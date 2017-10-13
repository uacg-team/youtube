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

	// TESTING
	public static void main(String[] args) throws UserException, SQLException, UserNotFoundException {
		//User u = new User("Ivan", "ivan4oouu", "Ivan@Ivan.ivan");
		// UserDao.getInstance().insertUser(u);
		// User user = UserDao.getInstance().getUser("Ivan");
		// user.setFirst_name("Ivan");
		// user.setLast_name("Ivanov");
		// UserDao.getInstance().updateUser(user);
		// User u = UserDao.getInstance().getUser("Ivan");
		// System.out.println(u);
		//System.out.println(UserDao.getInstance().existsUser(u));

		System.out.println("Excelent");
	}

	// OK
	public void insertUser(User u) throws SQLException, UserException {
		String sql = "INSERT INTO users (username, password, email, date_creation) VALUES (?, ?, ?, ?)";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, u.getUsername());
		ps.setString(2, u.getPassword());
		ps.setString(3, u.getEmail());
		ps.setString(4, DateTimeConvertor.fromLocalDateTimeToSqlDateTime(u.getDate_creation()));
		ps.executeUpdate();
	}

	// OK
	public void updateUser(User u) throws SQLException, UserException, UserNotFoundException {
		String sql = "UPDATE users SET password = ?, facebook = ?, email = ?, first_name = ?, last_name = ? WHERE user_id = ? ;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, u.getPassword());
		ps.setString(2, u.getFacebook());
		ps.setString(3, u.getEmail());
		ps.setString(4, u.getFirst_name());
		ps.setString(5, u.getLast_name());
		ps.setLong(6, u.getUser_id());
		int affectedRows = ps.executeUpdate();

		if (affectedRows > 1) {
			throw new UserException(UserException.MORE_THAN_ONE_USER_AFFECTED);
		}
		if (affectedRows == 0) {
			throw new UserNotFoundException();
		}
	}

	// NOT OK
	public boolean existsUser(User u) throws SQLException {
		String sql = "SELECT COUNT(*) FROM users WHERE username = ? ;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, u.getUsername());
		ResultSet rs = ps.executeQuery(sql);
		if (rs.next()) {
			return true;
		}
		return false;

	}

	//ОК	
	public User getUser(String username) throws SQLException, UserNotFoundException {
		String sql = "SELECT * FROM users WHERE username = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
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
		throw new UserNotFoundException();
	}

}
