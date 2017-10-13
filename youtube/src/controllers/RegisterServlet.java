package controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.UserDao;
import model.exceptions.user.UsernameEmptyException;
import model.exceptions.user.UsernameLengthException;
import model.exceptions.user.UsernameNullException;
import model.utils.DBConnection;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		// validations
		if (username == null || username.length() < 4) {
			response.getWriter().append("username must be at least 3 symbols");
			return;
		}

		if (password == null || password.length() < 7) {
			response.getWriter().append("password must be at least 6 symbols");
			return;
		}

		if (!isValidEmailAddress(email)) {
			response.getWriter().append("email is not valid");
			return;
		}
		// validations

		User u = null;
		try {
			u = new User(username, password, email);
		} catch (UsernameNullException e) {
			e.printStackTrace();
		} catch (UsernameEmptyException e) {
			e.printStackTrace();
		} catch (UsernameLengthException e) {
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
