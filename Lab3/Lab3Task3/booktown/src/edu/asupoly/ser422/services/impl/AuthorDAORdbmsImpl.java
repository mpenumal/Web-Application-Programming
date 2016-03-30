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

//impl of interface AuthorDAO
public class AuthorDAORdbmsImpl implements AuthorDAO {
	private static int __id = 1;
	
	private static String __jdbcUrl;
	private static String __jdbcUser;
	private static String __jdbcPasswd;
	private static String __jdbcDriver;

	// Only instantiated by factory within package scope
	public AuthorDAORdbmsImpl() {
		// read your db init properties
	}

	public AuthorVO findByPrimaryKey(int pPrimaryKey) 
	{
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
