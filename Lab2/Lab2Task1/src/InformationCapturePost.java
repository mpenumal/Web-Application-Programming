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

public class InformationCapturePost extends HttpServlet {
  public void doPost(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException, IOException {

      String firstname = request.getParameter("firstname");
      String lastname = request.getParameter("lastname");
      String languageList = request.getParameter("language");
      String[] days = request.getParameterValues("weekday");
      String location = request.getParameter("location");
      
      String filepath = getServletContext().getRealPath("lab2.xml");
      
      String[] languages = languageList.split("\\s*,\\s*");
      
      boolean result = false;
      
      HandleXML handlexml = new HandleXML();
      if (!handlexml.isXMLAvailable(filepath)) {
    	  handlexml.createXML(filepath);
      }
      
      result = handlexml.writeToXML(filepath, firstname, lastname,languages, days, location);
      int count = handlexml.getUserCount(filepath);
      
      String s = "/ser422/lab2task1/";

      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println("<html><body>");
      if (result) {
    	  out.println("<h2>Congratulations!</h2><br>");
    	  out.println("<h4>The data was received successfully.</h4><br>");
    	  out.println("<h4>Current count of users = " + count + "</h4><br>");
      }
      else {
    	  out.println("<h2>We are sorry!</h2><br>");
    	  out.println("<h4>The data was NOT received successfully.</h4><br>");
      }
      out.println("<a href="+s+">Go back!</a>");
      out.println("</body></html>");
  }
}

class HandleXML {
	public boolean isXMLAvailable(String filepath)
    {
		boolean isAvailable = false;
		File f = new File(filepath);
		if(f.exists() && !f.isDirectory()) { 
		    isAvailable = true;
		}
    	return isAvailable;
    }
    
	public void createXML(String filepath)
    {
    	try {
    		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder builder = factory.newDocumentBuilder();
    		Document doc = builder.newDocument();
    		
    		Element root = doc.createElement("UserInfo");
	    	doc.appendChild(root);
    		
    	    TransformerFactory trFactory = TransformerFactory.newInstance();
    	    Transformer transformer = trFactory.newTransformer();
    	    DOMSource source = new DOMSource(doc);
    	
    	    StreamResult result = new StreamResult(new FileOutputStream(filepath));
    	    transformer.transform(source, result);
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
	public int getUserCount(String filepath)
	{
		File xmlFile = new File(filepath);
		int userCount = 0;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			
			Node root = doc.getFirstChild();
			NodeList list = root.getChildNodes();
			userCount = list.getLength(); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return userCount;
	}
	
	public boolean writeToXML(String filepath, String firstName, String lastName, 
							String[] languages, String[] days, String location)
    {
		File xmlFile = new File(filepath);
		boolean result = false;
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			
			Node root = doc.getFirstChild();
			NodeList list = root.getChildNodes();
			
			Element user = doc.createElement("user");
			root.appendChild(user);

			Element name = doc.createElement("name");
			user.appendChild(name);
			
		    Element firstname = doc.createElement("firstname");
		    firstname.appendChild(doc.createTextNode(firstName));
		    name.appendChild(firstname);
		    	
		    Element lastname = doc.createElement("lastname");
		    lastname.appendChild(doc.createTextNode(lastName));
		    name.appendChild(lastname);
		    	
		    Element langs = doc.createElement("languages");
		    user.appendChild(langs);
		    
		    for(String language: languages) {
		   		if (!language.equals("None")) 
		   		{
		   			Element lang = doc.createElement("language");
		   			lang.appendChild(doc.createTextNode(language));
		   			langs.appendChild(lang);
		   		}
		    }
		    	
		    Element weekdays = doc.createElement("days");
		    user.appendChild(weekdays);

		   	for(String day: days) {
		   		if (!day.equals("None")) 
		   		{
		   			Element weekday = doc.createElement("day");
		   			weekday.appendChild(doc.createTextNode(day));
		   			weekdays.appendChild(weekday);
		   		}
		    }
		   	
		   	Element loc = doc.createElement("location");
		    loc.appendChild(doc.createTextNode(location));
		    user.appendChild(loc);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult stResult = new StreamResult(new FileOutputStream(filepath));
			transformer.transform(source, stResult);
			
			result = true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return result;
    }
}