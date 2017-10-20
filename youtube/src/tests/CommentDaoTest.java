package tests;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import model.Comment;
import model.CommentDao;
import model.exceptions.video.VideoException;

public class CommentDaoTest {

	@Test
	public void testGetAllComments() throws VideoException, SQLException {
		//TODO retry
		List<Comment> comments = CommentDao.getInstance().getAllComments(1, false);
		Assert.assertEquals(comments.size(),1);
	}

}
