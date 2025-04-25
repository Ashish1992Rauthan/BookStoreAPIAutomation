package api;

import constants.EndPoints;
import data.BookStoreData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserApi {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    // --- Utility Methods ---

    public static String generateEmailAndPassword(int length) {
        StringBuilder generated = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            generated.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return generated.toString();
    }

    private static RequestSpecification buildRequest() {
        return given()
                .contentType(ContentType.JSON)
                .log().all();
    }

    private static Map<String, String> createCredentialPayload(String email, String password) {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password", password);
        return payload;
    }

    // --- API Methods ---

    public static Response signUp(String email, String password, BookStoreData bookStoreData) {
        return buildRequest()
                .body(createCredentialPayload(email, password))
                .when()
                .post(EndPoints.SING_UP)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public static Response login(String email, String password) {
        RequestSpecification request = buildRequest();

        if (email != null && password != null) {
            request.body(createCredentialPayload(email, password));
        }

        return request
                .when()
                .post(EndPoints.LOG_IN)
                .then()
                .log().all()
                .extract()
                .response();
    }
}
