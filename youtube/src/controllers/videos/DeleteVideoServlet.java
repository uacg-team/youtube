package controllers.videos;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.VideoDao;


@WebServlet("/deleteVideo")
public class DeleteVideoServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long videoId = (long) request.getAttribute("videoId");
		if (videoId != 0) {
			try {
				VideoDao.getInstance().deleteVideo(videoId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		response.sendRedirect("main");
	}
}
