package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;

public class CourierSteps {
    public final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    public final String POST_CREATE = "/api/v1/courier";
    public final String POST_LOGIN = "/api/v1/courier/login";
    public final String DELETE_DELETE = "/api/v1/courier/{id}";

    public Response createCourier(String login, String password, String firstName) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("login", login);
        requestBody.put("password", password);
        requestBody.put("firstName", firstName);

        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(requestBody)
                .when()
                .post(POST_CREATE);
    }

    public ValidatableResponse loginCourier(String login, String password) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("login", login);
        requestBody.put("password", password);

        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(requestBody)
                .when()
                .post(POST_LOGIN)
                .then();
    }

    public ValidatableResponse deleteCourier(String id) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .pathParam("id", id)
                .when()
                .delete(DELETE_DELETE)
                .then();
    }
}

