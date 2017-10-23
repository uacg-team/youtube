package controllers.users;

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
import model.exceptions.user.UserNotFoundException;

@WebServlet("/updateUser")
public class UpdateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("updateUser.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User u = (User) request.getSession().getAttribute("user");

		String firstName = request.getParameter("firstName").equals("") ? null : request.getParameter("firstName");
		String lastName = request.getParameter("lastName").equals("")  ? null : request.getParameter("lastName");
		String facebook = request.getParameter("facebook").equals("")  ? null : request.getParameter("facebook");
		String gender = request.getParameter("gender") == "null" ? null : request.getParameter("gender");

		try {
			if (firstName != null) {
				u.setFirstName(firstName);
			}
			if (lastName != null) {
				u.setLastName(lastName);
			}
			if (facebook != null) {
				u.setFacebook(facebook);
			}

			u.setGender(gender);
			
			UserDao.getInstance().updateUser(u);
		} catch (UserException e) {
			e.printStackTrace();
			request.setAttribute("UserException", e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("SQLException", e.getMessage());
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			request.setAttribute("UserNotFoundException", e.getMessage());
		} finally {
			request.getRequestDispatcher("updateUser.jsp").forward(request, response);
		}
	}

}
