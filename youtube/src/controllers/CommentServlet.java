package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Comment;
import model.CommentDao;
import model.exceptions.comments.CommentException;
import model.exceptions.video.VideoException;

/**
 * Servlet implementation class CommentServlet
 */
@WebServlet("/test")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Comment> comments = null;
		int countComments = 0;
		try {
			comments = CommentDao.getInstance().getAllComments(1, false);
			for (Comment c : comments) {
				List<Comment> replies = CommentDao.getInstance().getAllReplies(c.getCommentId());
				c.addReplies(replies);
				countComments += replies.size() + 1;
			}
		} catch (VideoException | SQLException e) {
			request.setAttribute("Exception", "exception");
		} catch (CommentException e) {
			request.setAttribute("Exception", "exception");
		}
		request.setAttribute("comments", comments);
		request.setAttribute("countComments", countComments);
		request.getRequestDispatcher("comments.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO add Comment
		
		doGet(request, response);
	}

}
