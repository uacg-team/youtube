package controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.UserDao;
import model.exceptions.UserNotFoundException;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		try {
			User u = UserDao.getInstance().getUser(username);
			if (u.getPassword().equals(password)) {
				response.getWriter().append("Successfully logged in");
			}else {
				response.getWriter().append("wrong password");
			}
		} catch (SQLException e) {
			response.getWriter().append("SQLException: " + e.getMessage());
		} catch (UserNotFoundException e) {
			response.getWriter().append("User do not exist");
		}
	}

}
