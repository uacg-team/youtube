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
import model.exceptions.user.UserNotFoundException;

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
				request.getRequestDispatcher("home.html").forward(request, response);
			} else {
				request.getRequestDispatcher("login.html").forward(request, response);
			}
		} catch (SQLException e) {
			response.getWriter().append("SQLException: " + e.getMessage());
		} catch (UserNotFoundException e) {
			request.getRequestDispatcher("login.html").forward(request, response);
		}
	}

}
