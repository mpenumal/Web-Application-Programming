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
public class BookPageViewServlet extends javax.servlet.http.HttpServlet
{
	public void doPost (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		HttpSession sess = req.getSession();
		String operationResult = (String) sess.getAttribute("operationResult");
		ArrayList<AuthorVO> authorVOList = (ArrayList<AuthorVO>) sess.getAttribute("authorVOList");
		HashMap<String, ArrayList<String>> authorBookMap = 
				(HashMap<String, ArrayList<String>>) sess.getAttribute("authorBookMap");
		
		// some generic setup - our content type and output stream
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		// We'll put all our page contents in this buffer
		StringBuffer pageBuffer = new StringBuffer();
		pageBuffer.append("<html>\n<head>\n<title>Books Information</title>\n</head>\n<body>\n");
		pageBuffer.append("<script>");
		pageBuffer.append("</script>");
		pageBuffer.append("<p><form method=\"post\">\n");

		pageBuffer.append("<h2>Books Information</h2><br/>");
		
		if (authorBookMap != null && authorBookMap.size() > 0) {
			pageBuffer.append("Books List: <br/>");
			
			for ( String book_title : authorBookMap.keySet() ) {
				ArrayList<String> authorList = (ArrayList<String>) authorBookMap.get(book_title);
				pageBuffer.append(book_title + "<br/>");
				
				if (authorList != null && authorList.size() > 0)
				for (String authorname : authorList) {
					pageBuffer.append(authorname + "<br/>");
				}
				pageBuffer.append("<br/>");
			}
		}
		
		if (authorVOList != null && authorVOList.size() > 0) {
			pageBuffer.append("Authors List: <br/>");
			for (AuthorVO authorVO : authorVOList) {
				pageBuffer.append(authorVO.getAuthorId() + " " + authorVO.getAuthorFirstName() + " " + authorVO.getAuthorLastName()+ "<br/>");
			}
		}
		
		if (operationResult != null && operationResult != "") {
			pageBuffer.append(operationResult + "<br/><br/>");
		}
	
		pageBuffer.append("<br/>isbn: ");
		pageBuffer.append("<input type=\"text\" id=\"isbn\" name=\"isbn\"><br/><br/>");
		pageBuffer.append("publisher: ");
		pageBuffer.append("<input type=\"text\" id=\"publisher\" name=\"publisher\"><br/><br/>");
		pageBuffer.append("title: ");
		pageBuffer.append("<input type=\"text\" id=\"title\" name=\"title\"><br/><br/>");
		pageBuffer.append("year: ");
		pageBuffer.append("<input type=\"text\" id=\"year\" name=\"year\"><br/><br/>");
		pageBuffer.append("author_id: ");
		pageBuffer.append("<input type=\"text\" id=\"author_id\" name=\"author_id\"><br/><br/>");
		
		pageBuffer.append("<input type=\"submit\" value=\"add\" formaction=\"./book_page?action=add\"> ");
		pageBuffer.append("<input type=\"submit\" value=\"update\" formaction=\"./book_page?action=update\"> ");
		pageBuffer.append("<input type=\"submit\" value=\"deleteAuthor\" formaction=\"./book_page?action=deleteAuthor\"> ");
		pageBuffer.append("<input type=\"submit\" value=\"delete\" formaction=\"./book_page?action=delete\"> </form>");
		
		pageBuffer.append("</body></html>");
		out.println(pageBuffer.toString());
		sess.removeAttribute("operationResult");
		sess.removeAttribute("authorVOList");
		sess.removeAttribute("authorBookMap");
	}
	
	public void doGet (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		doPost(req, res);
	}
}
