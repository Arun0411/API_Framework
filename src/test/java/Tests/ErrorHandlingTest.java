package Tests;

import Base.BaseTest;
import Utilities.TestDataUtil;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ErrorHandlingTest extends BaseTest {

    private String adminToken;

    @BeforeClass
    public void setUp() throws IOException {
        JsonNode admin = TestDataUtil.getUser("users.json", "admin");
        adminToken = TestDataUtil.getField(admin, "token");
    }

    @Test
    public void testMissingRequiredFields() {
        RequestSpecification spec = getSpec(adminToken);

        // Missing required fields like name, email, and password
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", ""); // blank name
        // email and password fields are completely missing

        Response res = sendRequest("POST", "/users", spec, payload, null);

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
        RequestSpecification spec = getSpec(adminToken);

        // Intentionally invalid format (e.g., invalid email and password too short)
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Test User");
        payload.put("email", "invalid-email"); // not a valid email
        payload.put("password", "123");        // too short password

        Response res = sendRequest("POST", "/users", spec, payload, null);

        int statusCode = res.getStatusCode();
        String responseBody = res.getBody().asString();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);

        // Again expecting validation error (400)
        Assert.assertTrue(statusCode >= 400 && statusCode < 500, "Expected client-side validation error (4xx)");

        // Should return user-friendly message only
        Assert.assertFalse(responseBody.contains("Exception") ||
                        responseBody.contains("StackTrace") ||
                        responseBody.toLowerCase().contains("sql") ||
                        responseBody.toLowerCase().contains("syntax"),
                "Response should not expose internal error/stack trace");
    }
}
