package controllers.videos;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.Video;
import model.VideoDao;
import model.exceptions.video.VideoException;
import model.exceptions.video.VideoNotFoundException;

@WebServlet("/editVideo")
public class EditVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long videoId = Long.parseLong(request.getParameter("videoId"));
		String newName = request.getParameter("name");
		String newDesc = request.getParameter("description");
		long newPrivacy = Long.parseLong(request.getParameter("privacy"));
		
		try {
			Video v = VideoDao.getInstance().getVideo(videoId);
			if (newName != null) {
				v.setName(newName);
			}
			if (newDesc != null) {
				v.setDescription(newDesc);
			}
			v.setPrivacyId(newPrivacy);
			VideoDao.getInstance().updateVideo(v);
		} catch (VideoNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (VideoException e) {
			e.printStackTrace();
		}
		response.sendRedirect("player?videoId="+videoId);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long videoId = Long.parseLong(request.getParameter("videoId"));
		if (request.getSession().getAttribute("user") == null) {
			response.sendRedirect("login");
			return;
		}
		try {
			Video video = VideoDao.getInstance().getVideo(videoId);
			request.setAttribute("video", video);
			User user = (User) request.getSession().getAttribute("user");
			request.setAttribute("username", user.getUsername());
			request.setAttribute("userId", user.getUserId());
			request.getRequestDispatcher("editVideo.jsp").forward(request, response);
		} catch (VideoNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
