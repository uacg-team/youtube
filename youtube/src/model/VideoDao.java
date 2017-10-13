package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import model.exceptions.video.VideoException;
import model.exceptions.video.VideoNotFoundException;
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

	//OK
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

	//OK
	public void updateVideo(Video v) throws SQLException {
		String sql = "UPDATE videos SET name = ?, description = ?, privacy_id = ? where video_id = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, v.getName());
		ps.setString(2, v.getDescription());
		ps.setLong(3, v.getPrivacy_id());
		ps.setLong(4, v.getUser_id());
		ps.executeUpdate();
	}

	// NOT OK - NOT EVEN CLOSE
	public void deleteVideo(Video v) throws SQLException {
		// TODO: to be implement
		// TODO: delete all likes for this video_id
		// TODO: delete all videos from playlist
		// TODO: delete all tags for this video_id
		String sql = "DELETE FROM videos WHERE video_id = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, v.getVideo_id());
		ps.executeUpdate();
	}

	// NOT OK
	public ArrayList<Video> searchVideo(String search) throws SQLException {
		// FIXME: Problem with injection
		String sql = "SELECT * FROM videos WHERE name LIKE ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, "%" + search + "%");
		ResultSet rs = ps.executeQuery(sql);
		ArrayList<Video> videos = new ArrayList<>();
		while (rs.next()) {
			videos.add(new Video(rs.getString("name"), rs.getString("location_url"), rs.getLong("privacy_id"),
					rs.getLong("user_id"), null));
		}
		return videos;
	}

	// NOT OK
	public boolean existsVideo(Video v) throws SQLException {
		// FIXME Problem with injection
		String sql = "SELECT COUNT(*) FROM videos WHERE video_id = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, v.getVideo_id());
		ResultSet rs = ps.executeQuery(sql);
		if (rs.next()) {
			return true;
		}
		return false;
	}

	//TESTING
	public static void main(String[] args) throws SQLException, VideoException {
		// VideoDao.getInstance().createVideo(new Video("name", "url", 1, 1, null));
		// Video video = VideoDao.getInstance().getVideo("www.somewhere1.com");
		// System.out.println(video);
		//CommentDAO.getInstance().deleteComments(video);
		//VideoDao.getInstance().deleteVideo(video);
		
		//VideoDao.getInstance().createVideo(new Video("name", "location_url", 1, 1, null));
		//VideoDao.getInstance().updateVideo(video);
		// System.out.println(VideoDao.getInstance().searchVideo("am").toString());

		System.out.println("Good");
	}

	// OK
	public Video getVideo(String location_url) throws VideoNotFoundException, SQLException {
		HashSet<Tag> tags = new HashSet<>();
		String getTags = "SELECT tags.tag FROM videos_has_tags JOIN tags USING (tag_id) JOIN videos ON (videos_has_tags.video_id = videos.video_id) WHERE location_url = ? ;";
		PreparedStatement ps_tags = con.prepareStatement(getTags);
		ps_tags.setString(1, location_url);
		ResultSet rs1 = ps_tags.executeQuery();
		while(rs1.next()) {
			tags.add(new Tag(rs1.getString("tag")));
		}
		
		String sql = "SELECT * FROM videos WHERE location_url = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, location_url);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			Video video = new Video(rs.getLong("video_id"), rs.getString("name"), rs.getInt("views"),
					DateTimeConvertor.fromSqlDateTimeToLocalDateTime(rs.getString("date")),
					rs.getString("location_url"), rs.getLong("user_id"), rs.getString("thumbnail_url"),
					rs.getString("description"), rs.getLong("privacy_id"), tags);
			return video;
		}

		throw new VideoNotFoundException(VideoException.NOT_FOUND);
	}

}
