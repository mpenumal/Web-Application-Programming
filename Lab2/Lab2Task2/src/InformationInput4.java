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
public class InformationInput4 extends HttpServlet {
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
	    	String[] session_days = request.getParameterValues("weekday");
	    	
	    	sess.setAttribute("weekday", session_days);
	    	
	    	String session_location = (String) sess.getAttribute("location");
	    	
	    	String post = "post";
	    	String InformationInput3 = "InformationInput3";
	    	String InformationInput5 = "InformationInput5";
	    	String submit = "submit";
        	String Previous = "Previous";
        	String Next = "Next";
        	String radio = "radio";
        	String location = "location";
        	String checked = "checked";
        	String Downtown = "Downtown";
        	String Mesa = "Mesa";
        	String Tempe = "Tempe";
        	String Tucson = "Tucson";
        	String Scottsdale = "Scottsdale";
        	String Sedona = "Sedona";
        	
        	response.setContentType("text/html");
	    	PrintWriter out = response.getWriter();
	    	out.println("<html><body>");
	    	out.println("<form method="+post+">");
	    	out.println("<h4>Input Page 4</h4><br>");
	    	out.println("Accesible Location:");
	    	if (session_location == null) {
	    		out.println("<input type="+radio+" name="+location+" value="+Downtown+" checked="+checked+"> Downtown");
	    	}
	    	else {
	    		if (session_location.equals(Downtown)) {
	    			out.println("<input type="+radio+" name="+location+" value="+Downtown+" checked="+checked+"> Downtown");
	    		}
	    		else {
	    			out.println("<input type="+radio+" name="+location+" value="+Downtown+"> Downtown");
	    		}
	    	}
	    	if (session_location != null && session_location.equals(Mesa)) {
	    		out.println("<input type="+radio+" name="+location+" value="+Mesa+" checked="+checked+"> Mesa");
	    	}
	    	else {
	    		out.println("<input type="+radio+" name="+location+" value="+Mesa+"> Mesa");
	    	}
	    	if (session_location != null && session_location.equals(Tempe)) {
	    		out.println("<input type="+radio+" name="+location+" value="+Tempe+" checked="+checked+"> Tempe");
	    	}
	    	else {
	    		out.println("<input type="+radio+" name="+location+" value="+Tempe+"> Tempe");
	    	}
	    	if (session_location != null && session_location.equals(Tucson)) {
	    		out.println("<input type="+radio+" name="+location+" value="+Tucson+" checked="+checked+"> Tucson");
	    	}
	    	else {
	    		out.println("<input type="+radio+" name="+location+" value="+Tucson+"> Tucson");
	    	}
			if (session_location != null && session_location.equals(Scottsdale)) {
				out.println("<input type="+radio+" name="+location+" value="+Scottsdale+" checked="+checked+"> Scottsdale");
			}
			else {
				out.println("<input type="+radio+" name="+location+" value="+Scottsdale+"> Scottsdale");
			}
			if (session_location != null && session_location.equals(Sedona)) {
				out.println("<input type="+radio+" name="+location+" value="+Sedona+" checked="+checked+"> Sedona<br><br>");
			}
			else {
				out.println("<input type="+radio+" name="+location+" value="+Sedona+"> Sedona<br><br>");
			}
	    	out.println("<input type="+submit+" value="+Previous+" formaction="+InformationInput3+">");
	    	out.println("<input type="+submit+" value="+Next+" formaction="+InformationInput5+">");
	    	out.println("</form></body></html>");				
	    }
	}
	
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		response.sendError(405, "HTTP method GET is not supported by this URL" );
	}
}
