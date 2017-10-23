package controllers.users;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.UserDao;
import model.Video;
import model.VideoDao;
import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		if (u == null) {
			response.sendRedirect("main");
			return;
		}

		try {
			List<User> followers = UserDao.getInstance().getFollowers(u.getUserId());
			List<User> following = UserDao.getInstance().getFollowing(u.getUserId());
			List<Video> myVideos = VideoDao.getInstance().getVideos(u.getUserId());
			request.setAttribute("followers", followers);
			request.setAttribute("following", following);
			request.setAttribute("videos", myVideos);
		} catch (SQLException e) {
			request.setAttribute("error", "SQL: " + e.getMessage());
		} catch (UserNotFoundException e) {
			request.setAttribute("error", "UserNotFound");
		} catch (UserException e) {
			request.setAttribute("error", "User: " + e.getMessage());
		} finally {
			request.getRequestDispatcher("myprofile.jsp").forward(request, response);
		}
	}
}
