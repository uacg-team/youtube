package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.exceptions.comments.InvalidCommentIdException;
import model.utils.DBConnection;
import model.utils.DateTimeConvertor;

public class CommentDAO {
	private static final Connection con =  DBConnection.CON1.getConnection();
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
	 * @param c must have text,date,video_id,user_id;
	 * @throws SQLException
	 * @throws InvalidCommentIdException 
	 */
	public void createComment(Comment c) throws SQLException, InvalidCommentIdException {
		String sql = "insert into comments (text, date,video_id, user_id) values(?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, c.getText());
		ps.setString(2, DateTimeConvertor.fromLocalDateTimeToSqlDateTime(c.getDate()));
		ps.setInt(3, c.getVideo_id());
		ps.setInt(4, c.getUser_id());
		ResultSet rs = ps.executeQuery();
		rs.next();
		long id = rs.getLong(1);
		c.setId(id);
	}
	/**
	 * @param c-comment
	 */
	public void updateComment(Comment c) {
		// TODO update comment using id of comment?
	}

	public void deleteComment(Comment c) {
		// TODO delete comment from db cascade to replays.
	}

	public void likeComment(Comment c, User u) {
		//TODO add one like in comments_likes or replays dislike or delete existing like;
	}
	public void dislikeComment(Comment c, User u) {
		//TODO add one like in comments_likes or replays dislike or delete existing like;
	}
	
	public List<Comment> getAllComments(Comment c) {
		// TODO get all replays
		return null;
	}
	public int getLikes(Comment c) {
		//TODO get Number of likes from comments_likes,-1 not existing video
		return 0;
	}
	public int getDislikes(Comment c) {
		//TODO get Number of dislikes from comments_likes,-1 not existing comment id
		return 0;
	}
	//test
	public static void main(String[] args) {
//		Comment c = new 
	}
}
