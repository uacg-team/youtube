package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;
import model.utils.DBConnection;
import model.utils.DateTimeConvertor;

/**
 * User Data Access Object
 * 
 * @author HP
 *
 */
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
		// User u = new User("Ivan", "ivan4oouu", "Ivan@Ivan.ivan");
		// UserDao.getInstance().insertUser(u);
		// User user = UserDao.getInstance().getUser("Ivan");
		// user.setFirst_name("Ivan");
		// user.setLast_name("Ivanov");
		// UserDao.getInstance().updateUser(user);
		// User u = UserDao.getInstance().getUser("Ivan");
		// System.out.println(u);
		// System.out.println(UserDao.getInstance().existsUser(u));

		// User u1 = UserDao.getInstance().getUser("Pesho");
		// User u2 = UserDao.getInstance().getUser("Velichko");
		// UserDao.getInstance().followUnfollowUser(u1, u2);
		//System.out.println(UserDao.getInstance().loginUser(new User("Hristo", "Penev", "hristo@penev.bg")));
		System.out.println("Excelent");
	}

	// OK
	public void createUser(User u) throws SQLException, UserException {
		String sql = "INSERT INTO users (username, password, email, date_creation) VALUES (?, ?, ?, ?)";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, u.getUsername());
		ps.setString(2, u.getPassword());
		ps.setString(3, u.getEmail());
		ps.setString(4, DateTimeConvertor.fromLocalDateTimeToSqlDateTime(u.getDate_creation()));
		ps.executeUpdate();
	}
		
	public boolean loginUser(User u) throws SQLException, UserNotFoundException, UserException {
		if (!existsUser(u)) {
			return false;
		}
		
		User userDB = getUser(u.getUsername());
		if (u.getPassword().equals(userDB.getPassword())) {
			return true;
		}
		
		return false;
	}
	
	// OK
	public ArrayList<User> searchUser(String username) throws SQLException, UserException {
		String sql = "SELECT * FROM users WHERE username LIKE ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, "%" + username + "%");
		ResultSet rs = ps.executeQuery();
		
		ArrayList<User> users = new ArrayList<>();
		while (rs.next()) {
			//TODO: not sure should i take all info?
			users.add(new User(
					rs.getLong("user_id"), 
					rs.getString("username"),
					rs.getString("password"), 
					rs.getString("facebook"), 
					rs.getString("email"), 
					DateTimeConvertor.fromSqlDateTimeToLocalDateTime(rs.getString("date_creation")),
					rs.getString("first_name"), 
					rs.getString("last_name")));
		}
		return users;
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

	// OK
	public boolean existsUser(User u) throws SQLException {
		String sql = "SELECT COUNT(*) FROM users WHERE username = ? ;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, u.getUsername());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return true;
		}
		return false;

	}

	// ОК
	public User getUser(String username) throws SQLException, UserNotFoundException, UserException {
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

	// OK
	public boolean hasFollowing(User user, User following) throws SQLException {
		String sql = "SELECT * FROM users_follow_users WHERE user_id = ? AND follower_id = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, user.getUser_id());
		ps.setLong(2, following.getUser_id());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return true;
		}
		return false;
	}

	// OK
	public void followUnfollowUser(User user, User following) throws SQLException {
		if (hasFollowing(user, following)) {
			unfollowUser(user, following);
		} else {
			followUser(user, following);
		}
	}

	// OK
	private void followUser(User user, User following) throws SQLException {
		String follow = "INSERT INTO users_follow_users (user_id,follower_id) VALUES(?,?);";
		PreparedStatement ps = con.prepareStatement(follow);
		ps.setLong(1, user.getUser_id());
		ps.setLong(2, following.getUser_id());
		ps.executeUpdate();
	}

	// OK
	private void unfollowUser(User user, User following) throws SQLException {
		String unfollow = "DELETE FROM users_follow_users WHERE user_id = ? AND follower_id = ?;";
		PreparedStatement ps = con.prepareStatement(unfollow);
		ps.setLong(1, user.getUser_id());
		ps.setLong(2, following.getUser_id());
		ps.executeUpdate();
	}

}
