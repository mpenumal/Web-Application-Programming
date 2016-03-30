/*
 * Taken from J2EE Design Patterns Applied. Modified slightly.
 */
package edu.asupoly.ser422.model;

import java.util.UUID;

public class BookVO {
  private String title;
  private String publisher;
  private String isbn;
  private int year;
  
  public BookVO() {
	this.isbn = UUID.randomUUID().toString();
  }
		  
  public BookVO(String id) {
	this.isbn = id;
  }

  public String getBookISBN(){
    return isbn;
  }
  
  public String getBookTitle(){
    return title;
  }
  
  public String getBookPublisher(){
	return publisher;
  }
  
  public int getBookYear(){
	return year;
  }
  
  public void setBookTitle(String pTitle){
    this.title = pTitle;
  }

  public void setBookPublisher(String pPublisher){
	  this.publisher = pPublisher;
  }
  
  public void setBookYear(int pYear){
    this.year = pYear;
  }
}
