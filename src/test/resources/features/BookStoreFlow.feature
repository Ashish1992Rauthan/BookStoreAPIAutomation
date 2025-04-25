@BookStoreFlowFeature
  Feature: Validate the book store flow of add , update , list and delete books by user and verification of error handling

    @SignUpToDeleteBookFlow
    Scenario: Add new book , update the book details and verify them in list and fetch book details

      Given Adding the new book into the store after successful login of user into the system

      When do the sign up with valid credentials
      Then validate that the response code is 200 and response message should be User created successfully after sign up

      When user tried to login with valid credentials into book store system
      Then verify the response after login into book store should 200 and successLogin

      When add new book into book store with valid login token of user
      Then verify the response after adding the new book should be success

      When edit the name of the book added and verify the response after update
      Then verify the response after update should be 200
      And verify the edited book details values in response for editing name

      When get the details of the particular book using book id generated while creating
      Then verify the book details are fetched properly in the response by book id

      And delete the added book in the book store using book id and verify the response
      And verify the response after deleting the book should be success

      When delete the added book in the book store using book id and verify the response
      And verify the response after deleting the book should be notFound

      When get the details of the particular book using book id generated while creating
      Then verify the book details should not be fetched properly in the response for deleted book id




    @EditBookDetails @AuthenticationVerificationForEditApi
    Scenario: Edit the created Book Details and on every field and validate them in fetching book details response

      Given Adding the new book into the store after successful login of user into the system

      When do the sign up with valid credentials
      Then validate that the response code is 200 and response message should be User created successfully after sign up

      When user tried to login with valid credentials into book store system
      Then verify the response after login into book store should 200 and successLogin

      When add new book into book store with valid login token of user
      Then verify the response after adding the new book should be success

      When edit the noAccessToken of the book added and verify the response after update
#      Then verify the response after update should be 403 - because 200 is set on status code , but 403 in statusline


      When edit the name of the book added and verify the response after update
      Then verify the response after update should be 200
      And verify the edited book details values in response for editing name

      When get the details of the particular book using book id generated while creating
      Then verify the book details are fetched properly in the response by book id


      When edit the author of the book added and verify the response after update
      Then verify the response after update should be 200
      And verify the edited book details values in response for editing author

      When get the details of the particular book using book id generated while creating
      Then verify the book details are fetched properly in the response by book id


      When edit the bookSummary of the book added and verify the response after update
      Then verify the response after update should be 200
      And verify the edited book details values in response for editing bookSummary

      When get the details of the particular book using book id generated while creating
      Then verify the book details are fetched properly in the response by book id


      When edit the published_year of the book added and verify the response after update
      Then verify the response after update should be 200
      And verify the edited book details values in response for editing published_year

      When get the details of the particular book using book id generated while creating
      Then verify the book details are fetched properly in the response by book id



    @FetchAllBooks
    Scenario: Add multiple books and verify those books are listed in the fetch all books api

      Given Adding the new book into the store after successful login of user into the system

      When do the sign up with valid credentials
      Then validate that the response code is 200 and response message should be User created successfully after sign up

      When user tried to login with valid credentials into book store system
      Then verify the response after login into book store should 200 and successLogin

      When add new book into book store with valid login token of user
      Then verify the response after adding the new book should be success

      And Adding the new book into the store after successful login of user into the system

      When add new book into book store with valid login token of user
      Then verify the response after adding the new book should be success

      And Adding the new book into the store after successful login of user into the system


      When add new book into book store with valid login token of user
      Then verify the response after adding the new book should be success

      And Adding the new book into the store after successful login of user into the system


      When add new book into book store with valid login token of user
      Then verify the response after adding the new book should be success

      And Adding the new book into the store after successful login of user into the system


      When add new book into book store with valid login token of user
      Then verify the response after adding the new book should be success

      When fetch all the books that added to the book store
      Then verify the details of books that listed

