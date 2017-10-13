package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
	 *            with changes
	 * @throws SQLException
	 * @throws CommentNotFoundException
	 *             - if cant find comment
	 * @throws CommentException
	 *             -if cant update in db
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
		int i = ps.executeUpdate();
		ps.close();
		if (i == 0) {
			throw new CommentException(CommentException.CANT_UPDATE);
		}
	}

	/**
	 * 
	 * @param c
	 * @return comments deleted
	 * @throws CommentException
	 * @throws SQLException
	 */
	public int deleteComment(Comment c) throws CommentException, SQLException {
		int count;
		count = deleteAllReplaysToComment(c);
		String sql = "delete from comments where comment_id=?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, c.getId());
		count += ps.executeUpdate();
		return count;
	}

	/**
	 * @param c
	 * @return count deleted comments
	 * @throws SQLException
	 * @throws CommentException
	 */
	private int deleteAllReplaysToComment(Comment c) throws SQLException, CommentException {
		if (c.getId() == 0) {
			throw new CommentException(CommentException.INVALID_ID);
		}
		String sql = "delete from comments where comment_id in "
				+ "(select * from (select r.comment_id from comments as c "
				+ "inner join comments as r on (r.replay_id = c.comment_id) where c.comment_id=?) as d);";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, c.getId());
		int count = ps.executeUpdate();
		ps.close();
		return count;
	}
	@Deprecated
	/**
	 * @param c
	 * @return count deleted comments
	 * @throws SQLException
	 * @throws CommentException
	 */
	private int deleteAllReplaysToComments(ArrayList<Integer> comment_ids) throws SQLException {
		if (comment_ids == null || comment_ids.isEmpty()) {
			throw new NullPointerException("ArrayList comments_id is empty");
		}
		StringBuilder prepareStatement = new StringBuilder();
		for (int i : comment_ids) {
			prepareStatement.append("c.comment_id=" + i + " or ");
		}
		// because " or " is 4 symbols -1 = 3
		String statement = prepareStatement.substring(0, prepareStatement.length() - 3);
		//There is no risk for sql injection because input is only integers!
		String sql = "delete from comments where comment_id in "
				+ "(select * from (select r.comment_id from comments as c "
				+ "inner join comments as r on (r.replay_id = c.comment_id) where " + statement + " ) as d);";
		PreparedStatement ps = con.prepareStatement(sql);
		int count = ps.executeUpdate();
		ps.close();
		return count;
	}
	/**
	 * 
	 * @param video with id
	 * @return deleted comments
	 * @throws SQLException
	 */
	public int deleteComments(Video video) throws SQLException {
		int count = 0;
		String deleteReplays ="delete from comments where video_id = ? and replay_id is not null;";
		String deleteComments = "delete from comments where video_id = ?";
		PreparedStatement ps = con.prepareStatement(deleteReplays);
		ps.setLong(1, video.getVideo_id());
		count += ps.executeUpdate();
		ps.close();
		ps = con.prepareStatement(deleteComments);
		ps.setLong(1, video.getVideo_id());
		count += ps.executeUpdate();
		ps.close();
		return count;
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

	/**
	 * @param c-comment
	 *            must be with id!
	 * @return integer
	 * @throws SQLException
	 */
	public int getLikes(Comment c) throws SQLException {
		String sql = "select count(*) from comments_likes where comment_id=? and isLike = 1;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, c.getId());
		ResultSet rs = ps.executeQuery();
		// there is always information
		rs.next();
		int likes = rs.getInt(1);
		ps.close();
		rs.close();
		return likes;
	}

	/**
	 * @param c-comment
	 *            must be with id!
	 * @return integer
	 * @throws SQLException
	 */
	public int getDislikes(Comment c) throws SQLException {
		String sql = "select count(*) from comments_likes where comment_id=? and isLike = 0;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, c.getId());
		ResultSet rs = ps.executeQuery();
		int dislikes = rs.getInt(1);
		// there is always information
		rs.next();
		ps.close();
		rs.close();
		return dislikes;
	}
	public void replayToComment(Comment comment,User user) {
		//TODO
	}
	public void replayToVideo(Video video,User user) {
		//TODO
	}
	// test
	public static void main(String[] args) {
		/* add comment */
		// try {
		// Comment c = new Comment(3, "alsdja", LocalDateTime.now(), null, 2, 1,
		// (long) 0);
		// CommentDAO.getInstance().createComment(c);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// } catch (CommentException e) {
		// e.printStackTrace();
		// }
		/* change comment */
		// try {
		// Comment c = new Comment(3, "ala bala", LocalDateTime.now(), null, 2,
		// 1, (long) 0);
		// c.setDate(LocalDateTime.now());
		// CommentDAO.getInstance().updateComment(c);
		// } catch (CommentException e) {
		// e.printStackTrace();
		// }
		/* get likes */
		// try {
		// Comment c = new Comment(150, "ala bala", LocalDateTime.now(), null,
		// 2, 1, (long) 0);
		// System.out.println(CommentDAO.getInstance().getLikes(c));
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		/* get dislikes */
		// try {
		// Comment c = new Comment(0, "ala bala", LocalDateTime.now(), null, 2,
		// 1, (long) 0);
		// System.out.println(CommentDAO.getInstance().getLikes(c));
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		ArrayList<Integer> ints = new ArrayList<>();
		ints.add(1);
		try {
			CommentDAO.getInstance().deleteAllReplaysToComments(ints);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			DBConnection.CON1.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
