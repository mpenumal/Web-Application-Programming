
Design Decisions:
1. There cannot be multiple records with same first name and last name.
2. A user record is saved only when all the fields are provided, i.e., all the attributes have an associated value.
3. A user can update her/his information. This is done by entering her/his first name and last name in the '/post_info_capture1' page.
4. A user can add new users and their information. This is done by entering the new user's first name and last name in the '/post_info_capture1' page.
5. A user can update existing users and their information. This is done by entering the existing user's first name and last name in the '/post_info_capture1' page.
6. Login information is stored in cookies.
7. Conversational state is maintained in session.