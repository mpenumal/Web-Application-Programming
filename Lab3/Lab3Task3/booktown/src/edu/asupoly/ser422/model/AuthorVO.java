/*
 * Taken from J2EE Design Patterns Applied. Modified slightly.
 */
package edu.asupoly.ser422.model;

public class AuthorVO {
  private int authorid;
  private String authorfirstname;
  private String authorlastname;

  public AuthorVO() {
	this.authorid = (int) (System.currentTimeMillis() & 0xfffffff);
  }
  
  public AuthorVO(int id) {
    this.authorid = id;
  }

  public int getAuthorId(){
    return authorid;
  }

  public String getAuthorFirstName(){
    return authorfirstname;
  }

  public String getAuthorLastName(){
    return authorlastname;
  }

  public void setAuthorFirstName(String pFirstName){
    this.authorfirstname = pFirstName;
  }

  public void setAuthorLastName(String pLastName){
	this.authorlastname = pLastName;
  }
}
