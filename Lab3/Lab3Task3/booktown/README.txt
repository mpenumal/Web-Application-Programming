
1. Created AuthorDAO and BookDAO and placed in services folder.
2. Created valueObjects AuthorVO and BookVO with the required getters and setters.
3. Implemented findBooksByTitle() method.
4. Created AuthorDAORdbmsImpl for implementing AuthorDAO and created BookDAORdbmsImpl for implementing BookDAO.
5. Implemented doGet to get associated information. Below urls are examples:
	http://localhost:8080/lab3part2/get_booktown_info?id=1
	http://localhost:8080/lab3part2/get_booktown_info?isbn=a1b2c3
	http://localhost:8080/lab3part2/get_booktown_info?title=Servlets
	http://localhost:8080/lab3part2/get_booktown_info
6. Added Html form to allow manipulations on Author data. The below url takes us there. The page shows list of authors. Please refresh url or check db to see the changes.
	http://localhost:8080/lab3part2/author_page
7. Added Html form to allow manipulations on Book data. The below url takes us there. The page shows list of books and the associated authors. 
	We also show list of authors. Please refresh url or check db to see the changes.
	http://localhost:8080/lab3part2/book_page

NOTE: Activity1 related code is still accessible under the below url:
	http://localhost:8080/lab3part2/booktown
	
db and tables setup:
	connect 'jdbc:derby:MyDbTest;create=true';
	connect 'jdbc:derby:AuthorBookDB';
	create table Author(author_id INT NOT NULL, firstname varchar(20), lastname varchar(20), PRIMARY KEY (author_id));
	create table Book (isbn varchar(40) NOT NULL, publisher varchar(20), title varchar(25), book_year int, PRIMARY KEY (isbn));
	create table Join_Author_Book (author_id int, isbn varchar(40), FOREIGN KEY (author_id) REFERENCES Author(author_id), FOREIGN KEY (isbn) REFERENCES Book(isbn));
