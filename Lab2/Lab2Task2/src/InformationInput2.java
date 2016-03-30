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
public class InformationInput2 extends HttpServlet {
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
		    	String session_firstname = request.getParameter("firstname");
			    String session_lastname = request.getParameter("lastname");
		    	
		    	sess.setAttribute("firstname", session_firstname);
		        sess.setAttribute("lastname", session_lastname);
		        
		        String session_language = (String) sess.getAttribute("language");
		    	
		        String post = "post";
		    	String InformationInput1 = "InformationInput1";
		    	String InformationInput3 = "InformationInput3";
	        	String language = "language";
	        	String text = "text";
	        	String submit = "submit";
	        	String Previous = "Previous";
	        	String Next = "Next";
	        	int rows = 1;
	        	int columns = 50;

	        	response.setContentType("text/html");
		    	PrintWriter out = response.getWriter();
		    	out.println("<html><body>");
		    	out.println("<form method="+post+">");
		    	out.println("<h4>Input Page 2</h4><br>");
		    	out.println("Known Languages (add comma seperated values):");
		    	if (session_language != null) {
		    		out.println("<textarea rows="+rows+" cols="+columns+" name="+language+">"+session_language+"</textarea><br><br>");
		    	}
		    	else {
		    		out.println("<textarea rows="+rows+" cols="+columns+" name="+language+"></textarea><br><br>");
		    	}
		    	
		    	out.println("<input type="+submit+" value="+Previous+" formaction="+InformationInput1+">");
		    	out.println("<input type="+submit+" value="+Next+" formaction="+InformationInput3+">");
		    	out.println("</form></body></html>");
		    }
	}
	
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		response.sendError(405, "HTTP method GET is not supported by this URL" );
	}
}
