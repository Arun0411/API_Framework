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

public class HTTPMethodTamperingTest extends BaseTest {

    private String userToken;

    @BeforeClass
    public void setUpToken() throws IOException {
        JsonNode user = TestDataUtil.getUser("users.json", "user");
        userToken = TestDataUtil.getField(user, "token");
    }

    @Test
    public void testUnsupportedMethods_shouldBeBlocked() {
        RequestSpecification spec = getSpec(userToken);

        String[] unsupportedMethods = {"PATCH", "TRACE", "OPTIONS", "CONNECT"};

        for (String method : unsupportedMethods) {
            System.out.println("🔍 Testing unsupported HTTP method: " + method);

            Response res;
            try {
                res = sendTamperedMethod(method, "/users", spec);
                int statusCode = res.getStatusCode();

                System.out.println(method + " /users → Status Code: " + statusCode);
                Assert.assertTrue(
                        statusCode == 405 || statusCode == 400 || statusCode == 501,
                        "Expected 405/400/501 for method " + method + " but got " + statusCode
                );
            } catch (Exception e) {
                System.out.println("✅ " + method + " is likely blocked or not supported: " + e.getMessage());
            }
        }
    }

    // Custom method to bypass sendRequest() switch logic and use unsupported HTTP verbs
    private Response sendTamperedMethod(String method, String endpoint, RequestSpecification spec) {
        return io.restassured.RestAssured
                .given()
                .spec(spec)
                .request(method, BASE_URI + endpoint);
    }
}
