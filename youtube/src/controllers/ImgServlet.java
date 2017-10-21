package controllers;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.utils.Resources;


@WebServlet("/img")
public class ImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String path = null;
		if(request.getParameter("path")!=null) {
			path=Resources.ROOT + File.separator + request.getParameter("path");
			try{
				Resources.readFromFile(path, response);
			}catch(IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
