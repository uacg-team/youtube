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


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("register.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		User u = null;
		try {
			u = new User(username, password, email);
		} catch (UserException e) {
			request.setAttribute("userError", e.getMessage());
			request.getRequestDispatcher("register.jsp").forward(request, response);
			return;
		}

		try {
			if (UserDao.getInstance().existsUser(u.getUsername())) {
				request.setAttribute("userError", "This username already exist");
				request.getRequestDispatcher("register.jsp").forward(request, response);
			} else {
				UserDao.getInstance().createUser(u);
				request.getSession().setAttribute("user", u);
				response.sendRedirect("main");
				//request.getRequestDispatcher("main.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			request.setAttribute("userError", "SQLException: " + e.getMessage());
			request.getRequestDispatcher("register.jsp").forward(request, response);
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
