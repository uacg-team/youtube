package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.utils.Resources;


@WebServlet("/img")
public class ImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			String relativePath = request.getParameter("path");
			Long id =  Long.valueOf(request.getParameter("userId"));
			if (relativePath != null && id != null) {
				Resources.readAvatar(relativePath, id, response);
				System.out.println(relativePath);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
