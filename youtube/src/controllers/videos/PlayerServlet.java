package controllers.videos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac;

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
		String videoURL = request.getParameter("url");
	
		try {
			VideoDao.getInstance().increaseViews(videoURL);
			System.out.println("PlayerServlet:videoURL:"+videoURL);
			Video video = VideoDao.getInstance().getVideo(videoURL);
			request.setAttribute("mainVideo", video);
			User videoOwner =  UserDao.getInstance().getUser(video.getUserId());
			request.setAttribute("videoOwner", videoOwner);
			
			int likes = VideoDao.getInstance().getLikes(video.getVideoId());
			request.setAttribute("likes", likes);
		
			int disLikes = VideoDao.getInstance().getDisLikes(video.getVideoId());
			request.setAttribute("disLikes", disLikes);
		
			Set<Video> related = VideoDao.getInstance().getRelatedVideos(video.getLocationUrl());
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
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
