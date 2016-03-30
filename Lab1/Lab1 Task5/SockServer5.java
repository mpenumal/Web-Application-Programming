import java.net.*;
import java.io.*;
import java.util.*;

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

class SockServer5 {
    public static void main (String args[]) throws Exception {
        ServerSocket    serv = null;
        InputStream in = null;
        OutputStream out = null;
        Socket sock = null;
        
        int clientId = 0;
        byte[] i4 = new byte[4];
        byte[] i5 = new byte[4];
        byte[] i6 = new byte[4];
        
        try {
            serv = new ServerSocket(8888);
        } 
        catch(Exception e) {
        	e.printStackTrace();
        }
        
        while (serv.isBound() && !serv.isClosed()) {
            System.out.println("Ready...");
            try {
                sock = serv.accept();
                in = sock.getInputStream();
                out = sock.getOutputStream();
                
                in.read(i4);
                clientId = ByteBuffer.wrap(i4).order(ByteOrder.LITTLE_ENDIAN).getInt();
                char command = (char)in.read();
                in.read(i5);
                int x = ByteBuffer.wrap(i5).order(ByteOrder.LITTLE_ENDIAN).getInt();
                
                HandleXML handlexml = new HandleXML();
                if (!handlexml.isXMLAvailable()) {
                	handlexml.createXML();
                }
                int result = handlexml.writeToXML(clientId, command, x);
                
                i6 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(result).array();
                out.write(i6);
                out.flush();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (out != null)  out.close();
                if (in != null)   in.close();
                if (sock != null) sock.close();
            }
        }
    }
}

class HandleXML {
	String fileName = "clientTotals.xml";
	public boolean isXMLAvailable()
    {
		boolean isAvailable = false;
		File f = new File(fileName);
		if(f.exists() && !f.isDirectory()) { 
		    isAvailable = true;
		}
    	return isAvailable;
    }
    
	public void createXML()
    {
    	try {
    		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder builder = factory.newDocumentBuilder();
    		Document doc = builder.newDocument();
    		
    		Element root = doc.createElement("RunningTotal");
	    	doc.appendChild(root);
    		    	
    	    TransformerFactory trFactory = TransformerFactory.newInstance();
    	    Transformer transformer = trFactory.newTransformer();
    	    DOMSource source = new DOMSource(doc);
    	
    	    StreamResult result = new StreamResult(new File(fileName));
    	    transformer.transform(source, result);
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
	public int writeToXML(int clientId, char command, int x)
    {
		int result = 0;
		File xmlFile = new File(fileName);
		boolean flag = false;
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			
			Node root = doc.getFirstChild();
			NodeList list = root.getChildNodes();
			
			for (int i=0; i < list.getLength(); i++) 
			{
				Node client = list.item(i);
				if (client.getNodeType() == Node.ELEMENT_NODE) {
					Element clientElement = (Element) client;
					if (Integer.parseInt(clientElement.getAttribute("id")) == clientId) {
						int v = 0;
						
						if (Character.toUpperCase(command) == 'R') {
							System.out.println("Server received reset");
						}
						else {
							System.out.println("Server received " + x + "and" + command + " for client " + clientId);
							v = Integer.valueOf(clientElement.getElementsByTagName("value").item(0).getTextContent());
							v += x;
						}
						
						Node value = client.getChildNodes().item(0);
						// get the value element, and update it
						if ("value".equals(value.getNodeName())) {
							value.setTextContent(Integer.toString(v));
						}
						
						result = v;
						flag = true;
					}
				}
			}
			
			if (!flag) {
				Element client = doc.createElement("client");
				root.appendChild(client);
		    	
		    	Attr attr = doc.createAttribute("id");
		    	attr.setValue(Integer.toString(clientId));
		    	client.setAttributeNode(attr);
				
		    	Element value = doc.createElement("value");
		    	if (Character.toUpperCase(command) == 'R') {
					System.out.println("Server received reset");
					value.appendChild(doc.createTextNode(Integer.toString(0)));
				}
				else {
					System.out.println("Server received " + x + " for client " + clientId);
					value.appendChild(doc.createTextNode(Integer.toString(x)));
				}
		    	client.appendChild(value);
		    	
		    	result = x;
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult stResult = new StreamResult(new File(fileName));
			transformer.transform(source, stResult);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(result);
		return result;
    }
}
