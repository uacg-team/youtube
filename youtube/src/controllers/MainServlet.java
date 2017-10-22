package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Video;
import model.VideoDao;
import model.exceptions.video.VideoException;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Video> videos = null;
		try {
			String search = request.getParameter("search");
			if (search != null) {
				videos = VideoDao.getInstance().searchVideo(search);
			}else {
				String param = request.getParameter("sort");
				if (param == null) {
					videos = VideoDao.getInstance().getAllVideoOrderByDate();
					request.setAttribute("sort", "date");
				} else {
					if (param.equals("date")) {
						videos = VideoDao.getInstance().getAllVideoOrderByDate();
						request.setAttribute("sort", "date");
					}
					if (param.equals("like")) {
						videos = VideoDao.getInstance().getAllVideoOrderByLikes();
						request.setAttribute("sort", "like");
					}
					if (param.equals("view")) {
						videos = VideoDao.getInstance().getAllVideoOrderByViews();
						request.setAttribute("sort", "view");
					}
				}
			}

			for (Video video : videos) {
				video.setUserName(VideoDao.getInstance().getUserName(video.getUserId()));
				video.setPrivacy(VideoDao.getInstance().getPrivacy(video.getPrivacyId()));
				video.setLikes(VideoDao.getInstance().getLikes(video.getVideoId()));
				video.setDisLikes(VideoDao.getInstance().getDisLikes(video.getVideoId()));
			}

			request.setAttribute("videos", videos);
			request.getRequestDispatcher("main.jsp").forward(request, response);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (VideoException e) {
			e.printStackTrace();
		}
	}
}
