package edu.asupoly.ser422.servlets;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.PrintWriter;
import java.io.IOException;

import edu.asupoly.ser422.model.*;
import edu.asupoly.ser422.services.*;
import edu.asupoly.ser422.services.impl.*;

@SuppressWarnings("serial")
public class AuthorPageServlet extends javax.servlet.http.HttpServlet
{
	public void doPost (HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{
		HttpSession sess = req.getSession(true);
		AuthorDAO authorDAO = new AuthorDAORdbmsImpl();
		BookDAO bookDAO = new BookDAORdbmsImpl();
		ArrayList<AuthorVO> authorVOList = new ArrayList<AuthorVO>(); 
		String AuthorPageViewUrl = "/author_page_view";
		String AuthorPageDeleteUrl = "/author_page_delete";
		String action = req.getParameter("action");
		int authorId = -1;
		
		authorVOList = authorDAO.getAllAuthors();
		if (authorVOList != null && authorVOList.size() > 0) {
			sess.setAttribute("authorVOList", authorVOList);
		}
		
		if (action != null && action.equalsIgnoreCase("add")) {
			AuthorVO authorVO = new AuthorVO(Integer.parseInt(req.getParameter("author_id")));
			authorVO.setAuthorFirstName(req.getParameter("firstname"));
            authorVO.setAuthorLastName(req.getParameter("lastname"));
			
			authorDAO.insert(authorVO);
			sess.setAttribute("operationResult", "Insert Operation Completed.");
			
			req.getRequestDispatcher(AuthorPageViewUrl).forward(req, res);
		}
		else if (action != null && action.equalsIgnoreCase("update")) {
			AuthorVO authorVO = new AuthorVO(Integer.parseInt(req.getParameter("author_id")));
			authorVO.setAuthorFirstName(req.getParameter("firstname"));
            authorVO.setAuthorLastName(req.getParameter("lastname"));
			
			authorDAO.update(authorVO);
			sess.setAttribute("operationResult", "Update Operation Completed.");
			
			req.getRequestDispatcher(AuthorPageViewUrl).forward(req, res);
		}
		else if (action != null && action.equalsIgnoreCase("delete")) {
			AuthorVO authorVO = new AuthorVO(Integer.parseInt(req.getParameter("author_id")));
			authorVO.setAuthorFirstName(req.getParameter("firstname"));
            authorVO.setAuthorLastName(req.getParameter("lastname"));
            
            ArrayList<BookVO> bookVOList = bookDAO.findBooksByAuthorID(authorVO.getAuthorId());
			sess.setAttribute("authorVO", authorVO);
			sess.setAttribute("bookVOList", bookVOList);
			
			req.getRequestDispatcher(AuthorPageDeleteUrl).forward(req, res);
		}
		else if (action != null && action.equalsIgnoreCase("deletewithbooks")) {
			AuthorVO authorVO = (AuthorVO) sess.getAttribute("authorVO");
			ArrayList<BookVO> bookVOList = (ArrayList<BookVO>) sess.getAttribute("bookVOList");
			
			for (BookVO bookVO : bookVOList) {
				bookDAO.delete(bookVO);
			}
			authorDAO.delete(authorVO);
			sess.setAttribute("operationResult", "Delete Operation Completed.");
			sess.removeAttribute("authorVO");
			sess.removeAttribute("bookVOList");
			
			req.getRequestDispatcher(AuthorPageViewUrl).forward(req, res);
		}
		else if (action != null && action.equalsIgnoreCase("deletewithoutbooks")) {
			AuthorVO authorVO = (AuthorVO) sess.getAttribute("authorVO");
			
			authorDAO.delete(authorVO);
			sess.setAttribute("operationResult", "Delete Operation Completed.");
			sess.removeAttribute("authorVO");
			sess.removeAttribute("bookVOList");
			
			req.getRequestDispatcher(AuthorPageViewUrl).forward(req, res);
		}
		else {
			req.getRequestDispatcher(AuthorPageViewUrl).forward(req, res);
		}
		
	}
	
	public void doGet (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		doPost(req, res);
	}
}
