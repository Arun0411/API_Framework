package BaseTest;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import static io.restassured.RestAssured.*;

public class BaseRequest {

    public Response sendRequest(String method, String endpoint, RequestSpecification spec, Object body, Map<String, ?> pathParams) {
        if (pathParams != null) {
            spec.pathParams(pathParams);
        }

        switch (method.toUpperCase()) {
            case "GET":
                return given().spec(spec).when().get(endpoint);
            case "POST":
                return given().spec(spec).body(body).when().post(endpoint);
            case "PUT":
                return given().spec(spec).body(body).when().put(endpoint);
            case "DELETE":
                return given().spec(spec).when().delete(endpoint);
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
    }
}