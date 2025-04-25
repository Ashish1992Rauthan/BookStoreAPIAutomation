package Steps;

import api.BooksApi;
import api.UserApi;
import data.BookStoreData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.*;


public class UserStepDefs {

    private final BookStoreData bookStoreData;
    private final Map<String, Object> bookDetails = new HashMap<>();
    private final List<Map<String, Object>> allBooksList = new ArrayList<>();

    public UserStepDefs() {
        this.bookStoreData = new BookStoreData();
    }

    // --- SIGN UP ---

    @Given("Sign up to the book store as the new user with email and password")
    public void SignUpToTheBookStoreAsTheNewUserWithValidEmailAndPassword() {
        // Placeholder for any setup before sign-up
    }

    @When("do the sign up with {string} credentials")
    public void doTheSignUpWithValidCredentials(String condition) {
        switch (condition.toLowerCase()) {
            case "valid":
                bookStoreData.setValidEmailUsed(UserApi.generateEmailAndPassword(10) + "@gmail.com");
                bookStoreData.setValidPasswordUsed(UserApi.generateEmailAndPassword(8));
                break;
            case "newpasswordonly":
                bookStoreData.setValidPasswordUsed(UserApi.generateEmailAndPassword(8));
                break;
        }

        Response response = UserApi.signUp(bookStoreData.getValidEmailUsed(), bookStoreData.getValidPasswordUsed(), bookStoreData);
        bookStoreData.setSignUpResponse(response);
    }

    @When("do the sign up with credentials of email {string} and password {string}")
    public void doTheSignUpWithCredentialsOfEmailEmailAndPasswordPassword(String email, String password) {
        UserApi.signUp(email, password, bookStoreData);
    }

    @Then("validate that the response code is {int} and response message should be {string} after sign up")
    public void validateThatTheResponseCodeIsAfterSuccessfulSignUp(int statusCode, String responseMsg) {
        Response response = bookStoreData.getSignUpResponse();
        Assert.assertEquals(response.getStatusCode(), statusCode, "Unexpected status code");

        if (statusCode == 200) {
            Assert.assertEquals(responseMsg, response.jsonPath().getString("message"), "User creation message mismatch");
        } else if (statusCode == 400) {
            Assert.assertEquals(responseMsg, response.jsonPath().getString("detail"), "Expected error detail not found");
        }
    }

    @When("user tried to login with {string} credentials into book store system")
    public void userTriedToLoginWithValidCredentialsIntoBookStoreSystem(String condition) {
        switch (condition.toLowerCase()) {
            case "nosignupuser":
                bookStoreData.setValidEmailUsed(UserApi.generateEmailAndPassword(10) + "@gmail.com");
                bookStoreData.setValidPasswordUsed(UserApi.generateEmailAndPassword(8));
                break;
            case "missingparam":
                bookStoreData.setValidEmailUsed(null);
                bookStoreData.setValidPasswordUsed(null);
                break;
        }

        Response response = UserApi.login(bookStoreData.getValidEmailUsed(), bookStoreData.getValidPasswordUsed());
        bookStoreData.setLogInResponse(response);
    }


    @Then("verify the response after login into book store should {int} and {string}")
    public void verifyTheResponseAfterLoginIntoBookStoreShouldAndSuccess(int statusCode, String condition) {
        Response response = bookStoreData.getLogInResponse();
        Assert.assertEquals(response.getStatusCode(), statusCode, "Unexpected status code");

        switch (condition.toLowerCase()) {
            case "successlogin":
                String token = response.jsonPath().getString("access_token");
                Assert.assertNotNull(token, "Access token not generated");
                Assert.assertEquals(response.jsonPath().getString("token_type"), "bearer", "Token type mismatch");
                bookStoreData.setAccessToken("Bearer " + token);
                break;
            case "incorrectcredentials":
                Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 Bad Request", "Unexpected response line");
                Assert.assertEquals(response.jsonPath().getString("detail"), "Incorrect email or password", "Incorrect credentials message mismatch");
                break;
            case "missingparam":
                Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 422 Unprocessable Entity", "Unexpected response line");
                Assert.assertEquals(response.jsonPath().getString("detail[0].type"), "missing", "Missing param type mismatch");
                Assert.assertEquals(response.jsonPath().getString("detail[0].msg"), "Field required", "Missing param message mismatch");
                break;
        }
    }


    @Given("Adding the new book into the store after successful login of user into the system")
    public void AddingTheNewBookIntoTheStoreAfterSuccessfulLoginOfUserIntoTheSystem() {
        long id = System.nanoTime();
        bookDetails.put("bookName", "Book Title " + id);
        bookDetails.put("author", "Book Author " + id);
        bookDetails.put("published_year", id);
        bookDetails.put("book_summary", "Summary for Book " + id);

        allBooksList.add(new HashMap<>(bookDetails));
    }



    @When("add new book into book store with valid login token of user")
    public void addNewBookIntoBookStoreWithValidLoginTokenOfUser() {
        bookStoreData.setAddBookResponse(
                BooksApi.addNewBook((HashMap<String, Object>) bookDetails, bookStoreData.getAccessToken(), bookStoreData)
        );
    }

    @Then("verify the response after adding the new book should be {string}")
    public void verifyTheResponseAfterAddingTheNewBookShouldBeSuccess(String condition) {
        if ("success".equalsIgnoreCase(condition)) {
            var response = bookStoreData.getAddBookResponse();
            String id = response.jsonPath().getString("id");
            bookDetails.put("createdBookId", id);

            Assert.assertNotNull(id, "Book ID not generated");
            Assert.assertEquals(response.jsonPath().getString("name"), bookDetails.get("bookName"), "Book name mismatch");
            Assert.assertEquals(response.jsonPath().getString("author"), bookDetails.get("author"), "Author mismatch");
            Assert.assertEquals(response.jsonPath().getLong("published_year"), bookDetails.get("published_year"), "Year mismatch");
            Assert.assertEquals(response.jsonPath().getString("book_summary"), bookDetails.get("book_summary"), "Summary mismatch");
        }
    }

    // --- EDIT BOOK ---

    @When("edit the {string} of the book added and verify the response after update")
    public void editTheNameOfTheBookAddedAndVerifyTheResponseAfterUpdate(String field) {
        switch (field.toLowerCase()) {
            case "name": bookDetails.put("bookName", "Edited Book Name"); break;
            case "author": bookDetails.put("author", "Edited Author"); break;
            case "booksummary": bookDetails.put("book_summary", "Edited Summary"); break;
            case "published_year": bookDetails.put("published_year", System.nanoTime()); break;
        }

        String token = "noaccesstoken".equalsIgnoreCase(field) ? null : bookStoreData.getAccessToken();
        bookStoreData.setEditBookResponse(BooksApi.editTheBook((HashMap<String, Object>) bookDetails, token));
    }

    @Then("verify the response after update should be {int}")
    public void verifyTheResponseAfterUpdateShouldBe(int statusCode) {
        var response = bookStoreData.getEditBookResponse();
        Assert.assertEquals(response.getStatusCode(), statusCode, "Unexpected status code");

        switch (statusCode) {
            case 200:
                Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 200 OK", "Expected 200 OK");
                break;
            case 400:
                Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 Bad Request", "Expected 400");
                break;
            case 403:
                Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 403 Forbidden", "Expected 403");
                Assert.assertEquals(response.jsonPath().getString("detail"), "Not authenticated", "Auth error mismatch");
                break;
        }
    }

    @And("verify the edited book details values in response for editing {string}")
    public void verifyEditedBook(String field) {
        var response = bookStoreData.getEditBookResponse();
        Assert.assertEquals(response.jsonPath().getString("name"), bookDetails.get("bookName"));
        Assert.assertEquals(response.jsonPath().getString("author"), bookDetails.get("author"));
        Assert.assertEquals(response.jsonPath().getLong("published_year"), bookDetails.get("published_year"));
        Assert.assertEquals(response.jsonPath().getString("book_summary"), bookDetails.get("book_summary"));
        Assert.assertEquals(response.jsonPath().getString("id"), bookDetails.get("createdBookId"));
    }

    @When("edit the {string} of the book added and verify the response after update")
    public void editBook(String field) {
        switch (field.toLowerCase()) {
            case "name": bookDetails.put("bookName", "Edited Book Name"); break;
            case "author": bookDetails.put("author", "Edited Author"); break;
            case "booksummary": bookDetails.put("book_summary", "Edited Summary"); break;
            case "published_year": bookDetails.put("published_year", System.nanoTime()); break;
        }

        String token = "noaccesstoken".equalsIgnoreCase(field) ? null : bookStoreData.getAccessToken();
        bookStoreData.setEditBookResponse(BooksApi.editTheBook((HashMap<String, Object>) bookDetails, token));
    }

    @Then("verify the response after update should be {int}")
    public void verifyEditResponse(int statusCode) {
        var response = bookStoreData.getEditBookResponse();
        Assert.assertEquals(response.getStatusCode(), statusCode, "Unexpected status code");

        switch (statusCode) {
            case 200:
                Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 200 OK", "Expected 200 OK");
                break;
            case 400:
                Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 Bad Request", "Expected 400");
                break;
            case 403:
                Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 403 Forbidden", "Expected 403");
                Assert.assertEquals(response.jsonPath().getString("detail"), "Not authenticated", "Auth error mismatch");
                break;
        }
    }


    @And("^verify the edited book details values in response for editing (.*)$")
    public void verifyTheEditedBookDetailsValuesInResponseForEditingName(String editedAction)
    {
        var response = bookStoreData.getEditBookResponse();
        Assert.assertEquals(response.jsonPath().getString("name"), bookDetails.get("bookName"));
        Assert.assertEquals(response.jsonPath().getString("author"), bookDetails.get("author"));
        Assert.assertEquals(response.jsonPath().getLong("published_year"), bookDetails.get("published_year"));
        Assert.assertEquals(response.jsonPath().getString("book_summary"), bookDetails.get("book_summary"));
        Assert.assertEquals(response.jsonPath().getString("id"), bookDetails.get("createdBookId"));
    }
    @When("get the details of the particular book using book id generated while creating")
    public void getTheDetailsOfTheParticularBookUsingBookIdGeneratedWhileCreating() {
        bookStoreData.setGetBookDetailsById(BooksApi.getBookDetailsById((HashMap<String, Object>) bookDetails, bookStoreData.getAccessToken()));

    }

    @Then("verify the book details are fetched properly in the response by book id")
    public void verifyTheBookDetailsAreFetchedProperlyInTheResponseByBookId() {
        var response = bookStoreData.getGetBookDetailsById();
        Assert.assertEquals(response.jsonPath().getString("name"), bookDetails.get("bookName"));
        Assert.assertEquals(response.jsonPath().getString("author"), bookDetails.get("author"));
        Assert.assertEquals(response.jsonPath().getLong("published_year"), bookDetails.get("published_year"));
        Assert.assertEquals(response.jsonPath().getString("book_summary"), bookDetails.get("book_summary"));
        Assert.assertEquals(response.jsonPath().getString("id"), bookDetails.get("createdBookId"));
    }

    @Then("verify the book details should not be fetched properly in the response for deleted book id")
    public void verifyTheBookDetailsShouldNotBeFetchedProperlyInTheResponseForDeletedBookId() {
        Assert.assertEquals(bookStoreData.getGetBookDetailsById().jsonPath().getString("detail"), "Book not found");

    }

    @When("fetch all the books that added to the book store")
    public void fetchAllTheBooksThatAddedToTheBookStore() {
        bookStoreData.setFetchAllBooks(BooksApi.getAllBooks(bookStoreData.getAccessToken()));
    }

    @Then("verify the details of books that listed")
    public void verifyTheDetailsOfBooksThatListed() {
        for(Map<String, Object> eachData:allBooksList)
        {
            System.out.println(bookStoreData.getFetchAllBooks().contains(eachData));
        }
    }

    @And("delete the added book in the book store using book id and verify the response")
    public void deleteTheAddedBookInTheBookStoreUsingBookIdAndVerifyTheResponse() {
        bookStoreData.setDeleteBookResponse(
                BooksApi.deleteTheBookById(bookDetails.get("createdBookId").toString(), bookStoreData.getAccessToken())
        );
    }


    @And("^verify the response after deleting the book should be (.*)$")
    public void verifyTheResponseAfterDeletingTheBookShouldBeSuccess(String condition) {
        var response = bookStoreData.getDeleteBookResponse();

        if ("success".equalsIgnoreCase(condition)) {
            Assert.assertEquals(response.getStatusCode(), 200);
            Assert.assertEquals(response.jsonPath().getString("message"), "Book deleted successfully");
        } else if ("notfound".equalsIgnoreCase(condition)) {
            Assert.assertEquals(response.getStatusCode(), 404);
            Assert.assertEquals(response.jsonPath().getString("detail"), "Book not found");
        }
    }

}
