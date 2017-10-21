package tests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.Comment;
import model.CommentDao;
import model.exceptions.comments.CommentException;
import model.exceptions.user.UserException;
import model.exceptions.video.VideoException;
import model.utils.DBConnection;

public class CommentDaoTest {
	private static final Connection con = DBConnection.CON1.getConnection();
	@Test
	public void testGetAllComments() throws VideoException, SQLException, CommentException {
		//for test get all comments
		CommentDao.getInstance().createComment(new Comment("initial comment", LocalDateTime.now(), 1, 2, 0));
		List<Comment> comments = CommentDao.getInstance().getAllComments(2, false);
		Assert.assertEquals(comments.size(),1);
	}
	@Test
	public void testCreateComment() throws VideoException, SQLException, CommentException {
		//comments
		for (int i = 0; i < 15; i++) {
			CommentDao.getInstance().createComment(new Comment("comment"+i, LocalDateTime.now(), 1, 1, 0));
		}
		//replies
		for (int i = 0; i < 15; i++) {
			CommentDao.getInstance().createComment(new Comment("reply"+i, LocalDateTime.now(), 1, 1,1+ new Random().nextInt(15)));
		}
	}
	@Test
	public void testLikeComment() throws SQLException, CommentException, UserException {
		CommentDao.getInstance().createComment(new Comment("comment1", LocalDateTime.now(), 1, 1, 0));
		CommentDao.getInstance().likeComment(1, 1);
		Assert.assertEquals(CommentDao.getInstance().getLikes(1), 1);
	}
	@Test
	public void testDislikeComment() throws SQLException, CommentException, UserException {
		CommentDao.getInstance().createComment(new Comment("comment1", LocalDateTime.now(), 1, 1, 0));
		CommentDao.getInstance().dislikeComment(1, 1);
		Assert.assertEquals(CommentDao.getInstance().getDislikes(1), 1);
		CommentDao.getInstance().likeComment(1, 1);
		Assert.assertEquals(CommentDao.getInstance().getLikes(1), 1);
	}
	@Before
	public void init() throws SQLException {
		String sql1 = "delete from comments_likes";
		String sql2 = "delete from comments where reply_id is not null";
		String sql3 = "delete from comments";
		String sql4 = "alter table comments AUTO_INCREMENT = 1";
		try (PreparedStatement ps = con.prepareStatement(sql1)) {
			ps.executeUpdate();
		}
		try (PreparedStatement ps = con.prepareStatement(sql2)) {
			ps.executeUpdate();
		}
		try (PreparedStatement ps = con.prepareStatement(sql3)) {
			ps.executeUpdate();
		}
		try (PreparedStatement ps = con.prepareStatement(sql4)) {
			ps.executeUpdate();
		}
	}

}
