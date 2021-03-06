package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import model.exceptions.tags.TagNotFoundException;
import model.utils.DBConnection;

/**
 * Tag Data Access Object
 * @author HP
 *
 */

public class TagDao {
	private static TagDao instance;
	private static final Connection con = DBConnection.CON1.getConnection();

	static {
		instance = new TagDao();
	}

	private TagDao() {
	}

	public static TagDao getInstance() {
		return instance;
	}

	public boolean existTag(String tag) throws SQLException {
		String sql = "SELECT tag FROM tags WHERE tag = ?";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, tag);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return true;
				}
				return false;
			}
		}
	}

	public Tag getTag(String tag) throws SQLException, TagNotFoundException {
		String sql = "SELECT * FROM tags WHERE tag = ?";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, tag);
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					return new Tag(
							rs.getLong("tag_id"), 
							rs.getString("tag"));
				}
				throw new TagNotFoundException();	
			}
		}
	}

	public void insertVideoTags(Video v) throws SQLException, TagNotFoundException {
		Set<Tag> tags = v.getTags();
		
		String sql = "INSERT INTO videos_has_tags (video_id, tag_id) VALUES (?, ?)";

		for (Tag tag : tags) {
			insertTag(tag.getTag());
			tag = getTag(tag.getTag());
			
			try (PreparedStatement videos_has_tags = con.prepareStatement(sql);) {
				videos_has_tags.setLong(1, v.getVideoId());
				videos_has_tags.setLong(2, tag.getTag_id());
				videos_has_tags.executeUpdate();
			}
		}
	}

	public void insertTag(String tag) throws SQLException {
		if (!existTag(tag)) {
			String sql = "INSERT INTO tags (tag) VALUES (?)";
			try (PreparedStatement ps = con.prepareStatement(sql);) {
				ps.setString(1, tag);
				ps.executeUpdate();
			}
		}
	}

	public void delete(long videoId) throws SQLException {
		//TODO delete from table tags
		String sql = "DELETE FROM videos_has_tags WHERE video_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setLong(1, videoId);
			ps.executeUpdate();
		}
	}
}
