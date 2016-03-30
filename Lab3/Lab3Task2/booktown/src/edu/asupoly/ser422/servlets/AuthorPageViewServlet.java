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
public class AuthorPageViewServlet extends javax.servlet.http.HttpServlet
{
	public void doPost (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		HttpSession sess = req.getSession();
		String operationResult = (String) sess.getAttribute("operationResult");
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
				pageBuffer.append(authorVO.getAuthorId() + " " + authorVO.getAuthorFirstName() + " " + authorVO.getAuthorLastName()+ "<br/>");
			}
		}
		
		if (operationResult != null && operationResult != "") {
			pageBuffer.append(operationResult + "<br/><br/>");
		}
		
		pageBuffer.append("<br/>Author_id: ");
		pageBuffer.append("<input type=\"text\" id=\"author_id\" name=\"author_id\"><br/><br/>");
		pageBuffer.append("First name: ");
		pageBuffer.append("<input type=\"text\" id=\"firstname\" name=\"firstname\"><br/><br/>");
		pageBuffer.append("Last name: ");
		pageBuffer.append("<input type=\"text\" id=\"lastname\" name=\"lastname\"><br/><br/>");
		
		pageBuffer.append("<input type=\"submit\" value=\"add\" formaction=\"./author_page?action=add\"> ");
		pageBuffer.append("<input type=\"submit\" value=\"update\" formaction=\"./author_page?action=update\"> ");
		pageBuffer.append("<input type=\"submit\" value=\"delete\" formaction=\"./author_page?action=delete\"> </form>");
		
		pageBuffer.append("</body></html>");
		out.println(pageBuffer.toString());
	}
	
	public void doGet (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		doPost(req, res);
	}
}
