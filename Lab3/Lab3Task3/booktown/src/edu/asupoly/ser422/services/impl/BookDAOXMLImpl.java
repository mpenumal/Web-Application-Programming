package edu.asupoly.ser422.services.impl;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import edu.asupoly.ser422.services.*;
import edu.asupoly.ser422.model.*;

import java.util.Properties;

//impl of interface AuthorDAO
public class BookDAOXMLImpl implements BookDAO {
	private static int __id = 1;
	
	private static String __jdbcUrl;
	private static String __jdbcUser;
	private static String __jdbcPasswd;
	private static String __jdbcDriver;

	// Only instantiated by factory within package scope
	public BookDAOXMLImpl() {
		// read your db init properties
	}

	public BookVO findByPrimaryKey(String pPrimaryKey)
	{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BookVO bookVO = null;
		String sql = "SELECT * FROM Book WHERE isbn=?";

        try{
        	// load the jdbc driver
        	Class c1 = Class.forName(__jdbcDriver);
        				
        	// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
        				
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, pPrimaryKey);
            rs = preparedStatement.executeQuery();

            // Populating the value object with the data retrieved from the resultset.
            if (rs.next()){
            	bookVO = new BookVO(rs.getString("isbn"));
            	bookVO.setBookTitle(rs.getString("title"));
            	bookVO.setBookPublisher(rs.getString("publisher"));
            	bookVO.setBookYear(rs.getInt("book_year"));
            }
            return bookVO;
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
        finally{
            try{
                if (rs!=null) {  rs.close(); }
                if (preparedStatement != null) { preparedStatement.close(); }
                if (conn!=null) { conn.close(); }
            }
            catch(SQLException e){
            	e.printStackTrace();
            }
        }
    }
	
	public HashMap<String, ArrayList<String>> findBooksByTitle(String book_title)
	{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		HashMap<String, ArrayList<String>> bookAuthorMap = new HashMap<String, ArrayList<String>>();
		String sql = "SELECT bk.title, au.lastname, au.firstname FROM Book bk" +
					 " join Join_Author_Book jab on jab.isbn = bk.isbn join Author au" +
				     " on au.author_id = jab.author_id WHERE bk.title LIKE ('%' || ? || '%')";

   	    try{
   	    	// load the jdbc driver
        	Class c1 = Class.forName(__jdbcDriver);
        				
        	// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
   	    	
   	    	preparedStatement = conn.prepareStatement(sql);
   	    	preparedStatement.setString(1, book_title);
   	    	rs = preparedStatement.executeQuery();

   	    	while (rs.next()){
   	    		ArrayList<String> authorList = new ArrayList<String>();
		        String book_Ttl = rs.getString("title");
		        String author_name = rs.getString("firstname") + " " + rs.getString("lastname");
                
                if (bookAuthorMap.containsKey(book_Ttl)) {
                	authorList = bookAuthorMap.get(book_Ttl);
                	if (!authorList.contains(author_name)) {
                		authorList.add(author_name);
                		bookAuthorMap.put(book_Ttl, authorList);
                	}
                }
                else {
                	authorList.add(author_name);
                	bookAuthorMap.put(book_Ttl, authorList);
                }
		    }
		   	return bookAuthorMap;
		}
		catch(SQLException sqe){
			/*Aborting the transaction*/
		   	sqe.printStackTrace();
		   	return null;
		}
   	    catch(Exception ge) {
			ge.printStackTrace();
			return null;
		}
		finally{
			try{
				if (rs!=null)  { rs.close(); }
				if (preparedStatement != null) { preparedStatement.close(); }
		   		if (conn!=null) { conn.close(); }
		    }
		   	catch(SQLException e){
		   		e.printStackTrace();
		   	}
		}
   	}
	
	public ArrayList<BookVO> findBooksByAuthorID(int id)
	{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ArrayList<BookVO> bookVOList = new ArrayList<BookVO>();
		String sql = "SELECT bk.isbn, bk.title, bk.publisher, bk.year FROM Book bk" +
					 " join Join_Author_Book jab on jab.isbn = bk.isbn WHERE jab.author_id=?";

   	    try{
   	    	// load the jdbc driver
        	Class c1 = Class.forName(__jdbcDriver);
        				
        	// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
   	    	
   	    	preparedStatement = conn.prepareStatement(sql);
   	    	preparedStatement.setInt(1, id);
   	    	rs = preparedStatement.executeQuery();

   	    	while (rs.next()){
   	    		BookVO bookVO = new BookVO(rs.getString("isbn"));
            	bookVO.setBookTitle(rs.getString("title"));
            	bookVO.setBookPublisher(rs.getString("publisher"));
            	bookVO.setBookYear(rs.getInt("book_year"));
            	bookVOList.add(bookVO);
		    }
		   	return bookVOList;
		}
		catch(SQLException sqe){
			/*Aborting the transaction*/
		   	sqe.printStackTrace();
		   	return null;
		}
   	    catch(Exception ge) {
			ge.printStackTrace();
			return null;
		}
		finally{
			try{
				if (rs!=null)  { rs.close(); }
				if (preparedStatement != null) { preparedStatement.close(); }
		   		if (conn!=null) { conn.close(); }
		    }
		   	catch(SQLException e){
		   		e.printStackTrace();
		   	}
		}
   	}
	
	public ArrayList<AuthorVO> findAuthorsByBookID(String isbn)
	{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ArrayList<AuthorVO> authorVOList = new ArrayList<AuthorVO>();
		String sql = "SELECT au.author_id, au.firstname, au.lastname FROM Author au" +
					 " join Join_Author_Book jab on jab.author_id = au.author_id WHERE jab.isbn=?";

   	    try{
   	    	// load the jdbc driver
        	Class c1 = Class.forName(__jdbcDriver);
        				
        	// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
   	    	
   	    	preparedStatement = conn.prepareStatement(sql);
   	    	preparedStatement.setString(1, isbn);
   	    	rs = preparedStatement.executeQuery();

   	    	while (rs.next()){
   	    		AuthorVO authorVO = new AuthorVO(rs.getInt("author_id"));
   	    		authorVO.setAuthorFirstName(rs.getString("firstname"));
   	    		authorVO.setAuthorLastName(rs.getString("lastname"));
   	    		authorVOList.add(authorVO);
		    }
		   	return authorVOList;
		}
		catch(SQLException sqe){
			/*Aborting the transaction*/
		   	sqe.printStackTrace();
		   	return null;
		}
   	    catch(Exception ge) {
			ge.printStackTrace();
			return null;
		}
		finally{
			try{
				if (rs!=null)  { rs.close(); }
				if (preparedStatement != null) { preparedStatement.close(); }
		   		if (conn!=null) { conn.close(); }
		    }
		   	catch(SQLException e){
		   		e.printStackTrace();
		   	}
		}
   	}

	public HashMap<String, ArrayList<String>> getAllBooksAndAuthors()
	{
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		HashMap<String, ArrayList<String>> bookAuthorMap = new HashMap<String, ArrayList<String>>();
		String sql = "SELECT bk.isbn, bk.title, au.author_id, au.lastname, au.firstname FROM Book bk" +
					 " LEFT JOIN Join_Author_Book jab ON jab.isbn = bk.isbn LEFT JOIN Author au" +
				     " ON au.author_id = jab.author_id";

   	    try{
   	    	// load the jdbc driver
        	Class c1 = Class.forName(__jdbcDriver);
        				
        	// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
   	    	
   	    	preparedStatement = conn.prepareStatement(sql);
   	    	rs = preparedStatement.executeQuery();

   	    	while (rs.next()){
   	    		ArrayList<String> authorList = new ArrayList<String>();
		        String book_Ttl = "isbn: " + rs.getString("isbn") + "    Book_Title: " + rs.getString("title");
		        String author_name = "Author_id: " + rs.getString("author_id") + "    Author_Name: " + rs.getString("firstname") + " " + rs.getString("lastname");
                
                if (bookAuthorMap.containsKey(book_Ttl)) {
                	authorList = bookAuthorMap.get(book_Ttl);
                	if (!authorList.contains(author_name)) {
                		authorList.add(author_name);
                		bookAuthorMap.put(book_Ttl, authorList);
                	}
                }
                else {
                	authorList.add(author_name);
                	bookAuthorMap.put(book_Ttl, authorList);
                }
		    }
		   	return bookAuthorMap;
		}
		catch(SQLException sqe){
			/*Aborting the transaction*/
		   	sqe.printStackTrace();
		   	return null;
		}
   	    catch(Exception ge) {
			ge.printStackTrace();
			return null;
		}
		finally{
			try{
				if (rs!=null)  { rs.close(); }
				if (preparedStatement != null) { preparedStatement.close(); }
		   		if (conn!=null) { conn.close(); }
		    }
		   	catch(SQLException e){
		   		e.printStackTrace();
		   	}
		}
   	}

	/*
     * The update() method will update a single record in the Book table
     * with the valueobject passed in as a parameter.
     *
     */
    public void update(BookVO pValueObject)
    {
        /*Getting my SQL Code for updating all of the Books in my application.*/
    	Connection conn = null;
		PreparedStatement preparedStatement = null;
        String sql = "UPDATE Book SET title=?, publisher=?, book_year=? WHERE isbn=?";
        BookVO bookVO = pValueObject;

        try{
        	// load the jdbc driver
        	Class c1 = Class.forName(__jdbcDriver);
        				
        	// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
			
            /*Populating the prepared statement's parameters*/
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, bookVO.getBookTitle());
            preparedStatement.setString(2, bookVO.getBookPublisher());
            preparedStatement.setInt(3, bookVO.getBookYear());
            preparedStatement.setString(4, bookVO.getBookISBN());

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
     * The delete() record will delete a single record from the Book
     * table in the subscription database.  It does this based on the
     * id of the value object passed into the table.
     *
     */
    @SuppressWarnings("resource")
	public void delete(BookVO pValueObject)
    {
        /* Building my SQL Code for deleting a record from the title table.*/
    	Connection conn = null;
		PreparedStatement preparedStatement = null;
		String sqlAuthorBook = "DELETE FROM Join_Author_Book WHERE isbn=?";
    	String sqlBook = "DELETE FROM Book WHERE isbn=?";
        BookVO bookVO = pValueObject;

        try {
        	// load the jdbc driver
        	Class c1 = Class.forName(__jdbcDriver);
        				
        	// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
        	
			/*Deleting the row from the Join table*/
            preparedStatement = conn.prepareStatement(sqlAuthorBook);
            preparedStatement.setString(1,bookVO.getBookISBN());
            preparedStatement.executeUpdate();
			
            /*Deleting the row from the Book table*/
            preparedStatement = conn.prepareStatement(sqlBook);
            preparedStatement.setString(1,bookVO.getBookISBN());
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
    
    public void deleteAuthorJoin(int author_id, String isbn)
    {
        /* Building my SQL Code for deleting a record from the title table.*/
    	Connection conn = null;
		PreparedStatement preparedStatement = null;
		String sqlAuthorBook = "DELETE FROM Join_Author_Book WHERE isbn=? AND author_id=?";

        try {
        	// load the jdbc driver
        	Class c1 = Class.forName(__jdbcDriver);
        				
        	// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
        	
			/*Deleting the row from the Join table*/
            preparedStatement = conn.prepareStatement(sqlAuthorBook);
            preparedStatement.setString(1,isbn);
            preparedStatement.setInt(2,author_id);
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
     * The insert() method will insert a single record into the Book table.
     * The data it inserts will be pulled from the value object passed into
     * the method.
     *
     */
    @SuppressWarnings("resource")
	public void insert(BookVO pValueObject)
    {
        /*Getting all of my SQL code for inserting a title*/
    	Connection conn = null;
		PreparedStatement preparedStatement = null;
    	String  sqlBook = "INSERT INTO Book VALUES (?,?,?,?)";

        /*Building my SQL Code for inserting a record into the title table.*/
        BookVO bookVO = pValueObject;
        
        try {
        	// load the jdbc driver
        	Class c1 = Class.forName(__jdbcDriver);
        				
        	// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
        	
            /*Populating the prepared statement with data from the value object*/
            preparedStatement = conn.prepareStatement(sqlBook);
            preparedStatement.setString(1, bookVO.getBookISBN());
            preparedStatement.setString(2, bookVO.getBookPublisher());
            preparedStatement.setString(3, bookVO.getBookTitle());
            preparedStatement.setInt(4, bookVO.getBookYear());

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
    
    public void insertAuthorJoin(int author_id, String isbn)
    {
        /*Getting all of my SQL code for inserting a title*/
    	Connection conn = null;
		PreparedStatement preparedStatement = null;
    	String  sqlBook = "INSERT INTO Join_Author_Book VALUES (?,?)";
        
        try {
        	// load the jdbc driver
        	Class c1 = Class.forName(__jdbcDriver);
        				
        	// connect to db based on name
			conn = DriverManager.getConnection(__jdbcUrl, __jdbcUser, __jdbcPasswd);
        	
            /*Populating the prepared statement with data from the value object*/
            preparedStatement = conn.prepareStatement(sqlBook);
            preparedStatement.setInt(1, author_id);
            preparedStatement.setString(2, isbn);

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
    public BookVO createValueObject()
    {
        BookVO bookVO = new BookVO();

        // ID is set on creation of the VO and is immutable

        /* Populating the rest of the value object with data */
        bookVO.setBookTitle("");
        bookVO.setBookPublisher("");
        bookVO.setBookYear(0);

        return bookVO;
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
