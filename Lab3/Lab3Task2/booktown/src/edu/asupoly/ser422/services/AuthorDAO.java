package edu.asupoly.ser422.services;

import edu.asupoly.ser422.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public interface AuthorDAO {
    public AuthorVO findByPrimaryKey(int pPrimaryKey);
    public ArrayList<AuthorVO> getAllAuthors();
    public void update(AuthorVO pAuthorVO);
	public void delete(AuthorVO pAuthorVO);
	public void insert(AuthorVO pAuthorVO);
    public AuthorVO createValueObject();
}
