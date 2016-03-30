package edu.asupoly.ser422.servlets;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.List;
import java.util.Iterator;
import java.io.PrintWriter;
import java.io.IOException;

import edu.asupoly.ser422.model.Author;
import edu.asupoly.ser422.services.BooktownService;
import edu.asupoly.ser422.services.BooktownServiceFactory;

@SuppressWarnings("serial")
public class BooktownServlet extends javax.servlet.http.HttpServlet
{
	public void doGet (HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{
		HttpSession sess = req.getSession(true);
		
		BooktownService bookstore = null;
		String defaultViewUrl = "/default_view";
		String deleteViewUrl = "/delete_view";
		String createViewUrl = "/create_view";
		String errorViewUrl = "/error_view";
		String exceptionViewUrl = "/exception_view";
		
		try {
			bookstore = BooktownServiceFactory.getInstance();
			if (bookstore == null) {
				req.getRequestDispatcher(errorViewUrl).forward(req, res);
			}
		}
		catch (Throwable t) {
			sess.setAttribute("throwable", t);
			req.getRequestDispatcher(exceptionViewUrl).forward(req, res);
		}
		
		String action = req.getParameter("action");
		int authorId = -1;
		
		if (action != null && action.equalsIgnoreCase("delete")) {
			authorId = Integer.parseInt(req.getParameter("authorid"));
			boolean deleteResult = bookstore.deleteAuthor(authorId);
			sess.setAttribute("bookstore", bookstore);
			sess.setAttribute("deleteResult", deleteResult);
			sess.setAttribute("authorId", authorId);
			
			req.getRequestDispatcher(deleteViewUrl).forward(req, res);
		}
		else if (action != null && action.equalsIgnoreCase("create")) {
			String lname = req.getParameter("lastname");
			String fname = req.getParameter("firstname");
			
			if (lname != null && fname != null && lname.length() > 0 && fname.length() > 0) {
				authorId = bookstore.createAuthor(lname, fname);
			}
			sess.setAttribute("bookstore", bookstore);
			sess.setAttribute("lname", lname);
			sess.setAttribute("fname", fname);
			sess.setAttribute("authorId", authorId);
			
			req.getRequestDispatcher(createViewUrl).forward(req, res);
		}
		else {
			sess.setAttribute("bookstore", bookstore);
		
			// No matter what the command was, we display the list of authors and a create form
			// Call a service to get the list of authors
			req.getRequestDispatcher(defaultViewUrl).forward(req, res);
		}
	}
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		response.sendError(405, "HTTP method POST is not supported by this URL" );
	}
}
