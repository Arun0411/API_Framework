package Base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class BaseTest {

    protected String BASE_URI = "https://api.example.com"; // Replace with your actual URI

    public RequestSpecification getSpec(String token) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .addHeader("Authorization", "Bearer " + token)
                .setContentType("application/json")
                .build();
    }

    public RequestSpecification getSpecWithoutAuth() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType("application/json")
                .build();
    }

    public Response sendRequest(String method, String endpoint, RequestSpecification spec, Object body, Map<String, ?> pathParams) {
        if (pathParams != null) {
            spec.pathParams(pathParams);
        }

        return switch (method.toUpperCase()) {
            case "GET" -> given().spec(spec).when().get(endpoint);
            case "POST" -> given().spec(spec).body(body).when().post(endpoint);
            case "PUT" -> given().spec(spec).body(body).when().put(endpoint);
            case "DELETE" -> given().spec(spec).when().delete(endpoint);
            default -> throw new IllegalArgumentException("Invalid HTTP method: " + method);
        };
    }
}
