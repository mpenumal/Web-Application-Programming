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
public class InformationInput3 extends HttpServlet {
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
		    	String session_language = request.getParameter("language");
		    	
		    	sess.setAttribute("language", session_language);
		    	
		    	String[] session_days = (String[]) sess.getAttribute("weekday");
		    	
		    	String post = "post";
		    	String InformationInput2 = "InformationInput2";
		    	String InformationInput4 = "InformationInput4";
	        	String checkbox = "checkbox";
	        	String weekday = "weekday";
	        	String None = "None";
	        	String hide = "display:none;";
	        	String Monday = "Monday";
	        	String Tuesday = "Tuesday";
	        	String Wednesday = "Wednesday";
	        	String Thursday = "Thursday";
	        	String Friday = "Friday";
	        	String Saturday = "Saturday";
	        	String Sunday = "Sunday";
	        	String submit = "submit";
	        	String Previous = "Previous";
	        	String Next = "Next";
	        	

	        	response.setContentType("text/html");
		    	PrintWriter out = response.getWriter();
		    	out.println("<html><body>");
		    	out.println("<form method="+post+">");
		    	out.println("<h4>Input Page 3</h4><br>");
		    	out.println("Available days of the week:");
		    	out.println("<input type="+checkbox+" name="+weekday+" value="+None+" checked disabled style="+hide+"><br>");
		    	if (session_days != null && Arrays.asList(session_days).contains(Monday)) {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Monday+" checked> Monday");
		    	}
		    	else {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Monday+"> Monday");
		    	}
		    	if (session_days != null && Arrays.asList(session_days).contains(Tuesday)) {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Tuesday+" checked> Tuesday");
		    	}
		    	else {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Tuesday+"> Tuesday");
		    	}
		    	if (session_days != null && Arrays.asList(session_days).contains(Wednesday)) {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Wednesday+" checked> Wednesday");
		    	}
		    	else {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Wednesday+"> Wednesday");
		    	}
		    	if (session_days != null && Arrays.asList(session_days).contains(Thursday)) {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Thursday+" checked> Thursday");
		    	}
		    	else {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Thursday+"> Thursday");
		    	}
		    	if (session_days != null && Arrays.asList(session_days).contains(Friday)) {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Friday+" checked> Friday");
		    	}
		    	else {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Friday+"> Friday");
		    	}
		    	if (session_days != null && Arrays.asList(session_days).contains(Saturday)) {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Saturday+"> Saturday");
		    	}
		    	else {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Saturday+"> Saturday");
		    	}
		    	if (session_days != null && Arrays.asList(session_days).contains(Sunday)) {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Sunday+"> Sunday<br><br>");
		    	}
		    	else {
		    		out.println("<input type="+checkbox+" name="+weekday+" value="+Sunday+"> Sunday<br><br>");
		    	}
		    	out.println("<input type="+submit+" value="+Previous+" formaction="+InformationInput2+">");
		    	out.println("<input type="+submit+" value="+Next+" formaction="+InformationInput4+">");
		    	out.println("</form></body></html>");				
		    }
	}
	
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		response.sendError(405, "HTTP method GET is not supported by this URL" );
	}
}
