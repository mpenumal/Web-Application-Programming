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
public class ErrorViewServlet extends javax.servlet.http.HttpServlet
{
	public void doGet (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		// some generic setup - our content type and output stream
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		// serious problem, perhaps a config error. For now punt.
		out.println("Servlet execution error: The bookstore is closed.");
	}
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		response.sendError(405, "HTTP method POST is not supported by this URL" );
	}
}
