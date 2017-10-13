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
import model.exceptions.user.UserException;
import model.utils.DBConnection;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		User u = null;
		try {
			u = new User(username, password, email);
		} catch (UserException e) {
			e.printStackTrace();
		} 

		try {
			if (UserDao.getInstance().existsUser(u)) {
				response.getWriter().append("user with email ").append(u.getEmail()).append(" exists");
			} else {
				UserDao.getInstance().insertUser(u);
				response.getWriter().append("Well done, you registerred with id = " + u.getUser_id());
			}
		} catch (SQLException e) {
			response.getWriter().append("SQLException: " + e.getMessage());
		} catch (UserException e) {
			response.getWriter().append("UserException: " + e.getMessage());
		}
	}

	@Override
	public void destroy() {
		try {
			DBConnection.CON1.closeConnection();
		} catch (SQLException e) {
			System.out.println("cannot close connection");
			e.printStackTrace();
		}
		super.destroy();
	}

}
