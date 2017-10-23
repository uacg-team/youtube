package controllers.videos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.Tag;
import model.User;
import model.Video;
import model.VideoDao;
import model.exceptions.tags.TagNotFoundException;
import model.exceptions.video.VideoException;
import model.utils.Resources;

@WebServlet("/upload")
@MultipartConfig
public class UploadVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("upload.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// check if user is logged
		
		User u = (User) request.getSession().getAttribute("user");
		if (u == null) {
			response.sendRedirect("login");
		}
		Part newVideo = request.getPart("newVideo");
		String name = request.getParameter("name");
		Resources.writeVideo(u, newVideo);
		
		String[] inputTags = request.getParameter("tags").split("\\s+");
		Set<Tag> tags = new HashSet<>();
		for (String string : inputTags) {
			tags.add(new Tag(string));
		}
		
		try {
			Long privacy = Long.valueOf(request.getParameter("privacy"));
			String fileName = Paths.get(newVideo.getSubmittedFileName()).getFileName().toString();
			Video v = new Video(name, fileName , privacy, u.getUserId(), tags);
			VideoDao.getInstance().createVideo(v);
		} catch (VideoException e) {
			request.getRequestDispatcher("upload.jsp").forward(request, response);
		} catch (SQLException e) {
			request.getRequestDispatcher("upload.jsp").forward(request, response);
		} catch (TagNotFoundException e) {
			request.getRequestDispatcher("upload.jsp").forward(request, response);
		}
		
		request.getRequestDispatcher("main").forward(request, response);
	}

}
