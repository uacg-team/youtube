package controllers.users;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.UserDao;
import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;

@WebServlet("/follow")
public class FollowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User follower = (User) request.getSession().getAttribute("user");
		if (follower == null) {
			response.sendRedirect("login");
			return;
		}

		long followingId = Long.valueOf(request.getParameter("following"));
		long followerId = Long.valueOf(request.getParameter("follower"));

		String action = request.getParameter("action");
		
		System.out.println("action = " + action);
		
		User user = null;
		try {
			if (action.equals("follow")) {
				UserDao.getInstance().followUser(followingId, followerId);

			}
			if (action.equals("unfollow")) {
				UserDao.getInstance().unfollowUser(followingId, followerId);
			}
			user = UserDao.getInstance().getUser(followingId);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
//		request.getRequestDispatcher("viewProfile?username=" + user.getUsername()).forward(request, response);
		 response.sendRedirect("viewProfile?username=" + user.getUsername());
	}

}
