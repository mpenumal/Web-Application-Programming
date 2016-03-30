package edu.asupoly.ser422.services.impl;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import edu.asupoly.ser422.services.*;
import edu.asupoly.ser422.model.*;

import java.util.Properties;

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

//impl of interface AuthorDAO
public class AuthorDAOXMLImpl implements AuthorDAO {
	private static int __id = 1;
	
	private static String __jdbcUrl;
	private static String __jdbcUser;
	private static String __jdbcPasswd;
	private static String __jdbcDriver;

	// Only instantiated by factory within package scope
	public AuthorDAOXMLImpl() {
		// read your db init properties
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
	
	public void createXML()
    {
    	try {
    		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder builder = factory.newDocumentBuilder();
    		Document doc = builder.newDocument();
    		
    		Element root = doc.createElement("Authors");
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
	
	public int writeToXML(String filepath, AuthorVO authorVO)
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
				Node author = list.item(i);
				if (author.getNodeType() == Node.ELEMENT_NODE) {
					Element authorElement = (Element) author;
					if (Integer.parseInt(authorElement.getAttribute("author_id")) == authorVO.getAuthorId()) {
						String fname = "";
						String lname = "";
						Element nameElement = (Element) authorElement.getElementsByTagName("name").item(0);
						fname = nameElement.getElementsByTagName("firstname").item(0).getTextContent();
						lname = nameElement.getElementsByTagName("lastname").item(0).getTextContent();
						
						Node firstname = nameElement.getElementsByTagName("firstname").item(0);
						Node lastname = nameElement.getElementsByTagName("lastname").item(0);
						
						if ("firstname".equals(firstname.getNodeName())) {
							value.setTextContent(authorVO.getAuthorFirstName());
						}
						if ("lastname".equals(lastname.getNodeName())) {
							value.setTextContent(authorVO.getAuthorLastName());
						}
						flag = true;
					}
				}
			}
			
			if (!flag) {
				Element author = doc.createElement("Author");
				root.appendChild(author);
				
				Attr attr = doc.createAttribute("author_id");
		    	attr.setValue(Integer.toString(author_id));
		    	author.setAttributeNode(attr);

				Element name = doc.createElement("name");
				author.appendChild(name);
				
			    Element firstname = doc.createElement("firstname");
			    firstname.appendChild(doc.createTextNode(firstName));
			    name.appendChild(firstname);
			    	
			    Element lastname = doc.createElement("lastname");
			    lastname.appendChild(doc.createTextNode(lastName));
			    name.appendChild(lastname);
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
	
	public int readXML(String filepath)
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
				Node author = list.item(i);
				if (author.getNodeType() == Node.ELEMENT_NODE) {
					Element authorElement = (Element) author;
					if (Integer.parseInt(authorElement.getAttribute("author_id")) == authorVO.getAuthorId()) {
						String fname = "";
						String lname = "";
						Element nameElement = (Element) authorElement.getElementsByTagName("name").item(0);
						fname = nameElement.getElementsByTagName("firstname").item(0).getTextContent();
						lname = nameElement.getElementsByTagName("lastname").item(0).getTextContent();
						
						Node firstname = nameElement.getElementsByTagName("firstname").item(0);
						Node lastname = nameElement.getElementsByTagName("lastname").item(0);
						
						if ("firstname".equals(firstname.getNodeName())) {
							value.setTextContent(authorVO.getAuthorFirstName());
						}
						if ("lastname".equals(lastname.getNodeName())) {
							value.setTextContent(authorVO.getAuthorLastName());
						}
						flag = true;
					}
				}
			}
			
			if (!flag) {
				Element author = doc.createElement("Author");
				root.appendChild(author);
				
				Attr attr = doc.createAttribute("author_id");
		    	attr.setValue(Integer.toString(author_id));
		    	author.setAttributeNode(attr);

				Element name = doc.createElement("name");
				author.appendChild(name);
				
			    Element firstname = doc.createElement("firstname");
			    firstname.appendChild(doc.createTextNode(firstName));
			    name.appendChild(firstname);
			    	
			    Element lastname = doc.createElement("lastname");
			    lastname.appendChild(doc.createTextNode(lastName));
			    name.appendChild(lastname);
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
	
	public AuthorVO findByPrimaryKey(int pPrimaryKey) 
	{
		AuthorVO authorVO = null;
		String filepath = getServletContext().getRealPath("lab3Author.xml");
		
		if (!isXMLAvailable(filepath)) {
			return null;
		}
		else {
				
			}
		}
		
		
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		AuthorVO authorVO = null;
		String sql = "SELECT * FROM Author WHERE author_id=?";
		
		try {
			// load the jdbc driver
			Class c1 = Class.forName(__jdbcDriver);
			
			// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);

			preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, pPrimaryKey);
            rs = preparedStatement.executeQuery();

            // Populating the value object with the data retrieved from the resultset.
            while (rs.next()) {
                authorVO = new AuthorVO(rs.getInt("author_id"));
                authorVO.setAuthorFirstName(rs.getString("firstname"));
                authorVO.setAuthorLastName(rs.getString("lastname"));
            }
            return authorVO;
		}
		catch(SQLException sqe) {
            /*Aborting the transaction*/
			sqe.printStackTrace();
        	return null;
        }
		catch(Exception ge) {
			ge.printStackTrace();
        	return null;
		}
        finally
        {
            try {
                if (rs!=null)  { rs.close(); }
                if (preparedStatement != null) { preparedStatement.close(); }
                if (conn!=null) { conn.close(); }
            }
            catch(SQLException e) {
            	e.printStackTrace();
            }
        }
    }
	
	public ArrayList<AuthorVO> getAllAuthors()
	{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ArrayList<AuthorVO> authorVOList = new ArrayList<AuthorVO>();
		String sql = "SELECT * FROM Author";
		
		try {
			// load the jdbc driver
			Class c1 = Class.forName(__jdbcDriver);
			
			// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);

			preparedStatement = conn.prepareStatement(sql);
            rs = preparedStatement.executeQuery();

            // Populating the value object with the data retrieved from the resultset.
            while (rs.next()) {
            	AuthorVO authorVO = new AuthorVO(rs.getInt("author_id"));
                authorVO.setAuthorFirstName(rs.getString("firstname"));
                authorVO.setAuthorLastName(rs.getString("lastname"));
                authorVOList.add(authorVO);
            }
            return authorVOList;
		}
		catch(SQLException sqe) {
            /*Aborting the transaction*/
			sqe.printStackTrace();
        	return null;
        }
		catch(Exception ge) {
			ge.printStackTrace();
        	return null;
		}
        finally
        {
            try {
                if (rs!=null)  { rs.close(); }
                if (preparedStatement != null) { preparedStatement.close(); }
                if (conn!=null) { conn.close(); }
            }
            catch(SQLException e) {
            	e.printStackTrace();
            }
        }
    }
	
	/*
     * The update() method will update a single record in the Author table
     * with the valueobject passed in as a parameter.
     *
     */
    public void update(AuthorVO pValueObject) 
    {
        /*Getting my SQL Code for updating all of the Authors in my application.*/
    	Connection conn = null;
		PreparedStatement preparedStatement = null;
        String sql = "UPDATE Author SET firstName=?, lastName=? WHERE author_id=?";
        AuthorVO authorVO = pValueObject;

        try{
        	// load the jdbc driver
			Class c1 = Class.forName(__jdbcDriver);
   				
			// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
        	
            /*Populating the prepared statement's parameters*/
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, authorVO.getAuthorFirstName());
            preparedStatement.setString(2, authorVO.getAuthorLastName());
            preparedStatement.setInt(3, authorVO.getAuthorId());

            /*
             * Checking to see if we were successful in updating the record.
             * If the queryResult does not equal 1, then we know we have run
             * into an optimistic lock situation.
             *
             */
            int queryResults = preparedStatement.executeUpdate();
        }
        catch(SQLException sqe){
        	sqe.printStackTrace();
        }
        catch(Exception ge) {
			ge.printStackTrace();
		}
        finally{
            try{
                if (preparedStatement!=null) preparedStatement.close();
                if (conn!=null) conn.close();
            }
            catch(SQLException e){
            	e.printStackTrace();
            }
        }
    }
    
    /*
     * The delete() record will delete a single record from the Author
     * table in the subscription database.  It does this based on the
     * id of the value object passed into the table.
     *
     */
    @SuppressWarnings("resource")
	public void delete(AuthorVO pValueObject) 
    {
        /* Building my SQL Code for deleting a record from the title table.*/
    	Connection conn = null;
		PreparedStatement preparedStatement = null;
		String sqlAuthorBook = "DELETE FROM Join_Author_Book WHERE author_id=?";
		String sqlAuthor = "DELETE FROM Author WHERE author_id=?";
        AuthorVO authorVO = pValueObject;

        try {
        	// load the jdbc driver
       		Class c1 = Class.forName(__jdbcDriver);
        	   				
       		// connect to db based on name
        	conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
        	
        	/*Deleting the row from the Join table*/
            preparedStatement = conn.prepareStatement(sqlAuthorBook);
            preparedStatement.setInt(1,authorVO.getAuthorId());
            preparedStatement.executeUpdate();
        	
            /*Deleting the row from the Author table*/
            preparedStatement = conn.prepareStatement(sqlAuthor);
            preparedStatement.setInt(1,authorVO.getAuthorId());
            preparedStatement.executeUpdate();
        }
        catch(SQLException sqe){
            sqe.printStackTrace();
        }
        catch(Exception ge) {
			ge.printStackTrace();
		}
        finally{
            try{
                if (preparedStatement!=null) preparedStatement.close();
                if (conn!=null)       conn.close();
            }
            catch(SQLException e) {
            	e.printStackTrace();
            }
        }
    }
    
    /*
     * The insert() method will insert a single record into the Author table.
     * The data it inserts will be pulled from the value object passed into
     * the method.
     *
     */
    @SuppressWarnings("resource")
	public void insert(AuthorVO pValueObject) 
    {
        /*Getting all of my SQL code for inserting a title*/
    	Connection conn = null;
		PreparedStatement preparedStatement = null;
        String  sqlAuthor = "INSERT INTO Author VALUES (?,?,?)";

        /*Building my SQL Code for inserting a record into the title table.*/
        AuthorVO authorVO = pValueObject;

        try {
        	// load the jdbc driver
       		Class c1 = Class.forName(__jdbcDriver);
        	   				
       		// connect to db based on name
        	conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
        	
            /*Populating the prepared statement with data from the value object*/
            preparedStatement = conn.prepareStatement(sqlAuthor);
            preparedStatement.setInt(1, authorVO.getAuthorId());
            preparedStatement.setString(2, authorVO.getAuthorFirstName());
            preparedStatement.setString(3, authorVO.getAuthorLastName());

            preparedStatement.execute();
        }
        catch(SQLException sqe){
            sqe.printStackTrace();
        }
        catch(Exception ge) {
			ge.printStackTrace();
		}
        finally{
            try{
                if (preparedStatement!=null) preparedStatement.close();
                if (conn!=null) conn.close();
            }
            catch(SQLException e){
            	e.printStackTrace();
            }
        }
    }
    
    /*
     * The createValueObject() method will create an empty value object pre-
     * populated with any mandatory data.  An example of this mandatory data
     * might be a primary key that for a particular record..
     *
     */
    public AuthorVO createValueObject()
    {
        AuthorVO authorVO = new AuthorVO();

        // ID is set on creation of the VO and is immutable

        /* Populating the rest of the value object with data */
        authorVO.setAuthorFirstName("");
        authorVO.setAuthorLastName("");

        return authorVO;
    }
	
	// This class is going to look for a file named rdbm.properties in the classpath
	// to get its initial settings
	static {
		try {
			Properties dbProperties = new Properties();
			dbProperties.load(AuthorDAORdbmsImpl.class.getClassLoader().getResourceAsStream("rdbm.properties"));
			__jdbcUrl    = dbProperties.getProperty("jdbcUrl");
			__jdbcUser   = dbProperties.getProperty("jdbcUser");
			__jdbcPasswd = dbProperties.getProperty("jdbcPasswd");
			__jdbcDriver = dbProperties.getProperty("jdbcDriver");
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
		}
	}

}
