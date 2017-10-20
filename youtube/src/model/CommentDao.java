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
import model.exceptions.user.UserException;
import model.exceptions.video.VideoException;
import model.utils.DBConnection;
import model.utils.DateTimeConvertor;

public class CommentDao {
	private static final Connection con = DBConnection.CON1.getConnection();
	private static CommentDao instance;
	static {
		instance = new CommentDao();
	}

	private CommentDao() {
	}

	public static CommentDao getInstance() {
		return instance;
	}

	/**
	 * <b>!Warning! set replay_id only to comment,not for replay!</b>
	 * 
	 * @param comment
	 *            must have text,date,video_id,user_id,no need of id;
	 * @throws SQLException
	 * @throws CommentException
	 */
	public void createComment(Comment comment) throws SQLException, CommentException {
		String sql = "insert into comments (text, date,video_id, user_id, replay_id) values(?,?,?,?,?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, comment.getText());
			ps.setString(2, DateTimeConvertor.ldtToSql(comment.getDate()));
			ps.setLong(3, comment.getVideo_id());
			ps.setLong(4, comment.getUser_id());
			if (comment.getReplayTo_id() != 0) {
				ps.setLong(5, comment.getReplayTo_id());
			} else {
				ps.setString(5, null);
			}
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				rs.next();
				long id = rs.getLong(1);
				comment.setId(id);
			}
		}
	}

	/**
	 * @param comment-comment
	 *            with changes
	 * @throws SQLException
	 * @throws CommentNotFoundException
	 *             - if cant find comment
	 * @throws CommentException
	 *             -if cant update in db
	 */
	public void updateComment(Comment comment) throws SQLException, CommentException {
		String foundComment = "select comment_id from comments where comment_id=?;";
		try (PreparedStatement ps = con.prepareStatement(foundComment)) {
			ps.setLong(1, comment.getId());
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					ps.close();
					rs.close();
					throw new CommentNotFoundException();
				}
			}
		}

		String sql = "UPDATE comments SET text=?, date=STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s.%f'),video_id=?, user_id=?, replay_id=? WHERE comment_id="
				+ comment.getId();
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, comment.getText());
			ps.setString(2, DateTimeConvertor.ldtToSql(comment.getDate()));
			ps.setLong(3, comment.getVideo_id());
			ps.setLong(4, comment.getUser_id());
			if (comment.getReplayTo_id() != 0) {
				ps.setLong(5, comment.getUser_id());
			} else {
				ps.setString(5, null);
			}
			int i = ps.executeUpdate();
			if (i == 0) {
				throw new CommentException(CommentException.CANT_UPDATE);
			}
		}
	}

	/**
	 * 
	 * @param c
	 * @return comments deleted
	 * @throws CommentException
	 * @throws SQLException
	 */
	public int deleteComment(long comment_id) throws CommentException, SQLException {
		int count;
		count = deleteAllReplaysToComment(comment_id);
		// TODO delete all likes and dislikes
		String sql = "delete from comments where comment_id=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, comment_id);
			count += ps.executeUpdate();
			return count;
		}
	}

	/**
	 * @param comment_id
	 * @return count deleted comments
	 * @throws SQLException
	 * @throws CommentException
	 */
	private int deleteAllReplaysToComment(long comment_id) throws SQLException {
		String sql = "delete from comments where comment_id in "
				+ "(select * from (select r.comment_id from comments as c "
				+ "inner join comments as r on (r.replay_id = c.comment_id) where c.comment_id=?) as d);";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, comment_id);
			int count = ps.executeUpdate();
			ps.close();
			return count;
		}
	}

	@SuppressWarnings("unused")
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
		// There is no risk for sql injection because input is only integers!
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
	 * @param video_id
	 * @return deleted comments
	 * @throws SQLException
	 */
	public int deleteComments(long video_id) throws SQLException {
		int count = 0;
		// TODO delete comments likes
		String deleteReplays = "delete from comments where video_id = ? and replay_id is not null;";
		String deleteComments = "delete from comments where video_id = ?";
		try (PreparedStatement ps = con.prepareStatement(deleteReplays)) {
			ps.setLong(1, video_id);
			count += ps.executeUpdate();
		}
		try (PreparedStatement ps = con.prepareStatement(deleteComments)) {
			ps.setLong(1, video_id);
			count += ps.executeUpdate();
			return count;
		}
	}

	/**
	 * @param comment_id
	 *            must have comment_id
	 * @param user_id
	 *            - must have id
	 * @throws SQLException
	 * @throws CommentException
	 * @throws UserException
	 */
	public void likeComment(long comment_id, long user_id) throws SQLException, CommentException, UserException {
		String sql = "select isLike from comments_likes where user_id=? and comment_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, user_id);
			ps.setLong(2, comment_id);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					sql = "insert into comments_likes (user_id,comment_id,isLike) values (?,?,1)";
				} else {
					if (rs.getBoolean(1)) {
						sql = "delete from comments_likes where user_id=? and comment_id=?";
					} else {
						sql = "update comments_likes set isLike=1 where user_id=? and comment_id=?";
					}
				}
			}
		}
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, user_id);
			ps.setLong(2, comment_id);
			ps.executeUpdate();
		}
	}

	/**
	 * @param comment_id
	 * @param user_id
	 * @throws SQLException
	 * @throws CommentException
	 * @throws UserException
	 */
	public void dislikeComment(long comment_id, long user_id) throws SQLException, CommentException, UserException {
		String sql = "select isLike from comments_likes where user_id=? and comment_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, user_id);
			ps.setLong(2, comment_id);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					sql = "insert into comments_likes (user_id,comment_id,isLike) values (?,?,0)";
				} else {
					if (!rs.getBoolean(1)) {
						sql = "delete from comments_likes where user_id=? and comment_id=?";
					} else {
						sql = "update comments_likes set isLike=0 where user_id=? and comment_id=?";
					}
				}
			}
		}
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, user_id);
			ps.setLong(2, comment_id);
			ps.executeUpdate();
		}
	}

	/**
	 * @param comment_id
	 * @return -empty ArrayList list if no comments, or ordered list replays by
	 *         date
	 * @throws SQLException
	 * @throws CommentException
	 *             -if comment have no id
	 */
	public List<Comment> getAllReplays(long comment_id) throws SQLException, CommentException {
		String sql = "select * from comments where replay_id=? order by date;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, comment_id);
			try (ResultSet rs = ps.executeQuery()) {
				List<Comment> replays = new ArrayList<>();
				while (rs.next()) {
					Long id = rs.getLong("comment_id");
					String text = rs.getString("text");
					LocalDateTime date = DateTimeConvertor.sqlToLdt(rs.getString("date"));
					Long user_id = rs.getLong("user_id");
					Long video_id = rs.getLong("video_id");
					// because this is replay there is no replay to this
					// comment!
					Long replayTo_id = (long) 0;
					Comment replay = new Comment(id, text, date, user_id, video_id, replayTo_id);
					replays.add(replay);
				}
				return replays;
			}
		}
	}

	/**
	 * @param video_id
	 * @param withReplays
	 *            - if true comments and replays sorted by date,else only
	 *            comments ordered by date
	 * @return List<Comments>
	 * @throws VideoException
	 *             -for invalid id = 0
	 * @throws SQLException
	 */
	public List<Comment> getAllComments(long video_id, boolean withReplays) throws VideoException, SQLException {
		String addition = "";
		if (!withReplays) {
			addition = "and replay_id is null ";
		}
		String sql = "select * from comments where video_id=? " + addition + "order by date;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, video_id);
			try (ResultSet rs = ps.executeQuery()) {
				List<Comment> comments = new ArrayList<>();
				while (rs.next()) {
					Long id = rs.getLong("comment_id");
					String text = rs.getString("text");
					LocalDateTime date = DateTimeConvertor.sqlToLdt(rs.getString("date"));
					Long user_id = rs.getLong("user_id");
					Long replayTo_id = rs.getLong("replay_id");
					Comment replay = new Comment(id, text, date, user_id, video_id, replayTo_id);
					comments.add(replay);
				}
				return comments;
			}
		}
	}

	/**
	 * @param user_id
	 * @param withReplays
	 *            -if true comments and replays sorted by date,else only
	 *            comments ordered by date
	 * @return
	 * @throws SQLException
	 * @throws UserException
	 *             -for invalid id = 0
	 */
	public List<Comment> getAllCommentsByUser(long user_id, boolean withReplays) throws SQLException, UserException {
		String addition = "";
		if (!withReplays) {
			addition = "and replay_id is null ";
		}
		String sql = "select * from comments where user_id=? " + addition + "order by date;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, user_id);
			try (ResultSet rs = ps.executeQuery()) {
				List<Comment> comments = new ArrayList<>();
				while (rs.next()) {
					Long id = rs.getLong("comment_id");
					String text = rs.getString("text");
					LocalDateTime date = DateTimeConvertor.sqlToLdt(rs.getString("date"));
					Long video_id = rs.getLong("video_id");
					Long replayTo_id = rs.getLong("replay_id");
					Comment replay = new Comment(id, text, date, user_id, video_id, replayTo_id);
					comments.add(replay);
				}
				return comments;
			}
		}
	}

	/**
	 * @param comment_id
	 * @return integer
	 * @throws SQLException
	 */
	public int getLikes(long comment_id) throws SQLException {
		String sql = "select count(*) from comments_likes where comment_id=? and isLike = 1;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, comment_id);
			try (ResultSet rs = ps.executeQuery()) {
				// there is always information
				rs.next();
				int likes = rs.getInt(1);
				return likes;
			}
		}
	}

	/**
	 * @param comment_id
	 * @return integer
	 * @throws SQLException
	 */
	public int getDislikes(long comment_id) throws SQLException {
		String sql = "select count(*) from comments_likes where comment_id=? and isLike = 0;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, comment_id);
			try (ResultSet rs = ps.executeQuery()) {
				// there is always information
				rs.next();
				int dislikes = rs.getInt(1);
				return dislikes;
			}
		}
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
		/* delete all replays */
		// ArrayList<Integer> ints = new ArrayList<>();
		// ints.add(1);
		// try {
		// CommentDAO.getInstance().deleteAllReplaysToComments(ints);
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// }
		/* delete comments for video */
		/* like dislike video test */
		// User user;
		// try {
		// user = UserDao.getInstance().getUser("Hristo");
		// Comment com = new Comment(1, "alabala", LocalDateTime.now(), 2, 1,
		// (long) 0);
		// CommentDAO.getInstance().dislikeComment(com, user);
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// } catch (UserNotFoundException e1) {
		// e1.printStackTrace();
		// } catch (UserException e) {
		// e.printStackTrace();
		// }
		/* replays to comment */
		// try {
		// Comment com = new Comment(1, "alabala", LocalDateTime.now(), 2, 1,
		// (long) 0);
		// List<Comment> replays = CommentDAO.getInstance().getAllReplays(com);
		// for (Comment c : replays) {
		// System.out.println(c.getText());
		// }
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// } catch (CommentException e1) {
		// e1.printStackTrace();
		// }
		/* comments to video */
		// try {
		// Video v = new Video(1, "", 1, LocalDateTime.now(), "location_url", 1,
		// "thumbnail_url", "description", 1,
		// null);
		// List<Comment> replays = CommentDAO.getInstance().getAllComments(v,
		// false);
		// for (Comment c : replays) {
		// System.out.println(c.getText());
		// }
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// } catch (VideoException e) {
		// e.printStackTrace();
		// }
		/* comments from user */
		// try {
		// User u =UserDao.getInstance().getUser("Velichko");
		// List<Comment> replays = CommentDAO.getInstance().getAllComments(u,
		// false);
		// for (Comment c : replays) {
		// System.out.println(c.getText());
		// }
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// } catch (UserNotFoundException e) {
		// e.printStackTrace();
		// } catch (UserException e) {
		// e.printStackTrace();
		// }
		try {
			DBConnection.CON1.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
