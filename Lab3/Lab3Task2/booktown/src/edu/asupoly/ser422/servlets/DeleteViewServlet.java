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
public class DeleteViewServlet extends javax.servlet.http.HttpServlet
{
	public void doGet (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		HttpSession sess = req.getSession();
		BooktownService bookstore = (BooktownService) sess.getAttribute("bookstore");
		boolean deleteResult = (boolean) sess.getAttribute("deleteResult");
		int authorId = (int) sess.getAttribute("authorId");
		
		// some generic setup - our content type and output stream
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		// We'll put all our page contents in this buffer
		StringBuffer pageBuffer = new StringBuffer();
		pageBuffer.append("<html>\n<head>\n<title>Booktown Servlet</title>\n</head>\n<body>\n<H2>Authors:</H2>\n");
		
		if (deleteResult) {
			pageBuffer.append("Author deleted: " + authorId + "<br/><br/>\n");
		}
		else {
			pageBuffer.append("Could not delete author with id " + authorId + "<br/><br/>\n");
		}
		
		List<Author> authorsList = bookstore.getAuthors();

		if (authorsList == null || authorsList.size() == 0) {
			pageBuffer.append("No authors found!!!<br/>\n");
		}
		else {
			// finally, we have some authors
			pageBuffer.append("\n<table>\n");
			Author nextAuthor = null;
			Iterator<Author> authorIter = authorsList.iterator();
			while (authorIter.hasNext()) {
				nextAuthor = (Author)authorIter.next();
				pageBuffer.append("\n<tr><td>");
				pageBuffer.append(nextAuthor.getLastName());
				pageBuffer.append("</td><td>");
				pageBuffer.append(nextAuthor.getFirstName());
				pageBuffer.append("</td><td><a href=\"./booktown?action=delete&authorid="+nextAuthor.getAuthorID()+"\">delete</a></td></tr>\n");
			}
			pageBuffer.append("</table>\n<br/>\n");
		}
		// Now display a form for create an author
		pageBuffer.append("<p><form action=\"./booktown\" method=\"get\">\n");
		pageBuffer.append("<input type=\"hidden\" name=\"action\" value=\"create\"/>\n");
		pageBuffer.append("Last name: <input type=\"text\" size=\"13\" name=\"lastname\"/><br/>");
		pageBuffer.append("First name: <input type=\"text\" size=\"12\" name=\"firstname\"/><br/>");  
		pageBuffer.append("<button type=\"submit\">Create author</button><br/></form><br/>");

		pageBuffer.append("</body></html>");
		
		sess.removeAttribute("bookstore");
		sess.removeAttribute("deleteResult");
		sess.removeAttribute("authorId");
		
		out.println(pageBuffer.toString());
	}
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		response.sendError(405, "HTTP method POST is not supported by this URL" );
	}
}
