package edu.asupoly.ser422.servlets;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.PrintWriter;
import java.io.IOException;

import edu.asupoly.ser422.model.*;
import edu.asupoly.ser422.services.*;
import edu.asupoly.ser422.services.impl.*;

@SuppressWarnings("serial")
public class BookPageDeleteServlet extends javax.servlet.http.HttpServlet
{
	public void doPost (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		HttpSession sess = req.getSession();
		ArrayList<AuthorVO> authorVOList = (ArrayList<AuthorVO>) sess.getAttribute("authorVOList");
		
		// some generic setup - our content type and output stream
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		// We'll put all our page contents in this buffer
		StringBuffer pageBuffer = new StringBuffer();
		pageBuffer.append("<html>\n<head>\n<title>Author Information</title>\n</head>\n<body>\n");
		
		pageBuffer.append("<p><form method=\"post\">\n");
		
		pageBuffer.append("<h2>Author Information</h2><br/>");
		
		if (authorVOList != null && authorVOList.size() > 0) {
			for (AuthorVO authorVO : authorVOList) {
				pageBuffer.append(authorVO.getAuthorId() + " " + authorVO.getAuthorFirstName() + " " +
									authorVO.getAuthorLastName()+ "<br/>");
			}
			pageBuffer.append("Delete the Authors as well?");
			pageBuffer.append("<input type=\"submit\" value=\"deleteWithAuthors\" formaction=\"./book_page?action=deleteWithAuthors\"> ");
			pageBuffer.append("<input type=\"submit\" value=\"deleteWithoutAuthors\" formaction=\"./book_page?action=deleteWithoutAuthors\"> </form>");
		}
		else {
			pageBuffer.append("No books associated with the Author. Click on the delete button to delete authors.");
			pageBuffer.append("<input type=\"submit\" value=\"deleteWithoutAuthors\" formaction=\"./book_page?action=deleteWithoutAuthors\"> </form>");
		}
			
		pageBuffer.append("</body></html>");
		out.println(pageBuffer.toString());
	}
	public void doGet (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		doPost(req, res);
	}
}
