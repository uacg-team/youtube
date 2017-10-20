package controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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


@WebServlet("/uploadAvatar")
@MultipartConfig
public class UploadAvatarSurvlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	public static final String AVATARS_URL = "C:/videos/";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User u = (User) request.getSession().getAttribute("user");
		Part avatar = request.getPart("avatar");
		String fileName = Paths.get(avatar.getSubmittedFileName()).getFileName().toString();
		
		InputStream fileContent = avatar.getInputStream();	
		File file = new File(AVATARS_URL,fileName);
		if(!file.exists()) {
			file.createNewFile();
		}
	    Path TO = Paths.get(AVATARS_URL+fileName);	    
	    Files.copy(fileContent, TO,  StandardCopyOption.REPLACE_EXISTING);
	    fileContent.close();
	    
	    u.setAvatarUrl(fileName);
	    try {
			UserDao.getInstance().updateUser(u);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	    response.sendRedirect("updateUser");
//	    resquest.getRequestDispatcher("updateUser.jsp").forward(request, response);
	}

}
