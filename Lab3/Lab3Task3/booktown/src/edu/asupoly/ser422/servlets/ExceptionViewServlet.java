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
public class ExceptionViewServlet extends javax.servlet.http.HttpServlet
{
	public void doGet (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		HttpSession sess = req.getSession();
		Throwable t = (Throwable) sess.getAttribute("throwable");
		
		// some generic setup - our content type and output stream
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		// We'll put all our page contents in this buffer
		StringBuffer pageBuffer = new StringBuffer();
		pageBuffer.append("<html>\n<head>\n<title>Booktown Servlet</title>\n</head>\n<body>\n<H2>Authors:</H2>\n");
		
		// you at least want to log these in a generic logger somewhere
		// but for now we'll dump to the screen...
		pageBuffer.append("Error while trying to retrieve authors!!!\n<br/>\n");
		pageBuffer.append("<pre>\n");
		pageBuffer.append(t.getMessage());
		pageBuffer.append("\n</pre>");
		
		pageBuffer.append("</body></html>");
		
		sess.removeAttribute("throwable");
		
		out.println(pageBuffer.toString());
	}
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		response.sendError(405, "HTTP method POST is not supported by this URL" );
	}
}
