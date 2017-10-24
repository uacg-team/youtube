package controllers.videos;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.VideoDao;

@WebServlet("/videoLike")
public class VideoLikeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = ((User) request.getSession().getAttribute("user"));
		if (u == null) {
			response.sendRedirect("login");
			return;
		}
		int like = Integer.valueOf(request.getParameter("like"));
		
		long videoId = Long.valueOf(request.getParameter("videoId"));
		long userId = u.getUserId();
		try {
			if (like == 1) {
				VideoDao.getInstance().like(videoId, userId);
			}
			if (like == -1) {
				VideoDao.getInstance().disLike(videoId, userId);
			}
		} catch (SQLException e) {
			request.getRequestDispatcher("player.jsp").forward(request, response);
			e.printStackTrace();
		}
		response.sendRedirect("player?videoId="+videoId);
	}
}
