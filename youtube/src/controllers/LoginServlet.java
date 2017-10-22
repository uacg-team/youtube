package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.UserDao;
import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;
import model.utils.Hash;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("user") != null) {
			response.sendRedirect("main");
		}else {
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = Hash.getHashPass(request.getParameter("password"));
		try {
			User u = UserDao.getInstance().getUser(username);
			if (password.equals(u.getPassword())) {
				request.getSession().setMaxInactiveInterval(-1);
				request.getSession().setAttribute("user", u);
				response.sendRedirect("main");
			} else {
				request.setAttribute("passwordError", "Wrong Password");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} catch (UserNotFoundException e) {
			request.setAttribute("usernameError", e.getMessage());
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} catch (UserException e) {
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} 
	}
}
