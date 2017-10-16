package controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if ((boolean) req.getSession().getAttribute("logged")) {
			PrintWriter pw = resp.getWriter();
			pw.println("I am home");
			HttpSession session = req.getSession();
			String username = session.getAttribute("username").toString();
			if (username != null) {
				pw.println("Username: " + username);
			}
			pw.print("html...");

			session.invalidate();

			req.getRequestDispatcher("home.html").forward(req, resp);
		} else {
			resp.sendRedirect("index.html");
		}

	}

}
