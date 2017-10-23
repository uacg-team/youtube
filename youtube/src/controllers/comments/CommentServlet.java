package controllers.comments;

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
@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//triger for delete command:
		if(request.getParameter("deleteCommentId")!=null) {
			doDelete(request, response);
			return;
		}
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
				e.printStackTrace();
			} catch (SQLException e) {
				//TODO handle
				e.printStackTrace();
			}
			response.sendRedirect("player?url="+request.getParameter("url"));
		}
	}
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CommentDao.getInstance().deleteComment(Long.valueOf(req.getParameter("deleteCommentId")));
		} catch (NumberFormatException e) {
			req.setAttribute("error", "converting url");
			req.getRequestDispatcher("player").forward(req, resp);
			return;
		} catch (CommentException e) {
			req.setAttribute("error", "commentException "+e.getMessage());
			req.getRequestDispatcher("player").forward(req, resp);
			return;
		} catch (SQLException e) {
			req.setAttribute("error", "sqlException "+e.getMessage());
			req.getRequestDispatcher("player").forward(req, resp);
			return;
		}
		resp.sendRedirect("player?url="+req.getParameter("url"));
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
				//load user info for comment:
				CommentDao.getInstance().loadUserInfo(c);
				// load likes dislikes for reply, and user info:
				for (Comment reply : replies) {
					reply.setLikes(CommentDao.getInstance().getLikes(reply.getCommentId()));
					reply.setDislikes(CommentDao.getInstance().getDislikes(reply.getCommentId()));
					CommentDao.getInstance().loadUserInfo(reply);
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
