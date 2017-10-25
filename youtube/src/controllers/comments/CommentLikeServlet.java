package controllers.comments;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CommentDao;
import model.User;
import model.exceptions.comments.CommentException;
import model.exceptions.user.UserException;

@WebServlet("/commentLike")
public class CommentLikeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = ((User)request.getSession().getAttribute("user"));
		if(u == null) {
			response.sendRedirect("login");
			return;
		}else {
			long commentId = Long.valueOf(request.getParameter("commentId"));
			int like = Integer.valueOf(request.getParameter("like"));
			try {
				//add like or dislike for comment id
				if(like==1) {
					CommentDao.getInstance().likeComment(commentId, u.getUserId());
				}else if(like==-1) {
					CommentDao.getInstance().dislikeComment(commentId, u.getUserId());
				}
			} catch (SQLException e) {
				request.setAttribute("error", "sqlException "+e.getMessage());
				request.getRequestDispatcher("player").forward(request, response);
				return;
			} catch (CommentException e) {
				request.setAttribute("error",e.getMessage());
				request.getRequestDispatcher("player").forward(request, response);
				return;
			} catch (UserException e) {
				request.setAttribute("error",e.getMessage());
				request.getRequestDispatcher("player").forward(request, response);
				return;
			}
			//TODO preprashta kym videoto CommentServlet!!!
			response.sendRedirect("player?url="+request.getParameter("url")+"&videoId="+request.getParameter("videoId"));
		}
	}

}
