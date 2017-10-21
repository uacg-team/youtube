package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Video;
import model.VideoDao;
import model.exceptions.video.VideoNotFoundException;

@WebServlet("/player")
public class PlayerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String videoURL = request.getParameter("url");
			Video video = VideoDao.getInstance().getVideo(videoURL);
			Set<Video> related = VideoDao.getInstance().getRelatedVideos(video.getLocationUrl());
			request.setAttribute("mainVideo", video);
			request.setAttribute("related", related);
			request.getRequestDispatcher("player.jsp").forward(request, response);
		} catch (VideoNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
