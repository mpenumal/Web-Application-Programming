import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import java.nio.*;

@SuppressWarnings("serial")
public class InformationInput1 extends HttpServlet {
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
        	
		String userName = null;
		Cookie[] cookies = request.getCookies();
		if(cookies !=null){
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("user")) userName = cookie.getValue();
			}
		}
			
			HttpSession sess = request.getSession();
			
		    if(userName == null) {
		    	response.sendRedirect("/ser422/lab2task2/LandingPage.html");
		    }
		    else {
		    	String post = "post";
		    	String InformationInput2 = "InformationInput2";
	        	String firstname = "firstname";
	        	String lastname = "lastname";
	        	String text = "text";
	        	String submit = "submit";
	        	String Next = "Next";
	        	
	        	String session_firstname = (String) sess.getAttribute("firstname");
	    	    String session_lastname = (String) sess.getAttribute("lastname");

	        	response.setContentType("text/html");
		    	PrintWriter out = response.getWriter();
		    	out.println("<html><body>");
		    	out.println("<form method="+post+" action="+InformationInput2+">");
		    	out.println("<h4>Input Page 1</h4><br>");
		    	out.println("First name:");
		    	if (session_firstname != null) {
		    		out.println("<input type="+text+" id="+firstname+" name="+firstname+" value="+session_firstname+">");
		    	}
		    	else {
		    		out.println("<input type="+text+" id="+firstname+" name="+firstname+">");
		    	}
		    	out.println("Last name:");
		    	if (session_lastname != null) {
		    		out.println("<input type="+text+" id="+lastname+" name="+lastname+" value="+session_lastname+"><br><br>");
		    	}
		    	else {
		    		out.println("<input type="+text+" id="+lastname+" name="+lastname+"><br><br>");
		    	}
		    	out.println("<input type="+submit+" value="+Next+">");
		    	out.println("</form></body></html>");
		    }
	}
	
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		response.sendError(405, "HTTP method GET is not supported by this URL" );
	}
}
