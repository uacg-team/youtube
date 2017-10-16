package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.exceptions.tags.TagNotFoundException;
import model.exceptions.video.VideoException;
import model.exceptions.video.VideoNotFoundException;
import model.utils.DBConnection;
import model.utils.DateTimeConvertor;

/**
 * Video Data Access Object
 * 
 * @author HP
 *
 */
public class VideoDao {
	private static VideoDao instance;
	private static final Connection con = DBConnection.CON1.getConnection();
	
	static {
		instance = new VideoDao();
	}

	private VideoDao() {
	}

	public static VideoDao getInstance() {
		return instance;
	}

	/**
	 * А method to create a new database entry
	 * @param v - object to be inserted into the database 
	 * @throws SQLException
	 * @throws TagNotFoundException
	 */
	public void createVideo(Video v) throws SQLException, TagNotFoundException {
		con.setAutoCommit(false);
		String sql = "INSERT into videos (name, views , date, location_url, user_id, thumbnail_url, description, privacy_id) VALUES(?,?,?,?,?,?,?,?);";
		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, v.getName());
			ps.setInt(2, v.getViews());
			String date = DateTimeConvertor.ldtToSql(v.getDate());
			ps.setString(3, date);
			ps.setString(4, v.getLocation_url());
			ps.setLong(5, v.getUser_id());
			ps.setString(6, v.getThumbnail_url());
			ps.setString(7, v.getDescription());
			ps.setLong(8, v.getPrivacy_id());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			v.setVideo_id(rs.getLong(1));
		}
		TagDao.getInstance().insertVideoTags(v);

		con.commit();
		con.setAutoCommit(true);
	}

	/**
	 * A method for editing an existing database entry
	 * @param v - object to be edited into the database 
	 * @throws SQLException
	 */
	public void updateVideo(Video v) throws SQLException {
		String sql = "UPDATE videos SET name = ?, description = ?, privacy_id = ? where video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);){
			ps.setString(1, v.getName());
			ps.setString(2, v.getDescription());
			ps.setLong(3, v.getPrivacy_id());
			ps.setLong(4, v.getVideo_id());
			ps.executeUpdate();
		}
	}

	// NOT OK - NOT EVEN CLOSE
	public void deleteVideo(Video v) throws SQLException {
		// FIXME: to be implement
		// TODO: delete all likes for this video_id
		// TODO: delete all videos from playlist
		// TODO: delete all tags for this video_id
		String sql = "DELETE FROM videos WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, v.getVideo_id());
			ps.executeUpdate();
		}
	}

	/**
	 * Search video by word. Searching in the video name.
	 * @param name - searching phrase
	 * @return ArrayList<Video> - all videos whose names match the search phrase 
	 * @throws SQLException
	 * @throws VideoException 
	 */
	public List<Video> searchVideo(String name) throws SQLException, VideoException {
		String sql = "SELECT * FROM videos WHERE LOWER(name) LIKE LOWER(?)";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, "%" + name + "%");
			ResultSet rs = ps.executeQuery();
			List<Video> videos = new ArrayList<>();
			while (rs.next()) {
				videos.add(
						new Video(
								rs.getString("name"), 
								rs.getString("location_url"), 
								rs.getLong("privacy_id"),
								rs.getLong("user_id"), 
								getTags(rs.getString("location_url"))));
			}
			return videos;
		}
	}

	/**
	 * Get all videos sorted by number of likes
	 * @return HashSet<Video> - all videos ordered by number of likes
	 * @throws SQLException
	 */
	public List<Video> getAllVideoOrderByLikes() throws SQLException {
		String sql = "SELECT v.video_id, v.name, v.views, v.date, v.location_url, v.user_id, v.thumbnail_url, v.description, v.privacy_id, SUM(video_likes.isLike) AS likes FROM videos as v JOIN video_likes USING (video_id) GROUP BY video_id ORDER BY SUM(video_likes.isLike) DESC;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();

			List<Video> videos = new ArrayList<>();
			while (rs.next()) {
				videos.add(
						new Video(
								rs.getLong("video_id"), 
								rs.getString("name"), 
								rs.getInt("views"),
								DateTimeConvertor.sqlToLdt(rs.getString("date")),
								rs.getString("location_url"), 
								rs.getLong("user_id"), 
								rs.getString("thumbnail_url"),
								rs.getString("description"), 
								rs.getLong("privacy_id"), 
								getTags(rs.getString("location_url"))));
			}
			return videos;
		}
	}

	/**
	 * Get all videos sorted by date uploaded
	 * @return HashSet<Video> - all videos ordered by date uploaded
	 * @throws SQLException
	 */
	public List<Video> getAllVideoOrderByDate() throws SQLException {
		String sql = "SELECT * FROM videos ORDER BY date DESC;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			List<Video> videos = new ArrayList<>();
			while (rs.next()) {
				videos.add(
						new Video(
								rs.getLong("video_id"), 
								rs.getString("name"), 
								rs.getInt("views"),
								DateTimeConvertor.sqlToLdt(rs.getString("date")),
								rs.getString("location_url"), 
								rs.getLong("user_id"), 
								rs.getString("thumbnail_url"),
								rs.getString("description"), 
								rs.getLong("privacy_id"), 
								getTags(rs.getString("location_url"))));
			}
			return videos;
		}
	}

	/**
	 * Get all tags of the video
	 * @param location_url - the video url
	 * @return HashSet<Tag> - all tags that video has
	 * @throws SQLException
	 */
	private List<Tag> getTags(String location_url) throws SQLException {
		List<Tag> tags = new ArrayList<>();
		String getTags = "SELECT tags.tag FROM videos_has_tags JOIN tags USING (tag_id) JOIN videos ON (videos_has_tags.video_id = videos.video_id) WHERE location_url = ? ;";
		try (PreparedStatement ps_tags = con.prepareStatement(getTags);) {
			ps_tags.setString(1, location_url);
			ResultSet rs1 = ps_tags.executeQuery();
			while (rs1.next()) {
				tags.add(new Tag(rs1.getString("tag")));
			}
			return tags;
		}
	}

	/**
	 * Check if video exist
	 * @param v - check this video
	 * @return
	 * @throws SQLException
	 */
	public boolean existsVideo(long video_id) throws SQLException {
		String sql = "SELECT COUNT(*) FROM videos WHERE video_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, video_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		}
	}

	public Video getVideo(String location_url) throws VideoNotFoundException, SQLException {
		String sql = "SELECT * FROM videos WHERE location_url = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, location_url);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Video video = new Video(
						rs.getLong("video_id"), 
						rs.getString("name"), 
						rs.getInt("views"),
						DateTimeConvertor.sqlToLdt(rs.getString("date")), 
						rs.getString("location_url"),
						rs.getLong("user_id"), 
						rs.getString("thumbnail_url"), 
						rs.getString("description"),
						rs.getLong("privacy_id"), 
						getTags(location_url));
				return video;
			}
			throw new VideoNotFoundException(VideoException.NOT_FOUND);
		}
	}

	public List<Video> getVideos(long user_id) throws SQLException {
		String sql = "Select * FROM videos WHERE user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, user_id);
			ResultSet rs = ps.executeQuery();

			List<Video> allUserVideos = new ArrayList<>();
			while (rs.next()) {
				List<Tag> tags = new ArrayList<>();
				String getTags = "SELECT tags.tag FROM videos_has_tags JOIN tags USING (tag_id) WHERE videos_has_tags.video_id = ? ;";
				PreparedStatement ps_tags = con.prepareStatement(getTags);
				ps_tags.setLong(1, rs.getLong("video_id"));
				ResultSet rs1 = ps_tags.executeQuery();
				while (rs1.next()) {
					tags.add(new Tag(rs1.getString("tag")));
				}

				allUserVideos.add(
						new Video(
								rs.getLong("video_id"), 
								rs.getString("name"), 
								rs.getInt("views"),
								DateTimeConvertor.sqlToLdt(rs.getString("date")),
								rs.getString("location_url"), 
								rs.getLong("user_id"), 
								rs.getString("thumbnail_url"),
								rs.getString("description"), 
								rs.getLong("privacy_id"), 
								tags));
			}
			return allUserVideos;
		}
	}

	public boolean existLikeDislike(long video_id, long user_id) throws SQLException {
		String sql = "Select * FROM video_likes WHERE video_id = ? AND user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, video_id);
			ps.setLong(2, user_id);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}
	}

	public boolean hasLike(long video_id, long user_id) throws SQLException {
		String sql = "Select isLike FROM video_likes WHERE video_id = ? AND user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, video_id);
			ps.setLong(2, user_id);
			ResultSet rs = ps.executeQuery();
			return rs.getBoolean("isLike");
		}
	}

	public void like(long video_id, long user_id) throws SQLException {
		String sql = "UPDATE video_likes SET isLike = 1 WHERE video_id = ? AND user_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, video_id);
			ps.setLong(2, user_id);
			ps.executeUpdate();
		}
		
		
	}

	public void disLike(long video_id, long user_id) throws SQLException {
		String sql = "UPDATE video_likes SET isLike = 0 WHERE video_id = ? AND user_id = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, video_id);
		ps.setLong(2, user_id);
		ps.executeUpdate();
	}


}
