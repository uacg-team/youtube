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


@WebServlet("/myvideos")
public class MyVideosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			User user = (User) request.getSession().getAttribute("user");
			request.setAttribute("videos", VideoDao.getInstance().getVideos(user.getUserId()));
			
			System.out.println(VideoDao.getInstance().getVideos(user.getUserId()));
			
			request.getRequestDispatcher("myprofile.jsp").forward(request, response);
		}catch (SQLException e) {
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("myprofile.jsp").forward(request, response);
		}
	}

}
