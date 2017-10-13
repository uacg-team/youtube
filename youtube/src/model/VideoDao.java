package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VideoDao {
	private static VideoDao instance;

	private VideoDao() {
	}

	public static synchronized VideoDao getInstance() {
		if (instance == null) {
			instance = new VideoDao();
		}
		return instance;
	}

	public void insertVideo(Video v) throws SQLException {
//		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"INSERT into videos (name, date, location_url, user_id, privacy_id, views) VALUES(?,?,?,?,?,?);",
				Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, v.getName());
		
		// date filed is DATETIME
		ps.setDate(2, java.sql.Date.valueOf(v.getDate().toLocalDate()));
		ps.setString(3, v.getLocation_url());
		ps.setLong(4, v.getUser_id());
		ps.setLong(4, v.getPrivacy_id());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		v.setVideo_id(rs.getLong(1));
	}

	public boolean existsVideo(Video v) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		Statement stmt = con.createStatement();
		String sql = "SELECT COUNT(*) FROM videos WHERE video_id = '" + v.getVideo_id() + "';";
		ResultSet rs = stmt.executeQuery(sql);

		rs.next();
		int count = rs.getInt(1);

		if (count == 1) {
			return true;
		}
		return false;
	}

	public void addDescription(Video v) throws SQLException {
		if (v.getDescription() == null || v.getDescription().isEmpty()) {
			// throw exception
			return;
		}

		if (!existsVideo(v)) {
			// throw exception
			return;
		}

		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("UPDATE videos SET description = '" + v.getDescription()
				+ "' WHERE video_id = " + v.getVideo_id() + ";");
		ps.executeUpdate();
	}

}
