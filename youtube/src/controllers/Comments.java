package controllers;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.Comment;
import model.CommentDao;
import model.exceptions.comments.CommentException;
import model.exceptions.video.VideoException;

public final class Comments {
	private Comments() {
	}
	public static HttpServletRequest loadCommentsForVideo(HttpServletRequest request) {
		//load all info for video comments
		List<Comment> comments = null;
		int countComments = 0;
		
		try {
			//TODO set video id with real id!!!!
			comments = CommentDao.getInstance().getAllComments(1, false);
			for (Comment c : comments) {
				List<Comment> replies = CommentDao.getInstance().getAllReplies(c.getCommentId());
				c.addReplies(replies);
				countComments += replies.size() + 1;
				//load likes dislikes for comment:
				c.setLikes(CommentDao.getInstance().getLikes(c.getCommentId()));
				c.setDislikes(CommentDao.getInstance().getDislikes(c.getCommentId()));
				//load likes dislikes for reply: 
				for(Comment reply:replies) {
					reply.setLikes(CommentDao.getInstance().getLikes(reply.getCommentId()));
					reply.setDislikes(CommentDao.getInstance().getDislikes(reply.getCommentId()));
				}
			}
			request.setAttribute("comments", comments);
			request.setAttribute("countComments", countComments);
		} catch (VideoException e) {
			request.setAttribute("Exception", e.getMessage());
		} catch (SQLException e) {
			request.setAttribute("Exception", "exception");
		} catch (CommentException e) {
			request.setAttribute("Exception", e.getMessage());
		}
		return request;
	}
}
