package edu.asupoly.ser422.services;

import edu.asupoly.ser422.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public interface BookDAO {
    public BookVO findByPrimaryKey(String pPrimaryKey);
    public HashMap<String, ArrayList<String>> findBooksByTitle(String book_title);
    public ArrayList<BookVO> findBooksByAuthorID(int id);
    public ArrayList<AuthorVO> findAuthorsByBookID(String isbn);
    public HashMap<String, ArrayList<String>> getAllBooksAndAuthors();
    public void update(BookVO pValueObject);
	public void delete(BookVO pValueObject);
	public void insert(BookVO pValueObject);
	public void insertAuthorJoin(int id, String isbn);
	public void deleteAuthorJoin(int id, String isbn);
    public BookVO createValueObject();
}
