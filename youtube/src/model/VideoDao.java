package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.utils.DBConnection;
import model.utils.DateTimeConvertor;

public class VideoDao {
	private static VideoDao instance;
	private static Connection con = DBConnection.CON1.getConnection();

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
	public static void main(String[] args) throws SQLException {
		VideoDao.getInstance().createVideo(new Video("name", "url", 1, 1, null));
	}

	public void createVideo(Video v) throws SQLException {
		String sql = "INSERT into videos (name, date, location_url, user_id, privacy_id, views) VALUES(?,?,?,?,?,?);";
		PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		ps.setString(1, v.getName());
		String date = DateTimeConvertor.fromLocalDateTimeToSqlDateTime(v.getDate());
		ps.setString(2, date);
		ps.setString(3, v.getLocation_url());
		ps.setLong(4, v.getUser_id());
		ps.setLong(5, v.getPrivacy_id());
		ps.setInt(6, v.getViews());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		v.setVideo_id(rs.getLong(1));
	}

	public void updateVideo(Video v) throws SQLException {
		String sql = "UPDATE videos SET name = '?', description = '?', privacy_id = ? where video_id = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, v.getName());
		ps.setString(2, v.getDescription());
		ps.setLong(3, v.getPrivacy_id());
		ps.setLong(4, v.getUser_id());
		ps.executeUpdate();
	}

	public void deleteVideo(Video v) throws SQLException {
		String sql = "DELETE FROM videos WHERE video_id = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, v.getVideo_id());
		ps.executeQuery();
	}

	public ArrayList<Video> searchVideo(String search) throws SQLException {
		String sql = "SELECT * from videos where name LIKE '%?%';";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, search);
		ResultSet rs = ps.executeQuery(sql);
		ArrayList<Video> videos = new ArrayList<>();
		while (rs.next()) {
			videos.add(new Video(rs.getString("name"), rs.getString("location_url"), rs.getLong("privacy_id"), rs.getLong("user_id"), null));
		}
		return videos;
	}

	public boolean existsVideo(Video v) throws SQLException {
		String sql = "SELECT COUNT(*) FROM videos WHERE video_id = '?';";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, v.getVideo_id());
		ResultSet rs = ps.executeQuery(sql);
		if (rs.next()) {
			return true;
		}
		return false;
	}

}
