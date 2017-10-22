package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.utils.Resources;


@WebServlet("/img")
public class ImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			String relativePath = request.getParameter("path");
			User u = (User) request.getAttribute("user");
			if (relativePath != null && u != null) {
				Resources.readAvatar(relativePath, u, response);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
