package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Playlist;
import model.PlaylistDao;
import model.User;
import model.Video;
import model.exceptions.playlists.PlaylistException;
import model.exceptions.user.UserException;

@WebServlet("/playlist")
public class PlaylistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO forward to playlist preview
		if(request.getSession().getAttribute("user")==null) {
			response.sendRedirect("login");
			return;
		}
		User user = (User)request.getSession().getAttribute("user");
		long userId = user.getUserId();
		if(request.getParameter("v")!=null) {
			switch(request.getParameter("v")) {
			case "loadPlaylist":
				//requestScope.myPlaylists
				loadPlaylistForUser(request, userId);
				break;
			case "loadVideos":
				//requestScope.
				String playlistName =request.getParameter("playlistName");
				loadVideosForPlaylist(request,userId,playlistName);
				request.getRequestDispatcher("playlistVideos.jsp").forward(request, response);
				return;
			default:
				break;
			}
		}
		request.getRequestDispatcher("playlist.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("user")==null) {
			response.sendRedirect("login");
			return;
		}
		switch(request.getParameter("m")) {
		case "createPlaylist":
			long userId=((User)request.getSession().getAttribute("user")).getUserId();
			String playlistName = request.getParameter("newPlaylist");
			createPlaylist(request, userId,playlistName);
			//TODO go to page...
			break;
		case "updatePlaylist":
			break;
		case "addToPlaylist":
			
			break;
		default:
			break;
		}
	}
	/**
	 * @param request
	 * @param response
	 */
	private static void createPlaylist(HttpServletRequest request, long userId,String playlistName) {
		Playlist playlist =null;
		try {
			playlist = new Playlist(playlistName, userId);
		} catch (PlaylistException e) {
			// TODO handle
			e.printStackTrace();
			return;
		}
		try {
			PlaylistDao.getInstance().createPlaylist(playlist);
		} catch (PlaylistException e) {
			// TODO handle
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO handle
			e.printStackTrace();
		} catch (UserException e) {
			// TODO handle
			e.printStackTrace();
		}
	}
	
	private static void updatePlaylist(HttpServletRequest request, HttpServletResponse response) {
		//TODO
		
	}
	private static void deletePlaylist() {
		//TODO
		
	}
	private static void addToPlaylist() {
		//TODO
	}
	private static void removeFromPlaylist() {
		//TODO
	}
	
	public static void loadPlaylistForUser(HttpServletRequest request,long userId) {
		List<Playlist> playlists = null;
		try {
			playlists=PlaylistDao.getInstance().getPlaylistForUser(userId);
		} catch (UserException e) {
			// TODO handle
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			// TODO handle
			e.printStackTrace();
			return;
		}
		request.setAttribute("myPlaylists", playlists);
	}
	
	private static void loadVideosForPlaylist(HttpServletRequest request,long userId,String playlistName) {
		try {
			Playlist playlist = PlaylistDao.getInstance().getPlaylist(userId, playlistName);
			PlaylistDao.getInstance().loadVideosInPlaylist(playlist);
			request.setAttribute("videos", playlist.getVideos());
		} catch (UserException e) {
			// TODO handle
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO handle
			e.printStackTrace();
		} catch (PlaylistException e) {
			// TODO handle
			e.printStackTrace();
		}
		
	}
	
}
