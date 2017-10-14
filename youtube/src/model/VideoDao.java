package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import org.apache.tomcat.jni.OS;

import model.exceptions.tags.TagNotFoundException;
import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;
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
	private static Connection con = DBConnection.CON1.getConnection();

	private VideoDao() {
	}

	public static synchronized VideoDao getInstance() {
		if (instance == null) {
			instance = new VideoDao();
		}
		return instance;
	}

	// OK
	public void createVideo(Video v) throws SQLException, TagNotFoundException {
		con.setAutoCommit(false);
		String sql = "INSERT into videos (name, views , date, location_url, user_id, thumbnail_url, description, privacy_id) VALUES(?,?,?,?,?,?,?,?);";
		PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, v.getName());
		ps.setInt(2, v.getViews());
		String date = DateTimeConvertor.fromLocalDateTimeToSqlDateTime(v.getDate());
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

		TagDao.getInstance().insertVideoTags(v);

		con.commit();
		con.setAutoCommit(true);
	}

	// OK
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
		// FIXME: to be implement
		// TODO: delete all likes for this video_id
		// TODO: delete all videos from playlist
		// TODO: delete all tags for this video_id
		String sql = "DELETE FROM videos WHERE video_id = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, v.getVideo_id());
		ps.executeUpdate();
	}

	// NOT OK
	public ArrayList<Video> searchVideo(String name) throws SQLException {
		// FIXME: Problem with injection
		String sql = "SELECT * FROM videos WHERE name LIKE ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, "%" + name + "%");
		ResultSet rs = ps.executeQuery(sql);
		ArrayList<Video> videos = new ArrayList<>();
		while (rs.next()) {
			videos.add(new Video(rs.getString("name"), rs.getString("location_url"), rs.getLong("privacy_id"),
					rs.getLong("user_id"), null));
		}
		return videos;
	}

	// OK
	public HashSet<Video> getAllVideoOrderByLikes() throws SQLException {
		String sql = "SELECT v.video_id, v.name, v.views, v.date, v.location_url, v.user_id, v.thumbnail_url, v.description, v.privacy_id, SUM(video_likes.isLike) AS likes FROM videos as v JOIN video_likes USING (video_id) GROUP BY video_id ORDER BY SUM(video_likes.isLike) DESC;";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		HashSet<Video> videos = new HashSet<>();
		while (rs.next()) {
			videos.add(
					new Video(
							rs.getLong("video_id"), 
							rs.getString("name"), 
							rs.getInt("views"),
							DateTimeConvertor.fromSqlDateTimeToLocalDateTime(rs.getString("date")),
							rs.getString("location_url"), 
							rs.getLong("user_id"), 
							rs.getString("thumbnail_url"),
							rs.getString("description"), 
							rs.getLong("privacy_id"), 
							getTags(rs.getString("location_url"))));
		}
		return videos;
	}
	
	// OK
	public HashSet<Video> getAllVideoOrderByDate() throws SQLException {
		String sql = "SELECT * FROM videos ORDER BY date DESC;";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		HashSet<Video> videos = new HashSet<>();
		while (rs.next()) {
			videos.add(
					new Video(
							rs.getLong("video_id"), 
							rs.getString("name"), 
							rs.getInt("views"),
							DateTimeConvertor.fromSqlDateTimeToLocalDateTime(rs.getString("date")),
							rs.getString("location_url"), 
							rs.getLong("user_id"), 
							rs.getString("thumbnail_url"),
							rs.getString("description"), 
							rs.getLong("privacy_id"), 
							getTags(rs.getString("location_url"))));
		}
		return videos;
	}

	private HashSet<Tag> getTags(String location_url) throws SQLException {
		HashSet<Tag> tags = new HashSet<>();
		String getTags = "SELECT tags.tag FROM videos_has_tags JOIN tags USING (tag_id) JOIN videos ON (videos_has_tags.video_id = videos.video_id) WHERE location_url = ? ;";
		PreparedStatement ps_tags = con.prepareStatement(getTags);
		ps_tags.setString(1, location_url);
		ResultSet rs1 = ps_tags.executeQuery();
		while (rs1.next()) {
			tags.add(new Tag(rs1.getString("tag")));
		}
		return tags;
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

	// TESTING
	public static void main(String[] args)
			throws SQLException, VideoException, UserNotFoundException, UserException, TagNotFoundException {
		// VideoDao.getInstance().createVideo(new Video("name", "url", 1, 1, null));
		// Video video = VideoDao.getInstance().getVideo("www.somewhere1.com");
		// System.out.println(video);
		// CommentDAO.getInstance().deleteComments(video);
		// VideoDao.getInstance().deleteVideo(video);

		// VideoDao.getInstance().createVideo(new Video("name", "location_url", 1, 1,
		// null));
		// HashSet<Tag> tags = new HashSet<>();
		// tags.add(new Tag("qko"));
		// tags.add(new Tag("cool"));
		// VideoDao.getInstance().createVideo(new Video("you", "tube", 1, 1, tags));
		// System.out.println(VideoDao.getInstance().searchVideo("am").toString());

		// User user = new User("Pesho1", "asdasd", "hristo1@hristo.com");
		// UserDao.getInstance().createUser(user);
		// User u = UserDao.getInstance().getUser("Hristo");
		// System.out.println(VideoDao.getInstance().getVideos(u));
		System.out.println(VideoDao.getInstance().getAllVideoOrderByLikes());
		System.out.println("Good");
	}

	// OK
	public Video getVideo(String location_url) throws VideoNotFoundException, SQLException {
		String sql = "SELECT * FROM videos WHERE location_url = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, location_url);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			Video video = new Video(rs.getLong("video_id"), rs.getString("name"), rs.getInt("views"),
					DateTimeConvertor.fromSqlDateTimeToLocalDateTime(rs.getString("date")),
					rs.getString("location_url"), rs.getLong("user_id"), rs.getString("thumbnail_url"),
					rs.getString("description"), rs.getLong("privacy_id"), getTags(location_url));
			return video;
		}

		throw new VideoNotFoundException(VideoException.NOT_FOUND);
	}

	public ArrayList<Video> getVideos(User u) throws SQLException {
		String sql = "Select * FROM videos WHERE user_id = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, u.getUser_id());
		ResultSet rs = ps.executeQuery();

		ArrayList<Video> allUserVideos = new ArrayList<>();
		while (rs.next()) {

			HashSet<Tag> tags = new HashSet<>();
			String getTags = "SELECT tags.tag FROM videos_has_tags JOIN tags USING (tag_id) WHERE videos_has_tags.video_id = ? ;";
			PreparedStatement ps_tags = con.prepareStatement(getTags);
			ps_tags.setLong(1, rs.getLong("video_id"));
			ResultSet rs1 = ps_tags.executeQuery();
			while (rs1.next()) {
				tags.add(new Tag(rs1.getString("tag")));
			}

			allUserVideos.add(new Video(rs.getLong("video_id"), rs.getString("name"), rs.getInt("views"),
					DateTimeConvertor.fromSqlDateTimeToLocalDateTime(rs.getString("date")),
					rs.getString("location_url"), rs.getLong("user_id"), rs.getString("thumbnail_url"),
					rs.getString("description"), rs.getLong("privacy_id"), tags));
		}

		return allUserVideos;
	}

}
