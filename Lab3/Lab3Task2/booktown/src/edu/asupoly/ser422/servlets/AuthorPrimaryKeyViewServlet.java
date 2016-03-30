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
public class AuthorPrimaryKeyViewServlet extends javax.servlet.http.HttpServlet
{
	public void doGet (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		HttpSession sess = req.getSession();
		String callType = (String) sess.getAttribute("callType");
		
		// some generic setup - our content type and output stream
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		// We'll put all our page contents in this buffer
		StringBuffer pageBuffer = new StringBuffer();
		pageBuffer.append("<html>\n<head>\n<title>Get Booktown Information</title>\n</head>\n<body>\n");

		if (callType == "findAuthorByKey") {
			AuthorVO authorVO = (AuthorVO) sess.getAttribute("authorVO");
			if (authorVO == null) {
				pageBuffer.append("No authors found!!!<br/>\n");
			}
			else {
				pageBuffer.append("Author Last Name: ");
				pageBuffer.append(authorVO.getAuthorLastName());
				pageBuffer.append("<br/>Author First Name: ");
				pageBuffer.append(authorVO.getAuthorFirstName());
			}
			sess.removeAttribute("authorVO");
			sess.removeAttribute("callType");
		}
		else if (callType == "findBookByKey") {
			BookVO bookVO = (BookVO) sess.getAttribute("bookVO");
			if (bookVO == null) {
				pageBuffer.append("No books found!!!<br/>\n");
			}
			else {
				pageBuffer.append("Book isbn: ");
				pageBuffer.append(bookVO.getBookISBN());
				pageBuffer.append("<br/>Book Publisher: ");
				pageBuffer.append(" " + bookVO.getBookPublisher());
				pageBuffer.append("<br/>Book Title: ");
				pageBuffer.append(" " + bookVO.getBookTitle());
				pageBuffer.append("<br/>Year: ");
				pageBuffer.append(" " + bookVO.getBookYear());
			}
			sess.removeAttribute("bookVO");
			sess.removeAttribute("callType");
		}
		else if (callType == "findBooksByTitle" || callType == "getAllBooksAndAuthors") {
			HashMap<String, ArrayList<String>> authorBookMap = 
					(HashMap<String, ArrayList<String>>) sess.getAttribute("authorBookMap");
			if (authorBookMap == null || authorBookMap.size() == 0) {
				if (callType == "getAllBooksAndAuthors") {
					pageBuffer.append("No books ro Authors found!!!<br/>\n");
				}
				else {
					pageBuffer.append("No books found with the given title!!!<br/>\n");
				}
			}
			else {
				pageBuffer.append("Books Information: <br/>");
				
				for ( String book_title : authorBookMap.keySet() ) {
					ArrayList<String> authorList = (ArrayList<String>) authorBookMap.get(book_title);
					pageBuffer.append(book_title + "<br/>");
					
					for (String authorname : authorList) {
						pageBuffer.append(authorname + "<br/>");
					}
					pageBuffer.append("<br/>");
				}
			}
			sess.removeAttribute("authorBookMap");
			sess.removeAttribute("callType");
		}
		pageBuffer.append("</body></html>");
		out.println(pageBuffer.toString());
	}
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		response.sendError(405, "HTTP method POST is not supported by this URL" );
	}
}
