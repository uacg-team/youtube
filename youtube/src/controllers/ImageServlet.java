package controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;


@WebServlet("/image")
public class ImageServlet extends HttpServlet {

	public static final String AVATAR_URL = "C:/videos/";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User)request.getSession().getAttribute("user");
		String url = u.getAvatarUrl();
		
		if (url == null || url.isEmpty()) {
			url = "defaultAvatar.png";
		}
		url = AVATAR_URL+url;
		
		try (OutputStream out = response.getOutputStream()){
			Path path = Paths.get(url);
			Files.copy(path, out);
			out.flush();
		}
	}
}
