package controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Playlist;
import model.PlaylistDao;
import model.User;
import model.exceptions.playlists.PlaylistException;
import model.exceptions.user.UserException;

@WebServlet("/playlist")
public class PlaylistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO forward to playlist preview
		request.getRequestDispatcher("playlist.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("user")==null) {
			response.sendRedirect("login");
			return;
		}
		switch(request.getParameter("m")) {
		case "createPlaylist":
			createPlaylist(request, response);
			break;
		case "updatePlaylist":
			break;
		case "addToPlaylist":
			break;
		default:
			break;
		}
		doGet(request, response);
	}
	/**
	 * waiting for parameter <b>newPlaylist</b> parameter,must be logged
	 * @param request
	 * @param response
	 */
	private static void createPlaylist(HttpServletRequest request, HttpServletResponse response) {
		long userId=((User)request.getSession().getAttribute("user")).getUserId();
		String playlistName = request.getParameter("newPlaylist");
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
	
}
