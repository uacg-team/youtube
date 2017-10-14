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

	/**
	 * <b>!Warning! set replay_id only to comment,not for replay!</b> 
	 * @param c
	 *            must have text,date,video_id,user_id,no need of id;
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
	 * @param video
	 *            with id
	 * @return deleted comments
	 * @throws SQLException
	 */
	public int deleteComments(Video video) throws SQLException {
		int count = 0;
		String deleteReplays = "delete from comments where video_id = ? and replay_id is not null;";
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

	/**
	 * 
	 * @param comment
	 *            must have comment_id
	 * @param user
	 *            - must have id
	 * @throws SQLException
	 * @throws CommentException
	 * @throws UserException
	 */
	public void likeComment(Comment comment, User user) throws SQLException, CommentException, UserException {
		if (comment.getId() == 0) {
			throw new CommentException(CommentException.MISSING_ID);
		}
		if (user.getUser_id() == 0) {
			throw new UserException(UserException.INVALID_ID);
		}
		String sql = "select isLike from comments_likes where user_id=? and comment_id=?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, user.getUser_id());
		ps.setLong(2, comment.getId());
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			sql = "insert into comments_likes (user_id,comment_id,isLike) values (?,?,1)";
		} else {
			if (rs.getBoolean(1)) {
				sql = "delete from comments_likes where user_id=? and comment_id=?";
			} else {
				sql = "update comments_likes set isLike=1 where user_id=? and comment_id=?";
			}
		}
		ps.close();
		ps = con.prepareStatement(sql);
		ps.setLong(1, user.getUser_id());
		ps.setLong(2, comment.getId());
		ps.executeUpdate();
		ps.close();
		rs.close();
	}

	/**
	 * 
	 * @param comment
	 *            -must have comment_id
	 * @param user
	 *            - must have id
	 * @throws SQLException
	 * @throws CommentException
	 * @throws UserException
	 */
	public void dislikeComment(Comment comment, User user) throws SQLException, CommentException, UserException {
		if (comment.getId() == 0) {
			throw new CommentException(CommentException.MISSING_ID);
		}
		if (user.getUser_id() == 0) {
			throw new UserException(UserException.INVALID_ID);
		}
		String sql = "select isLike from comments_likes where user_id=? and comment_id=?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, user.getUser_id());
		ps.setLong(2, comment.getId());
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			sql = "insert into comments_likes (user_id,comment_id,isLike) values (?,?,0)";
		} else {
			if (!rs.getBoolean(1)) {
				sql = "delete from comments_likes where user_id=? and comment_id=?";
			} else {
				sql = "update comments_likes set isLike=0 where user_id=? and comment_id=?";
			}
		}
		ps.close();
		ps = con.prepareStatement(sql);
		ps.setLong(1, user.getUser_id());
		ps.setLong(2, comment.getId());
		ps.executeUpdate();
		ps.close();
		rs.close();
	}

	/**
	 * @param comment
	 *            - comment must have id
	 * @return -empty ArrayList list if no comments, or ordered list replays by
	 *         date
	 * @throws SQLException
	 * @throws CommentException
	 *             -if comment have no id
	 */
	public List<Comment> getAllReplays(Comment comment) throws SQLException, CommentException {
		if (comment.getId() == 0) {
			throw new CommentException(CommentException.MISSING_ID);
		}
		String sql = "select * from comments where replay_id=? order by date;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, comment.getId());
		ResultSet rs = ps.executeQuery();
		List<Comment> replays = new ArrayList<>();
		while (rs.next()) {
			Long id = rs.getLong("comment_id");
			String text = rs.getString("text");
			LocalDateTime date = DateTimeConvertor.fromSqlDateTimeToLocalDateTime(rs.getString("date"));
			Long user_id = rs.getLong("user_id");
			Long video_id = rs.getLong("video_id");
			// because this is replay there is no replay to this comment!
			Long replayTo_id = (long) 0;
			Comment replay = new Comment(id, text, date, user_id, video_id, replayTo_id);
			replays.add(replay);
		}
		ps.close();
		rs.close();
		return replays;
	}

	/**
	 * 
	 * @param video
	 *            - must be with id
	 * @param withReplays
	 *            - if true comments and replays sorted by date,else only
	 *            comments ordered by date
	 * @return List<Comments>
	 * @throws VideoException
	 *             -for invalid id = 0
	 * @throws SQLException
	 */
	public List<Comment> getAllComments(Video video, boolean withReplays) throws VideoException, SQLException {
		if (video.getVideo_id() == 0) {
			throw new VideoException(VideoException.INVALID_ID);
		}
		String addition = "";
		if (!withReplays) {
			addition = "and replay_id is null ";
		}
		String sql = "select * from comments where video_id=? " + addition + "order by date;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, video.getVideo_id());
		ResultSet rs = ps.executeQuery();
		List<Comment> comments = new ArrayList<>();
		while (rs.next()) {
			Long id = rs.getLong("comment_id");
			String text = rs.getString("text");
			LocalDateTime date = DateTimeConvertor.fromSqlDateTimeToLocalDateTime(rs.getString("date"));
			Long user_id = rs.getLong("user_id");
			Long video_id = rs.getLong("video_id");
			Long replayTo_id = rs.getLong("replay_id");
			Comment replay = new Comment(id, text, date, user_id, video_id, replayTo_id);
			comments.add(replay);
		}
		ps.close();
		rs.close();
		return comments;
	}

	/**
	 * 
	 * @param user
	 *            -must be with id
	 * @param withReplays
	 *            -if true comments and replays sorted by date,else only
	 *            comments ordered by date
	 * @return
	 * @throws SQLException
	 * @throws UserException
	 *             -for invalid id = 0
	 */
	public List<Comment> getAllComments(User user, boolean withReplays) throws SQLException, UserException {
		if (user.getUser_id() == 0) {
			throw new UserException(UserException.INVALID_ID);
		}
		String addition = "";
		if (!withReplays) {
			addition = "and replay_id is null ";
		}
		String sql = "select * from comments where user_id=? " + addition + "order by date;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, user.getUser_id());
		ResultSet rs = ps.executeQuery();
		List<Comment> comments = new ArrayList<>();
		while (rs.next()) {
			Long id = rs.getLong("comment_id");
			String text = rs.getString("text");
			LocalDateTime date = DateTimeConvertor.fromSqlDateTimeToLocalDateTime(rs.getString("date"));
			Long user_id = rs.getLong("user_id");
			Long video_id = rs.getLong("video_id");
			Long replayTo_id = rs.getLong("replay_id");
			Comment replay = new Comment(id, text, date, user_id, video_id, replayTo_id);
			comments.add(replay);
		}
		ps.close();
		rs.close();
		return comments;
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
