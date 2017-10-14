package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import model.exceptions.tags.TagNotFoundException;
import model.utils.DBConnection;

/**
 * Tag Data Access Object
 * @author HP
 *
 */

public class TagDao {
	private static TagDao instance;
	private static Connection con = DBConnection.CON1.getConnection();

	private TagDao() {
	}

	public static synchronized TagDao getInstance() {
		if (instance == null) {
			instance = new TagDao();
		}
		return instance;
	}

	public static void main(String[] args) throws SQLException {
		System.out.println(TagDao.getInstance().existTag(new Tag("tag1")));
		System.out.println("excellent");
	}

	// OK
	public boolean existTag(Tag t) throws SQLException {
		String sql = "SELECT tag FROM tags WHERE tag = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, t.getTag());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return true;
		}
		return false;
	}

	public Tag getTag(String tag) throws SQLException, TagNotFoundException {
		String sql = "SELECT * FROM tags WHERE tag = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, tag);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return new Tag(rs.getLong("tag_id"), rs.getString("tag"));
		}
		throw new TagNotFoundException();
	}

	public void insertVideoTags(Video v) throws SQLException, TagNotFoundException {
		HashSet<Tag> tags = v.getTags();
		
		String insert_into_videos_has_tags = "INSERT INTO videos_has_tags (video_id, tag_id) values (?, ?)";

		for (Tag tag : tags) {
			insertTag(tag);
			tag = getTag(tag.getTag());
			
			PreparedStatement videos_has_tags = con.prepareStatement(insert_into_videos_has_tags);
			videos_has_tags.setLong(1, v.getVideo_id());
			videos_has_tags.setLong(2, tag.getTag_id());
			videos_has_tags.executeUpdate();
		}
	}

	public void insertTag(Tag t) throws SQLException {
		if (!existTag(t)) {
			String insertTags = "INSERT INTO tags (tag) values (?)";
			PreparedStatement ps = con.prepareStatement(insertTags);
			ps.setString(1, t.getTag());
			ps.executeUpdate();
		}
	}
}
