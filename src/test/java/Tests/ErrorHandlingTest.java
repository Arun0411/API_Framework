package Tests;

import BaseClass.BaseTest;
import Utilities.TestDataUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class ErrorHandlingTest extends BaseTest {

    private Map<String, Object> adminTokenHeaders;

    @BeforeClass
    public void setUp() {
        adminTokenHeaders = Map.of("Authorization", "Bearer " + TestDataUtil.getToken("admin_token"));
    }

    @Test
    public void testMissingRequiredFields() {
        // Missing required fields like name, email, and password
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", ""); // blank name
        // email and password fields are completely missing

        Response res = restRequest.sendRequest("POST", "/users", payload, null, adminTokenHeaders);

        int statusCode = res.getStatusCode();
        String responseBody = res.getBody().asString();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);

        // Expecting 400 Bad Request or appropriate validation error
        Assert.assertTrue(statusCode >= 400 && statusCode < 500, "Expected client-side validation error (4xx)");

        // Check that internal error details are not exposed
        Assert.assertFalse(responseBody.contains("Exception") ||
                        responseBody.contains("StackTrace") ||
                        responseBody.toLowerCase().contains("sql") ||
                        responseBody.toLowerCase().contains("syntax"),
                "Response should not expose internal error/stack trace");
    }

    @Test
    public void testInvalidDataFormat() {

        // Intentionally invalid format (e.g., invalid email and password too short)
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Test User");
        payload.put("email", "invalid-email"); // not a valid email
        payload.put("password", "123");        // too short password

        Response res = restRequest.sendRequest("POST", "/users", payload, null, adminTokenHeaders);

        int statusCode = res.getStatusCode();
        String responseBody = res.getBody().asString();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);

        Assert.assertTrue(statusCode >= 400 && statusCode < 500, "Expected client-side validation error (4xx)");

        Assert.assertFalse(responseBody.contains("Exception") ||
                        responseBody.contains("StackTrace") ||
                        responseBody.toLowerCase().contains("sql") ||
                        responseBody.toLowerCase().contains("syntax"),
                "Response should not expose internal error/stack trace");
    }
}
