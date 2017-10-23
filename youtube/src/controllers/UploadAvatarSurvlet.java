package controllers;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.User;
import model.UserDao;
import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;
import model.utils.Resources;


@WebServlet("/uploadAvatar")
@MultipartConfig
public class UploadAvatarSurvlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			User u = (User) request.getSession().getAttribute("user");
			Part avatar = request.getPart("avatar");
			if (u != null && avatar != null) {
				Resources.writeImage(u, avatar);
				String fileName = Paths.get(avatar.getSubmittedFileName()).getFileName().toString();
				u.setAvatarUrl(fileName);
				UserDao.getInstance().updateUser(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		response.sendRedirect("updateUser");
	}

}
