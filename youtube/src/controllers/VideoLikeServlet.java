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
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		User u = ((User)request.getSession().getAttribute("user"));
		if (u == null) {
			response.sendRedirect("login");
			return;
		}
		
		int like = Integer.valueOf(request.getParameter("like"));
		int videoId = Integer.valueOf(request.getParameter("videoId"));
		long userId = u.getUserId();
		if (like == 1) {
			try {
				VideoDao.getInstance().like(videoId, userId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (like == -1) {
			try {
				VideoDao.getInstance().disLike(videoId, userId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		response.sendRedirect("main");
	}

}
