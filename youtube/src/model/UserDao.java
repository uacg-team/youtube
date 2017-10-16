package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
	private static final Connection con = DBConnection.CON1.getConnection();
	
	static {
		instance = new UserDao();
	}

	private UserDao() {
	}
	
	public static UserDao getInstance() {
		return instance;
	}

	public void createUser(User u) throws SQLException, UserException {
		String sql = "INSERT INTO users (username, password, email, date_creation) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getEmail());
			ps.setString(4, DateTimeConvertor.ldtToSql(u.getDate_creation()));
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			u.setUser_id(rs.getLong(1));
		}
	}

	public List<User> searchUser(String username) throws SQLException, UserException {
		String sql = "SELECT * FROM users WHERE LOWER(username) LIKE LOWER(?)";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, "%" + username + "%");
			ResultSet rs = ps.executeQuery();
			List<User> users = new ArrayList<>();
			while (rs.next()) {
				users.add(
						new User(
								rs.getLong("user_id"), 
								rs.getString("username"), 
								rs.getString("password"),
								rs.getString("facebook"), 
								rs.getString("email"),
								DateTimeConvertor.sqlToLdt(rs.getString("date_creation")), 
								rs.getString("first_name"),
								rs.getString("last_name"),
								rs.getString("avatar_url"),
								rs.getString("gender")));
			}
			return users;
		}
	}

	public void updateUser(User u) throws SQLException, UserException, UserNotFoundException {
		String sql = "UPDATE users SET password = ?, facebook = ?, email = ?, first_name = ?, last_name = ?, avatar_url = ?, gender = ?  WHERE user_id = ? ;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, u.getPassword());
			ps.setString(2, u.getFacebook());
			ps.setString(3, u.getEmail());
			ps.setString(4, u.getFirst_name());
			ps.setString(5, u.getLast_name());
			ps.setString(6, u.getAvatar_url());
			ps.setString(7, u.getGender());
			ps.setLong(8, u.getUser_id());
			int affectedRows = ps.executeUpdate();

			if (affectedRows > 1) {
				throw new UserException(UserException.MORE_THAN_ONE_USER_AFFECTED);
			}
			if (affectedRows == 0) {
				throw new UserNotFoundException();
			}
		}
	}

	public boolean existsUser(String username) throws SQLException {
		String sql = "SELECT COUNT(*) FROM users WHERE username = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		}
	}

	public User getUser(String username) throws SQLException, UserNotFoundException, UserException {
		String sql = "SELECT * FROM users WHERE username = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new User(
						rs.getLong("user_id"), 
						rs.getString("username"), 
						rs.getString("password"),
						rs.getString("facebook"), 
						rs.getString("email"),
						DateTimeConvertor.sqlToLdt(rs.getString("date_creation")), 
						rs.getString("first_name"),
						rs.getString("last_name"),
						rs.getString("avatar_url"),
						rs.getString("gender"));
			} else {
				throw new UserNotFoundException();
			}
		}
	}

	public boolean isFollowing(long user_id, long following_id) throws SQLException {
		String sql = "SELECT * FROM users_follow_users WHERE user_id = ? AND follower_id = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, user_id);
		ps.setLong(2, following_id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return true;
		}
		return false;
	}

	public void followUser(long user_id, long follower_id) throws SQLException {
		String follow = "INSERT INTO users_follow_users (user_id,follower_id) VALUES(?,?);";
		try (PreparedStatement ps = con.prepareStatement(follow);) {
			ps.setLong(1, user_id);
			ps.setLong(2, follower_id);
			ps.executeUpdate();
		}
	}

	public void unfollowUser(long user_id, long following_id) throws SQLException {
		String unfollow = "DELETE FROM users_follow_users WHERE user_id = ? AND follower_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(unfollow);) {
			ps.setLong(1, user_id);
			ps.setLong(2, following_id);
			ps.executeUpdate();
		}
	}

	public List<User> getFollowers(long user_id) throws SQLException, UserNotFoundException, UserException {
		String unfollow = "SELECT users.* FROM users_follow_users AS follower JOIN users ON (follower.follower_id = users.user_id) WHERE follower.user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(unfollow);) {
			ps.setLong(1, user_id);
			ResultSet rs = ps.executeQuery();
			List<User> followers = new ArrayList<>();
			while (rs.next()) {
				followers.add(
						new User(
								rs.getLong("user_id"), 
								rs.getString("username"), 
								rs.getString("password"),
								rs.getString("facebook"), 
								rs.getString("email"),
								DateTimeConvertor.sqlToLdt(rs.getString("date_creation")), 
								rs.getString("first_name"),
								rs.getString("last_name"),
								rs.getString("avatar_url"),
								rs.getString("gender")));
			}
			return followers;
		}
	}

	public List<User> getFollowing(long user_id) throws SQLException, UserNotFoundException, UserException {
		String unfollow = "SELECT users.* FROM users_follow_users AS follower JOIN users ON (follower.user_id = users.user_id) WHERE follower.follower_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(unfollow);) {
			ps.setLong(1, user_id);
			ResultSet rs = ps.executeQuery();
			List<User> following = new ArrayList<>();
			while (rs.next()) {
				following.add(
						new User(
								rs.getLong("user_id"), 
								rs.getString("username"),
								rs.getString("password"),
								rs.getString("facebook"), 
								rs.getString("email"),
								DateTimeConvertor.sqlToLdt(rs.getString("date_creation")), 
								rs.getString("first_name"),
								rs.getString("last_name"),
								rs.getString("avatar_url"),
								rs.getString("gender")));
			}
			return following;
		}
	}

}