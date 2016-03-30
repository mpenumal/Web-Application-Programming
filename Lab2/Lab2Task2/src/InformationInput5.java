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
public class InformationInput5 extends HttpServlet {
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
	    	String submit = "submit";
	    	String Submit = "Submit";
	    	String Cancel = "Cancel";
	    	String post_coder = "post_coder";
	    	String InformationCaptureCancel = "InformationCaptureCancel";
	    	
	    	String session_location = request.getParameter("location");
	    	
	    	sess.setAttribute("location", session_location);
	    	
	    	String session_firstname = (String) sess.getAttribute("firstname");
    	    String session_lastname = (String) sess.getAttribute("lastname");
    	    String session_language = (String) sess.getAttribute("language");
    	    String[] session_days = (String[]) sess.getAttribute("weekday");
    	    session_location = (String) sess.getAttribute("location");
    	    
    	    response.setContentType("text/html");
	    	PrintWriter out = response.getWriter();
	    	out.println("<html><body>");
	    	out.println("<form method="+post+">");
	    	out.println("<h4>Review Page</h4><br>");
	    	if (session_firstname != null) {
	    		out.println("First name: " +session_firstname+ "<br>");
	    	}
	    	if (session_lastname != null) {
	    		out.println("Last name: " +session_lastname+ "<br>");
	    	}
	    	if (session_language != null) {
	    		out.println("Known Languages (add comma seperated values): " +session_language+"<br>");
	    	}
	    	if (session_days != null && session_days.length > 0) {
	    		out.println("Available days of the week: <br>");
	    		for (int i=0; i<session_days.length; i++) {
	    			out.println(" "+session_days[i]+"<br>");
	    		}
	    	}
	    	if (session_location != null) {
	    		out.println("Accesible Location: " +session_location+"<br>");
	    	}
	    	if (!(session_firstname == null && session_lastname == null && session_language == null && 
	    			(session_days == null || session_days.length == 0) && session_location == null)) {
	    		out.println("<input type="+submit+" value="+Submit+" formaction="+post_coder+">");
	    	}
	    	out.println("<input type="+submit+" value="+Cancel+" formaction="+InformationCaptureCancel+">");
	    	out.println("</form></body></html>");
	    }
	}
}
