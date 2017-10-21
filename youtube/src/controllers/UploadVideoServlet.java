package controllers;

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

@WebServlet("/upload")
@MultipartConfig
public class UploadVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String ROOT = "C:/res";
	public static final String VIDEOS_URL = "/videos";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("upload.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// check if user is logged
		
		User u = (User) request.getSession().getAttribute("user");

		Part newVideo = request.getPart("newVideo");
		String name = request.getParameter("name");
		String fileName = Paths.get(newVideo.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
		InputStream fileContent = newVideo.getInputStream();	
		File file = new File(ROOT+"/userID"+u.getUserId()+VIDEOS_URL,fileName);
		if(!file.exists()) {
			file.mkdirs();
			file.createNewFile();
		}
		
	    Path TO = Paths.get(file.getAbsolutePath());	    
	    
	    Files.copy(fileContent, TO,  StandardCopyOption.REPLACE_EXISTING);
	    
	    fileContent.close();
		
		String[] inputTags = request.getParameter("tags").split("\\s+");
		Set<Tag> tags = new HashSet<>();
		for (String string : inputTags) {
			tags.add(new Tag(string));
		}
		
		Long privacy = Long.valueOf(request.getParameter("privacy"));
		
		Video v = null;
		try {
			v = new Video(name, TO.toString().replace('\\', '/'), privacy, u.getUserId(), tags);
			VideoDao.getInstance().createVideo(v);
		} catch (VideoException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (TagNotFoundException e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("main.jsp").forward(request, response);
	}

}
