package controllers.users;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.playlists.PlaylistServlet;
import model.User;
import model.UserDao;
import model.Video;
import model.VideoDao;
import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;

@WebServlet("/viewProfile")
public class ViewProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String un  = request.getParameter("username");
		User loggedUser = (User) request.getSession().getAttribute("user");
		User u = null;
		try {
			u = UserDao.getInstance().getUser(un);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (UserNotFoundException e1) {
			e1.printStackTrace();
		} catch (UserException e1) {
			e1.printStackTrace();
		}
		
		try {
			List<User> followers = UserDao.getInstance().getFollowers(u.getUserId());
			List<User> following = UserDao.getInstance().getFollowing(u.getUserId());
			List<Video> videos = null;
			
			if (loggedUser != null && loggedUser.getUserId() == u.getUserId()) {
				videos = VideoDao.getInstance().getVideos(u.getUserId());
			} else {
				videos = VideoDao.getInstance().getPublicVideos(u.getUserId());
			}
			
			for (Video video : videos) {
				video.setUserName(VideoDao.getInstance().getUserName(video.getUserId()));
				video.setPrivacy(VideoDao.getInstance().getPrivacy(video.getPrivacyId()));
			}
			
			request.setAttribute("user", u);
			request.setAttribute("followers", followers);
			request.setAttribute("following", following);
			request.setAttribute("videos", videos);
			PlaylistServlet.loadPlaylistForUser(request, u.getUserId());
		} catch (SQLException e) {
			request.setAttribute("error", "SQL: " + e.getMessage());
		} catch (UserNotFoundException e) {
			request.setAttribute("error", "UserNotFound");
		} catch (UserException e) {
			request.setAttribute("error", "User: " + e.getMessage());
		} finally {
			request.getRequestDispatcher("viewProfile.jsp").forward(request, response);
		}
	}
}
