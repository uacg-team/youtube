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
import model.User;
import model.exceptions.comments.CommentException;
import model.exceptions.video.VideoException;

/**
 * Servlet implementation class CommentServlet
 */
@WebServlet("/test")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//TODO	long videoId = ((Video)request.getAttribute("mainVideo")).getVideoId();
		long videoId = 2;
		CommentServlet.loadCommentsForVideo(request,videoId);
		request.getRequestDispatcher("comments.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// add new comment or reply
		User u = ((User) request.getSession().getAttribute("user"));
		if (u == null) {
			response.sendRedirect("login");
		} else {
			String text = request.getParameter("newComment");
			long userId = u.getUserId();
			long videoId = Long.valueOf(request.getParameter("videoId"));
			long reply;
			if (request.getParameter("reply") == null) {
				reply = 0;
			} else {
				reply = Long.valueOf(request.getParameter("reply"));
			}
			try {
				Comment comment = new Comment(text, LocalDateTime.now(), userId, videoId, reply);
				CommentDao.getInstance().createComment(comment);
			} catch (CommentException e) {
				//TODO handle
			} catch (SQLException e) {
				//TODO handle
			}
		}
		doGet(request, response);
	}

	public static HttpServletRequest loadCommentsForVideo(HttpServletRequest request,long videoId) {
		// load all info for video comments
		List<Comment> comments = null;
		int countComments = 0;

		try {
			comments = CommentDao.getInstance().getAllComments(videoId, false);
			for (Comment c : comments) {
				List<Comment> replies = CommentDao.getInstance().getAllReplies(c.getCommentId());
				c.addReplies(replies);
				countComments += replies.size() + 1;
				// load likes dislikes for comment:
				c.setLikes(CommentDao.getInstance().getLikes(c.getCommentId()));
				c.setDislikes(CommentDao.getInstance().getDislikes(c.getCommentId()));
				// load likes dislikes for reply:
				for (Comment reply : replies) {
					reply.setLikes(CommentDao.getInstance().getLikes(reply.getCommentId()));
					reply.setDislikes(CommentDao.getInstance().getDislikes(reply.getCommentId()));
				}
			}
			request.setAttribute("comments", comments);
			request.setAttribute("countComments", countComments);
		} catch (VideoException e) {
			request.setAttribute("error", e.getMessage());
		} catch (SQLException e) {
			request.setAttribute("error", "sql exception"+e.getMessage());
		} catch (CommentException e) {
			request.setAttribute("error", e.getMessage());
		}
		return request;
	}

}
