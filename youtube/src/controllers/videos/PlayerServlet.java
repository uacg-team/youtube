package controllers.videos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.comments.CommentServlet;
import controllers.playlists.PlaylistServlet;
import model.User;
import model.UserDao;
import model.Video;
import model.VideoDao;
import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;
import model.exceptions.video.VideoNotFoundException;

@WebServlet("/player")
public class PlayerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long videoId = Long.parseLong(request.getParameter("videoId"));
		
		try {
			VideoDao.getInstance().increaseViews(videoId);
			Video video = VideoDao.getInstance().getVideo(videoId);
			request.setAttribute("mainVideo", video);
			User videoOwner =  UserDao.getInstance().getUser(video.getUserId());
			request.setAttribute("videoOwner", videoOwner);
			
			int likes = VideoDao.getInstance().getLikes(videoId);
			request.setAttribute("likes", likes);
		
			int disLikes = VideoDao.getInstance().getDisLikes(videoId);
			request.setAttribute("disLikes", disLikes);
		
			Set<Video> related = VideoDao.getInstance().getRelatedVideos(videoId);
			request.setAttribute("related", related);
		
			CommentServlet.loadCommentsForVideo(request, video.getVideoId());
			if(request.getSession().getAttribute("user")!=null) {
				User user = (User)request.getSession().getAttribute("user");
				long userId = user.getUserId();
				PlaylistServlet.loadPlaylistForUser(request, userId);
			}
			request.getRequestDispatcher("player.jsp").forward(request, response);
		} catch (VideoNotFoundException e) {
			request.getRequestDispatcher("player.jsp").forward(request, response);
		} catch (SQLException e) {
			request.getRequestDispatcher("player.jsp").forward(request, response);
		} catch (UserNotFoundException e) {
			request.getRequestDispatcher("player.jsp").forward(request, response);
		} catch (UserException e) {
			request.getRequestDispatcher("player.jsp").forward(request, response);
		}
		
	}

}
