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
public class BookPageServlet extends javax.servlet.http.HttpServlet
{
	public void doPost (HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{
		HttpSession sess = req.getSession(true);
		AuthorDAO authorDAO = new AuthorDAORdbmsImpl();
		BookDAO bookDAO = new BookDAORdbmsImpl();
		String BookPageViewUrl = "/book_page_view";
		String BookPageDeleteUrl = "/book_page_delete";
		String action = req.getParameter("action");
		int authorId = -1;
		
		ArrayList<AuthorVO> authorVOList = authorDAO.getAllAuthors();
		HashMap<String, ArrayList<String>> authorBookMap = bookDAO.getAllBooksAndAuthors();
    	
		if (authorVOList != null && authorVOList.size() > 0) {
			sess.setAttribute("authorVOList", authorVOList);
		}
		if (authorBookMap != null && authorBookMap.size() > 0) {
			sess.setAttribute("authorBookMap", authorBookMap);
		}
		
		if (action != null && action.equalsIgnoreCase("add")) {
			String isbn = req.getParameter("isbn");
			BookVO bookVO = new BookVO(isbn);
			bookVO.setBookTitle(req.getParameter("title"));
			bookVO.setBookPublisher(req.getParameter("publisher"));
			bookVO.setBookYear(Integer.parseInt(req.getParameter("year")));
			String year = req.getParameter("year");
			if (year != null && year != "") {
				bookVO.setBookYear(Integer.parseInt(year));
			}
			
			if (isbn != null && isbn != "") {
				bookDAO.insert(bookVO);
				
				String id = req.getParameter("author_id");
				if (id != null && id != "") {
					int author_id = Integer.parseInt(id);
					bookDAO.insertAuthorJoin(author_id, isbn);
				}
				sess.setAttribute("operationResult", "Insert Operation Completed.");
			}
			else {
				sess.setAttribute("operationResult", "Please enter isbn.");
			}
			req.getRequestDispatcher(BookPageViewUrl).forward(req, res);
		}
		else if (action != null && action.equalsIgnoreCase("update")) {
			String isbn = req.getParameter("isbn");
			BookVO bookVO = new BookVO(isbn);
			bookVO.setBookTitle(req.getParameter("title"));
			bookVO.setBookPublisher(req.getParameter("publisher"));
			bookVO.setBookYear(Integer.parseInt(req.getParameter("year")));
			String year = req.getParameter("year");
			if (year != null && year != "") {
				bookVO.setBookYear(Integer.parseInt(year));
			}
			
			if (isbn != null && isbn != "") {
				bookDAO.update(bookVO);
				
				String id = req.getParameter("author_id");
				if (id != null && id != "") {
					int author_id = Integer.parseInt(id);
					bookDAO.insertAuthorJoin(author_id, isbn);
				}
				sess.setAttribute("operationResult", "Update Operation Completed.");
			}
			else {
				sess.setAttribute("operationResult", "Please enter isbn.");
			}
			req.getRequestDispatcher(BookPageViewUrl).forward(req, res);
		}
		else if (action != null && action.equalsIgnoreCase("deleteAuthor")) {
			String isbn = req.getParameter("isbn");
			String id = req.getParameter("author_id");
			if (id != null && id != "") {
				int author_id = Integer.parseInt(id);
				if (isbn != null && isbn != "") {
					bookDAO.deleteAuthorJoin(author_id, isbn);
				}
			}
			req.getRequestDispatcher(BookPageDeleteUrl).forward(req, res);
		}
		else if (action != null && action.equalsIgnoreCase("delete")) {
			String isbn = req.getParameter("isbn");
			BookVO bookVO = new BookVO(isbn);
			bookVO.setBookTitle(req.getParameter("title"));
			bookVO.setBookPublisher(req.getParameter("publisher"));
			bookVO.setBookYear(Integer.parseInt(req.getParameter("year")));
			String year = req.getParameter("year");
			if (year != null && year != "") {
				bookVO.setBookYear(Integer.parseInt(year));
			}
            
            ArrayList<AuthorVO> authorList = bookDAO.findAuthorsByBookID(isbn);
			sess.setAttribute("bookVO", bookVO);
			sess.setAttribute("authorVOList", authorList);
			
			req.getRequestDispatcher(BookPageDeleteUrl).forward(req, res);
		}
		else if (action != null && action.equalsIgnoreCase("deleteWithAuthors")) {
			BookVO bookVO = (BookVO) sess.getAttribute("bookVO");
			ArrayList<AuthorVO> authorList = (ArrayList<AuthorVO>) sess.getAttribute("authorVOList");
			
			for (AuthorVO authorVO : authorList) {
				authorDAO.delete(authorVO);
			}
			bookDAO.delete(bookVO);
			sess.setAttribute("operationResult", "Delete Operation Completed.");
			sess.removeAttribute("bookVO");
			sess.removeAttribute("authorVOList");
			
			req.getRequestDispatcher(BookPageViewUrl).forward(req, res);
		}
		else if (action != null && action.equalsIgnoreCase("deleteWithoutAuthors")) {
			BookVO bookVO = (BookVO) sess.getAttribute("bookVO");
			
			bookDAO.delete(bookVO);
			sess.setAttribute("operationResult", "Delete Operation Completed.");
			sess.removeAttribute("bookVO");
			sess.removeAttribute("authorVOList");
			
			req.getRequestDispatcher(BookPageViewUrl).forward(req, res);
		}
		else {
			req.getRequestDispatcher(BookPageViewUrl).forward(req, res);
		}
		
	}
	
	public void doGet (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		doPost(req, res);
	}
}
