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
public class SessionStateGet extends HttpServlet {
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		
		String userName = null;
		Cookie[] cookies = request.getCookies();
		if(cookies !=null){
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("user")) userName = cookie.getValue();
			}
		}
		
		    if(userName == null) {
		    	response.sendRedirect("/ser422/lab2task2/LandingPage.html");
		    }
		    else {
			    String InformationInput1 = "InformationInput1";
			    String Text = "ClickHere!";
			    String submit = "submit";
			    String post ="post";
			    
			    ArrayList<String> languageMatch = new ArrayList<String>();
			    ArrayList<String> weekdaysMatch = new ArrayList<String>();
			    ArrayList<String> locationsMatch = new ArrayList<String>();
			    
			    String[] name = userName.split("-");
		    	String filepath = getServletContext().getRealPath("lab2.xml");
			    
			    ArrayList<ArrayList<String>> preferenceMatches = getPreferenceMatches(filepath, name[0], name[1]);
			    if (preferenceMatches != null && preferenceMatches.size() > 0) {
			    	languageMatch = preferenceMatches.get(0);
			    	weekdaysMatch = preferenceMatches.get(1);
			    	locationsMatch = preferenceMatches.get(2);
			    }

			    response.setContentType("text/html");
			    PrintWriter out = response.getWriter();
			    out.println("<html><body>");
			    out.println("<form method="+post+" action="+InformationInput1+">");
			    out.println("<h4>Welcome "+userName+"</h4>");
			    if ((languageMatch == null || languageMatch.size() == 0) &&
			    		(weekdaysMatch == null || weekdaysMatch.size() == 0) &&
			    		(locationsMatch == null && locationsMatch.size() == 0)) {
			    	out.println("<h4>No preferences available.</h4><br>");
			    }
			    else {
			    	out.println("<h4>List of the top 3 matches closest to each of your preferences:</h4>");
			    	if (languageMatch != null && languageMatch.size() > 0) {
			    		out.println("<br>Languages: <br>");
			    		for (int h = 0; h < languageMatch.size(); h++) {
			    			out.println(languageMatch.get(h)+"<br>");
			    		}
			    	}
			    	if (weekdaysMatch != null && weekdaysMatch.size() > 0) {
			    		out.println("<br>Days: <br>");
			    		for (int h = 0; h < weekdaysMatch.size(); h++) {
			    			out.println(weekdaysMatch.get(h)+"<br>");
			    		}
			    	}
			    	if (locationsMatch != null && locationsMatch.size() > 0) {
			    		out.println("<br>Location: <br>");
			    		for (int h = 0; h < locationsMatch.size(); h++) {
			    			out.println(weekdaysMatch.get(h)+"<br>");
			    		}
			    	}
			    }
			    out.println("<br><br>Want to add information?");
			    out.println("<input type="+submit+" value="+Text+"><br>");
			    out.println("</body></html>");
		    }
	}
	
	private ArrayList<ArrayList<String>> getPreferenceMatches(String filepath, String firstname, String lastname) {
		File xmlFile = new File(filepath);
		
		firstname = firstname == null ? "" : firstname;
		lastname = lastname == null ? "" : lastname;
		
		ArrayList<String> langMatches = new ArrayList<String>();
		ArrayList<String> daysMatches = new ArrayList<String>();
		ArrayList<String> locMatches = new ArrayList<String>();
		ArrayList<ArrayList<String>> allMatches = new ArrayList<ArrayList<String>>();
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			
			Node root = doc.getFirstChild();
			NodeList list = root.getChildNodes();
			
			// xml variants of the inputs
			String fname = "";
			String lname = "";
			ArrayList<String> language = new ArrayList<String>();
			ArrayList<String> weekday = new ArrayList<String>();
			String location = "";
			
			for (int i=0; i < list.getLength(); i++) 
			{
				Node user = list.item(i);
				if (user.getNodeType() == Node.ELEMENT_NODE) {
					Element userElement = (Element) user;
					Element nameElement = (Element) userElement.getElementsByTagName("name").item(0);
					
					fname = nameElement.getElementsByTagName("firstname").item(0).getTextContent();
					fname = fname == null ? "" : fname;
					
					lname = nameElement.getElementsByTagName("lastname").item(0).getTextContent();
					lname = lname == null ? "" : lname;
					
					if (firstname != "") {
						if ((fname.toLowerCase()).equals(firstname.toLowerCase())) {
							if (lastname != "") {
								if ((lname.toLowerCase()).equals(lastname.toLowerCase())) {
									Element languagesElement = (Element) userElement.getElementsByTagName("languages").item(0);
									NodeList languagesList = languagesElement.getElementsByTagName("language");
									for (int j = 0; j < languagesList.getLength(); j++) {
										String lg ="";
										lg = languagesList.item(j).getTextContent();
										lg = lg == null ? "" : lg;
										language.add(lg.toLowerCase());
									}
									
									Element daysElement = (Element) userElement.getElementsByTagName("days").item(0);
									NodeList daysList = daysElement.getElementsByTagName("day");
									for (int j = 0; j < daysList.getLength(); j++) {
										String wday ="";
										wday = daysList.item(j).getTextContent();
										wday = wday == null ? "" : wday;
										weekday.add(wday.toLowerCase());
									}
									
									location = userElement.getElementsByTagName("location").item(0).getTextContent();
									location = location == null ? "" : location;
									break;
								}
							}
						}
					}
					// xml variants of the inputs
					fname = "";
					lname = "";
					language = new ArrayList<String>();
					weekday = new ArrayList<String>();
					location = "";
				}
			}
			if ((language != null && language.size() > 0) || 
					(weekday != null && weekday.size() > 0) || location != "") {
				for (int i=0; i < list.getLength(); i++) 
				{
					String outValue = "";
					String firstname1 = "";
					String lastname1 = "";
					ArrayList<String> langs = new ArrayList<String>();
					ArrayList<String> days = new ArrayList<String>();
					String loc = "";
					Node user = list.item(i);
					if (user.getNodeType() == Node.ELEMENT_NODE) {
						Element userElement = (Element) user;
						Element nameElement = (Element) userElement.getElementsByTagName("name").item(0);
						
						firstname1 = nameElement.getElementsByTagName("firstname").item(0).getTextContent();
						firstname1 = firstname1 == null ? "" : firstname1;
						
						lastname1 = nameElement.getElementsByTagName("lastname").item(0).getTextContent();
						lastname1 = lastname1 == null ? "" : lastname1;
						
						Element languagesElement = (Element) userElement.getElementsByTagName("languages").item(0);
						NodeList languagesList = languagesElement.getElementsByTagName("language");
						for (int j = 0; j < languagesList.getLength(); j++) {
							String lg ="";
							lg = languagesList.item(j).getTextContent();
							lg = lg == null ? "" : lg;
							langs.add(lg.toLowerCase());
						}
						Element daysElement = (Element) userElement.getElementsByTagName("days").item(0);
						NodeList daysList = daysElement.getElementsByTagName("day");
						for (int j = 0; j < daysList.getLength(); j++) {
							String wday ="";
							wday = daysList.item(j).getTextContent();
							wday = wday == null ? "" : wday;
							days.add(wday.toLowerCase());
						}
						loc = userElement.getElementsByTagName("location").item(0).getTextContent();
						loc = loc == null ? "" : loc;
						
						if (language != null && language.size() > 0) {
							boolean match = false;
							if (langs != null && langs.size() > 0) {
								for (int k = 0; k < language.size(); k++) {
									if (langs.contains((language.get(k)).toLowerCase())) {
										match = true;
									}
								}
								if (match) {
									int p = 0;
									outValue = firstname1 + " " + lastname1 + ", ";
									for (p = 0; p < langs.size(); p++) {
										outValue = outValue + langs.get(p) + " ";
									}
									outValue += ", ";
									for (p = 0; p < days.size(); p++) {
										outValue = outValue + days.get(p).toUpperCase() + " ";
									}
									outValue += ", ";
									outValue = outValue + loc;
									langMatches.add(outValue);
									outValue = "";
								}
							}
						}
						
						if (weekday != null && weekday.size() > 0) {
							boolean match = false;
							if (days != null && days.size() > 0) {
								for (int k = 0; k < days.size(); k++) {
									if (days.contains((weekday.get(k)).toLowerCase())) {
										match = true;
									}
								}
								if (match) {
									int p = 0;
									outValue = firstname1 + " " + lastname1 + ", ";
									for (p = 0; p < langs.size(); p++) {
										outValue = outValue + langs.get(p) + " ";
									}
									outValue += ", ";
									for (p = 0; p < days.size(); p++) {
										outValue = outValue + days.get(p).toUpperCase() + " ";
									}
									outValue += ", ";
									outValue = outValue + loc;
									daysMatches.add(outValue);
									outValue = "";
								}
							}
						}
						if ((loc.toLowerCase()).contains(location.toLowerCase())) {
							int p = 0;
							outValue = firstname1 + " " + lastname1 + ", ";
							for (p = 0; p < langs.size(); p++) {
								outValue = outValue + langs.get(p) + " ";
							}
							outValue += ", ";
							for (p = 0; p < days.size(); p++) {
								outValue = outValue + days.get(p).toUpperCase() + " ";
							}
							outValue += ", ";
							outValue = outValue + loc;
							locMatches.add(outValue);
							outValue = "";
						}
						if (langMatches.size() > 2 && daysMatches.size() > 2 && locMatches.size() > 2) {
							break;
						}
					}
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		ArrayList<String> tempLangMatch = new ArrayList<String>();
		ArrayList<String> tempDaysMatch = new ArrayList<String>();
		ArrayList<String> tempLocMatch = new ArrayList<String>();
		if (langMatches != null && langMatches.size() > 0) {
			for (int g = 0; g < langMatches.size(); g++) {
				tempLangMatch.add(langMatches.get(g));
				if (g > 2) {
					break;
				}
			}
		}
		if (daysMatches != null && daysMatches.size() > 0) {
			for (int g = 0; g < daysMatches.size(); g++) {
				tempDaysMatch.add(langMatches.get(g));
				if (g > 2) {
					break;
				}
			}
		}
		if (daysMatches != null && daysMatches.size() > 0) {
			for (int g = 0; g < locMatches.size(); g++) {
				tempLocMatch.add(langMatches.get(g));
				if (g > 2) {
					break;
				}
			}
		}
		allMatches.add(tempLangMatch);
		allMatches.add(tempDaysMatch);
		allMatches.add(tempLocMatch);
		return allMatches;
	}
	
	public void doPost (HttpServletRequest request, HttpServletResponse response)
	        throws javax.servlet.ServletException, java.io.IOException {
		response.sendError(405, "HTTP method POST is not supported by this URL" );
	}
}
