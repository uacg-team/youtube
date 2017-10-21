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

import model.utils.Resources;

@WebServlet("/video")
public class VideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getParameter("url");
		String userId = request.getParameter("userId");

		url = Resources.ROOT + File.separator + userId + File.separator + "videos" + File.separator + url;

		try (OutputStream out = response.getOutputStream()){
			Path path = Paths.get(url);
			Files.copy(path, out);
			out.flush();
		}
	}

}
