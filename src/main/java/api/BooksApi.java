package api;

import constants.EndPoints;
import data.BookStoreData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.*;

import static io.restassured.RestAssured.given;

public class BooksApi {

    private static RequestSpecification buildRequest(String accessToken) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON)
                .log().all();

        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", accessToken);
        }

        return request;
    }

    private static Map<String, Object> createBookPayload(Map<String, Object> bookDetails) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", bookDetails.get("bookName"));
        payload.put("author", bookDetails.get("author"));
        payload.put("published_year", bookDetails.get("published_year"));
        payload.put("book_summary", bookDetails.get("book_summary"));
        return payload;
    }

    public static Response addNewBook(Map<String, Object> bookDetails, String accessToken, BookStoreData bookStoreData) {
        RequestSpecification request = buildRequest(accessToken);

        if (!bookDetails.isEmpty()) {
            request.body(createBookPayload(bookDetails));
        }

        return request
                .when()
                .post(EndPoints.ADD_NEW_BOOK)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public static Response editTheBook(Map<String, Object> bookDetails, String accessToken) {
        RequestSpecification request = buildRequest(accessToken);

        if (!bookDetails.isEmpty()) {
            request.body(createBookPayload(bookDetails));
        }

        return request
                .pathParam("book_id", bookDetails.get("createdBookId"))
                .when()
                .put(EndPoints.BY_BOOK_ID)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public static Response getBookDetailsById(Map<String, Object> bookDetails, String accessToken) {
        return buildRequest(accessToken)
                .pathParam("book_id", bookDetails.get("createdBookId"))
                .when()
                .get(EndPoints.BY_BOOK_ID)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public static List<Response> getAllBooks(String accessToken) {
        Response response = buildRequest(accessToken)
                .when()
                .get(EndPoints.ADD_NEW_BOOK)
                .then()
                .log().all()
                .extract()
                .response();

        return Collections.singletonList(response);
    }

    public static Response deleteTheBookById(String bookId, String accessToken) {
        return buildRequest(accessToken)
                .pathParam("book_id", bookId)
                .when()
                .delete(EndPoints.BY_BOOK_ID)
                .then()
                .log().all()
                .extract()
                .response();
    }
}
