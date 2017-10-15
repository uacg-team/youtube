package controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.UserDao;
import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;
import model.utils.Hash;

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

		
		request.getSession().invalidate();
		try {
			
			User u = UserDao.getInstance().getUser(username);
			
			
			System.out.println(password);
			System.out.println(u);
			
			
			
			if (Hash.equals(password, u.getPassword())) {
				response.sendRedirect("home.html");
			} else {
				System.out.println("wrong pass");
				response.sendRedirect("login.html");
			}
		} catch (SQLException e) {
			response.getWriter().append("SQLException: " + e.getMessage());
		} catch (UserNotFoundException e) {
			System.out.println("UserNotFoundException");
			response.sendRedirect("login.html");
		} catch (UserException e) {
			response.getWriter().append("UserException: " + e.getMessage());
		}
	}

}
