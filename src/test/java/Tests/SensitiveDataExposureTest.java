package Tests;

import BaseClass.BaseTest;
import Utilities.TestDataUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class SensitiveDataExposureTest extends BaseTest {

    private Map<String, Object> adminTokenHeaders;

    @BeforeClass
    public void setUp() {
        adminTokenHeaders = Map.of("Authorization", "Bearer " + TestDataUtil.getToken("admin_token"));
    }

    @Test
    public void testSensitiveFieldsAreNotExposedInUserList() {
        Response res = restRequest.sendRequest("GET", "/users", null, null, adminTokenHeaders);

        int statusCode = res.getStatusCode();
        String responseBody = res.getBody().asString();

        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);

        Assert.assertEquals(statusCode, 200, "Expected 200 OK for authenticated admin GET /users");

        // Assert sensitive fields are NOT present
        Assert.assertFalse(responseBody.contains("password"), "Password should not be exposed");
        Assert.assertFalse(responseBody.toLowerCase().contains("token"), "Token should not be exposed");
        Assert.assertFalse(responseBody.contains("internalId") || responseBody.contains("secretKey"),
                "Internal or sensitive fields should not be exposed");
    }
}
