package Utilities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestRequest {

    private final RequestSpecification requestSpecification;

    public RestRequest(String baseuri) {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseuri)
                .setContentType("application/json")
                .build();
    }

    public Response sendRequest(String method, String endpoint, Map<String, Object> body, Map<String, Object> pathParams, Map<String, Object> headers) {
        return given()
                .spec(requestSpecification)
                .headers(headers)
                .pathParams(pathParams)
                .body(body)
                .when()
                .request(method.toLowerCase(), endpoint);
    }
}
