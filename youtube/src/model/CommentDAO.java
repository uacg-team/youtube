package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import model.exceptions.comments.CommentException;
import model.exceptions.comments.CommentNotFoundException;
import model.exceptions.user.UserNotFoundException;
import model.exceptions.video.VideoNotFoundException;
import model.utils.DBConnection;
import model.utils.DateTimeConvertor;

public class CommentDAO {
	private static final Connection con = DBConnection.CON1.getConnection();
	private static CommentDAO instance;
	static {
		instance = new CommentDAO();
	}

	private CommentDAO() {
	}

	public static CommentDAO getInstance() {
		return instance;
	}

	public void getComment(String text, LocalDateTime datetime, User user, Video video, Comment replayTo) {

	}

	/**
	 * @param c
	 *            must have text,date,video_id,user_id;
	 * @throws SQLException
	 * @throws CommentException
	 */
	public void createComment(Comment c) throws SQLException, CommentException {
		String sql = "insert into comments (text, date,video_id, user_id, replay_id) values(?,?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, c.getText());
		ps.setString(2, DateTimeConvertor.fromLocalDateTimeToSqlDateTime(c.getDate()));
		ps.setLong(3, c.getVideo_id());
		ps.setLong(4, c.getUser_id());
		if (c.getReplayTo_id() != 0) {
			ps.setLong(5, c.getReplayTo_id());
		} else {
			ps.setString(5, null);
		}
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		long id = rs.getLong(1);
		c.setId(id);

		ps.close();
		rs.close();
	}

	/**
	 * @param c-comment
	 * @throws SQLException
	 * @throws CommentNotFoundException - if cant find comment
	 * @throws CommentException -if cant update in db
	 */
	public void updateComment(Comment c) throws SQLException, CommentException {
		String foundComment = "select comment_id from comments where comment_id=?;";
		PreparedStatement ps = con.prepareStatement(foundComment);
		ps.setLong(1, c.getId());
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			ps.close();
			rs.close();
			throw new CommentNotFoundException();
		}
		ps.close();
		rs.close();
		String sql = "UPDATE comments SET text=?, date=STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s.%f'),video_id=?, user_id=?, replay_id=? WHERE comment_id="
				+ c.getId();
		ps = con.prepareStatement(sql);
		ps.setString(1, c.getText());
		ps.setString(2, DateTimeConvertor.fromLocalDateTimeToSqlDateTime(c.getDate()));
		ps.setLong(3, c.getVideo_id());
		ps.setLong(4, c.getUser_id());
		if (c.getReplayTo_id() != 0) {
			ps.setLong(5, c.getUser_id());
		} else {
			ps.setString(5, null);
		}
		int i =ps.executeUpdate();
		if(i==0) {
			throw new CommentException(CommentException.CANT_UPDATE);
		}
	}

	public void deleteComment(Comment c) {
		// TODO delete comment from db cascade to replays.
	}

	public void likeComment(Comment c, User u) {
		// TODO add one like in comments_likes or replays dislike or delete
		// existing like;
	}

	public void dislikeComment(Comment c, User u) {
		// TODO add one like in comments_likes or replays dislike or delete
		// existing like;
	}

	public List<Comment> getAllComments(Comment c) {
		// TODO get all replays
		return null;
	}

	public int getLikes(Comment c) {
		// TODO get Number of likes from comments_likes,-1 not existing video
		return 0;
	}

	public int getDislikes(Comment c) {
		// TODO get Number of dislikes from comments_likes,-1 not existing
		// comment id
		return 0;
	}

	// test
	public static void main(String[] args) throws SQLException, UserNotFoundException, VideoNotFoundException {
//		try {
//			Comment c = new Comment(3, "alsdja", LocalDateTime.now(), null, 2, 1, (long) 0);
//			CommentDAO.getInstance().createComment(c);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (CommentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			Comment c = new Comment(3, "ala bala", LocalDateTime.now(), null, 2, 1, (long) 0);
			c.setDate(LocalDateTime.now());
			CommentDAO.getInstance().updateComment(c);
		} catch (CommentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnection.CON1.closeConnection();
		
	}
}
