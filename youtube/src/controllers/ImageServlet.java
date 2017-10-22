package controllers;

import java.io.File;
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
import model.utils.Resources;


@WebServlet("/image")
public class ImageServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		String url = u.getAvatarUrl();

		if (url.equals("defaultAvatar.png")) {
			url = Resources.ROOT + File.separator + url;
		} else {
			url = Resources.ROOT + File.separator + u.getUserId() + File.separator + url;
		}

		try (OutputStream out = response.getOutputStream()){
			Path path = Paths.get(url);
			Files.copy(path, out);
			out.flush();
		}
	}
}
