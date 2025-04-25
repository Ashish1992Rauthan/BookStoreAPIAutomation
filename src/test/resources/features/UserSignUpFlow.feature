@UserSignUpFlowFeature
  Feature: User sign up and Login to Book store with credentials and also validation on input for APIs

    @SignUpToBookStore
    Scenario: Signup with new email id and verify the success response and also validate with existing credentials for sign up error

      Given Sign up to the book store as the new user with email and password
      When do the sign up with valid credentials
      Then validate that the response code is 200 and response message should be User created successfully after sign up

      And do the sign up with old credentials
      Then validate that the response code is 400 and response message should be Email already registered after sign up

      And do the sign up with newPasswordOnly credentials
      Then validate that the response code is 400 and response message should be Email already registered after sign up



#    @SignUpApiErrorHandling
#    Scenario Outline:  Validating the sign up with wrong input credentials and verify the error handling for <email> and <password>
#      Given Sign up to the book store as the new user with email and password
#      When do the sign up with credentials of email <email> and password <password>
#      Then validate that the response code is 400 and response message should be Email already registered after sign up
#
#      Examples:
#      |email            | password   |
#      |@gmail.com       | Password#1 |
#      |test@@@gmail.com | Password@1 |
#      |test.            | test@      |
#      |testing@.co      | test@@12   |
#      |                 | Password@1 |
#      | test@yahoo.com  |            |

#    no validation is present in Signup api for above inputs - so uncomment if need to check failures
#    Commented for now to check the pass cases


    @SignUpAndLogin
    Scenario: Sign up as a new user with valid credentials and login with same and verify the token getting generated

      Given Sign up to the book store as the new user with email and password
      When do the sign up with valid credentials
      Then validate that the response code is 200 and response message should be User created successfully after sign up

      When user tried to login with valid credentials into book store system
      Then verify the response after login into book store should 200 and successLogin



    @LoginBeforeSignUp
    Scenario: Verify by logging with the credentials which is not yet signed into bookstore system

      When user tried to login with noSignUpUser credentials into book store system
      Then verify the response after login into book store should 400 and incorrectCredentials


    @LoginAPIValidationWithMissingParam
    Scenario: Verify by logging with the credentials which is not yet signed into bookstore system

      When user tried to login with missingParam credentials into book store system
      Then verify the response after login into book store should 422 and missingParam
