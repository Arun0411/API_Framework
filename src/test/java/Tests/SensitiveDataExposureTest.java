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

public class SensitiveDataExposureTest extends BaseTest {

    private String adminToken;

    @BeforeClass
    public void setUp() throws IOException {
        JsonNode admin = TestDataUtil.getUser("users.json", "admin");
        adminToken = TestDataUtil.getField(admin, "token");
    }

    @Test
    public void testSensitiveFieldsAreNotExposedInUserList() {
        RequestSpecification spec = getSpec(adminToken);
        Response res = sendRequest("GET", "/users", spec, null, null);

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
