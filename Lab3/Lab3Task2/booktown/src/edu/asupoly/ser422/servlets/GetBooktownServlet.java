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
public class GetBooktownServlet extends javax.servlet.http.HttpServlet
{
	public void doGet (HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{
		HttpSession sess = req.getSession(true);
		
		AuthorDAO authorDAO = new AuthorDAORdbmsImpl();
		BookDAO bookDAO = new BookDAORdbmsImpl();
		String authorPrimaryKeyViewUrl = "/author_primary_key_view";
		
		String author_id = req.getParameter("id");
        String isbn = req.getParameter("isbn");
        String title = req.getParameter("title");
        int id = -1;
        
        if ((author_id != null && isbn != null) || (title != null && isbn != null)
        		|| (author_id != null && title != null) || 
        		(title != null && isbn != null && author_id != null)) {
        	res.sendError(400, "Atmost one parameter allowed.");
        }

        if (author_id != null) {
        	id = Integer.parseInt(author_id);
        	AuthorVO authorVO = authorDAO.findByPrimaryKey(id);
        	sess.setAttribute("authorVO", authorVO);
			sess.setAttribute("callType", "findAuthorByKey");
        }

        else if (isbn != null) {
        	BookVO bookVO = bookDAO.findByPrimaryKey(isbn);
        	sess.setAttribute("bookVO", bookVO);
			sess.setAttribute("callType", "findBookByKey");
        }
        else if (title != null) {
        	HashMap<String, ArrayList<String>> authorBookMap = bookDAO.findBooksByTitle(title);
        	sess.setAttribute("authorBookMap", authorBookMap);
			sess.setAttribute("callType", "findBooksByTitle");
        }
        else {
        	HashMap<String, ArrayList<String>> authorBookMap = bookDAO.getAllBooksAndAuthors();
        	sess.setAttribute("authorBookMap", authorBookMap);
			sess.setAttribute("callType", "getAllBooksAndAuthors");
        }
        
        req.getRequestDispatcher(authorPrimaryKeyViewUrl).forward(req, res);
	}
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		response.sendError(405, "HTTP method POST is not supported by this URL" );
	}
}
