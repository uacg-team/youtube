package controllers;

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
		Long videoId = Long.valueOf(request.getParameter("videoId"));

		long userId = u.getUserId();
		String url = request.getParameter("url");
		try {
			if (like == 1) {
				// System.out.println("LikeVideo:"+videoId);
				VideoDao.getInstance().like(videoId, userId);
			}
			if (like == -1) {
				// System.out.println("disLikeVideo:"+videoId);
				VideoDao.getInstance().disLike(videoId, userId);
			}
		} catch (SQLException e) {
			request.getRequestDispatcher("player.jsp").forward(request, response);
			e.printStackTrace();
		}
		response.sendRedirect("player?url="+url);
		// request.getRequestDispatcher("player.jsp").forward(request, response);
		// response.sendRedirect("player.jsp");
	}

}
