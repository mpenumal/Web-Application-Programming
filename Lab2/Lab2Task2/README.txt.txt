SER598 - Web Application Programming

Lab2 Task2:
1. Firstname and lastname are taken as login information.
2. Cookies are used to hold the firstname and lastname. Expiry is set for 20 minutes.
3. Going to any of the other pages without login will redirect the user to landing page.
4. Input information is stored in session. Expiry is set for 20 minutes.
5. Preference matches are displayed by comparing login firstname and lastname with xml content and ]
      getting the records which match the remaining properties of that record in xml.
6. Bad request (400) is displayed when days or location is misspelled.
7. Method not allowed (405) is shown when GET call is made on POST servlets and vice-versa.