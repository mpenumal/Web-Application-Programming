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

public class InformationCaptureGet extends HttpServlet {
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

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
		    	String firstname = request.getParameter("firstname");
		        String lastname = request.getParameter("lastname");
		        String languageList = request.getParameter("languages");
		        String dayList = request.getParameter("days");
		        String location = request.getParameter("location");
		        
		        ArrayList<String> languages = new ArrayList<String>();
		        ArrayList<String> days = new ArrayList<String>();
		        
		        ArrayList<String> allDays = new ArrayList<String>();
		        allDays.add("monday");
		        allDays.add("tuesday");
		        allDays.add("wednesday");
		        allDays.add("thursday");
		        allDays.add("friday");
		        allDays.add("saturday");
		        allDays.add("sunday");
		        
		        ArrayList<String> allLocations = new ArrayList<String>();
		        allLocations.add("downtown");
		        allLocations.add("tempe");
		        allLocations.add("mesa");
		        allLocations.add("tucson");
		        allLocations.add("scottsdale");
		        allLocations.add("sedona");
		        
		        
		        
		        String filepath = getServletContext().getRealPath("lab2.xml");
		        
		        if (dayList != null) {
		        	days = new ArrayList<String>(Arrays.asList(dayList.split("\\s+")));
		        }
		        
		        if (languageList != null) {
		        	languages = new ArrayList<String>(Arrays.asList(languageList.split("\\s+")));
		        }
		        
		        if (days != null && days.size() > 0) {
		        	for (int i = 0; i < days.size(); i++) {
		        		if (!(allDays.contains((days.get(i)).toLowerCase()))) {
		        			response.sendError(400, "Improper Parameters.");
		        			break;
		        		}
		        	}
		        }
		        if (location != null && !(allLocations.contains(location.toLowerCase()))) {
		        	response.sendError(400, "Improper Parameters.");
		        }
		        else {
		        	boolean isChangeColor = request.getHeader("User-Agent").contains("Chrome");
			        String color = "#ff99ff";       
			        response.setContentType("text/html");
			        PrintWriter out = response.getWriter();
			        
			        if (!isXMLAvailable(filepath)) {
			        	out.println("<html>");
			        	if (isChangeColor) {
			        		out.println("<body bgcolor="+color+">");
			        	}
			        	else {
			        		out.println("<body>");
			        	}
			        	out.println(request.getHeader("User-Agent"));
			        	out.println("<h2>No Data!</h2>");
			        	out.println("</body></html>");
			        }
			        else {
			        	ArrayList<String> output = new ArrayList<String>();
			        	output = readXML(filepath, firstname, lastname, days, location, languages);
			        	
			        	out.println("<html>");
			        	if (isChangeColor) {
			        		out.println("<body bgcolor="+color+">");
			        	}
			        	else {
			        		out.println("<body>");
			        	}
			        	if (output != null && output.size() > 0) {
			        		out.println("<h2>Below is the requested data:</h2>");
			        		for (int i = 0; i < output.size(); i++)
			        		{
			        			out.println("<h4>"+output.get(i)+"</h4>");
			        		}
			        	}
			        	else {
			        		out.println("<html>");
			        		if (isChangeColor) {
			            		out.println("<body bgcolor="+color+">");
			            	}
			            	else {
			            		out.println("<body>");
			            	}
			            	out.println("<h2>No Data!</h2>");
			            	out.println("</body></html>");
			        	}
			        	out.println("</body></html>");
			        }
		        }
		    }
	}
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendError(405, "HTTP method POST is not supported by this URL" );
	}
  
	private boolean isXMLAvailable(String filepath)
	{
		boolean isAvailable = false;
		File f = new File(filepath);
		if(f.exists() && !f.isDirectory()) { 
			isAvailable = true;
		}
		return isAvailable;
	}
	
	private ArrayList<String> readXML(String filepath, String firstname, 
			String lastname, ArrayList<String> days, String loc, ArrayList<String> langs)
	{
		File xmlFile = new File(filepath);
		
		firstname = firstname == null ? "" : firstname;
		lastname = lastname == null ? "" : lastname;
		if (langs != null && langs.size() != 0)
		{
			for (int m=0; m<langs.size(); m++)
			{
				langs.set(m, (langs.get(m) == null ? "" : langs.get(m)));
			}
		}
		if (days != null && days.size() != 0)
		{
			for (int m=0; m<days.size(); m++)
			{
				days.set(m, (days.get(m) == null ? "" : days.get(m)));
			}
		}
		loc = loc == null ? "" : loc;

		ArrayList<String> output = new ArrayList<String>(); 
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			
			Node root = doc.getFirstChild();
			NodeList list = root.getChildNodes();
			
			for (int i=0; i < list.getLength(); i++) 
			{
				String outValue = "";
				// xml variants of the inputs
				String fname = "";
				String lname = "";
				ArrayList<String> language = new ArrayList<String>();
				ArrayList<String> weekday = new ArrayList<String>();
				String location = "";
				
				Node user = list.item(i);
				if (user.getNodeType() == Node.ELEMENT_NODE) {
					Element userElement = (Element) user;
					Element nameElement = (Element) userElement.getElementsByTagName("name").item(0);
					fname = nameElement.getElementsByTagName("firstname").item(0).getTextContent();
					fname = fname == null ? "" : fname;
					
					if (firstname != "") {
						if (!(fname.toLowerCase()).contains(firstname.toLowerCase())) {
							continue;
						}
					}
					
					lname = nameElement.getElementsByTagName("lastname").item(0).getTextContent();
					lname = lname == null ? "" : lname;
					
					if (lastname != "") {
						if (!(lname.toLowerCase()).contains(lastname.toLowerCase())) {
							continue;
						}
					}
					
					Element languagesElement = (Element) userElement.getElementsByTagName("languages").item(0);
					NodeList languagesList = languagesElement.getElementsByTagName("language");
					for (int j = 0; j < languagesList.getLength(); j++) {
						String lg ="";
						lg = languagesList.item(j).getTextContent();
						lg = lg == null ? "" : lg;
						language.add(lg.toLowerCase());
					}
					
					if (langs != null && langs.size() > 0) {
						boolean match = false;
						if (language != null && language.size() > 0) {
							for (int k = 0; k < langs.size(); k++) {
								if (!language.contains((langs.get(k)).toLowerCase())) {
									continue;
								}
								else {
									match = true;
									break;
								}
							}
							if (!match) {
								continue;
							}
						}
						else {
							continue;
						}
					}
					
					Element daysElement = (Element) userElement.getElementsByTagName("days").item(0);
					NodeList daysList = daysElement.getElementsByTagName("day");
					for (int j = 0; j < daysList.getLength(); j++) {
						String wday ="";
						wday = daysList.item(j).getTextContent();
						wday = wday == null ? "" : wday;
						weekday.add(wday.toLowerCase());
					}
					
					if (days != null && days.size() > 0) {
						boolean match = false;
						if (weekday != null && weekday.size() > 0) {
							for (int k = 0; k < days.size(); k++) {
								if (!weekday.contains((days.get(k)).toLowerCase())) {
									continue;
								}
								else {
									match = true;
									break;
								}
							}
							if (!match) {
								continue;
							}
						}
						else {
							continue;
						}
					}
					
					location = userElement.getElementsByTagName("location").item(0).getTextContent();
					location = location == null ? "" : location;
					
					if (!(location.toLowerCase()).contains(loc.toLowerCase())) {
						continue;
					}
					
					int p = 0;
					outValue = fname + " " + lname + ", ";
					for (p = 0; p < language.size(); p++) {
						outValue = outValue + language.get(p) + " ";
					}
					outValue += ", ";
					for (p = 0; p < weekday.size(); p++) {
						outValue = outValue + weekday.get(p).toUpperCase() + " ";
					}
					outValue += ", ";
					outValue = outValue + location;
					
					output.add(outValue);
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return output;
	}
}