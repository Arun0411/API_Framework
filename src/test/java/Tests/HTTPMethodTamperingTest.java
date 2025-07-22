package Tests;

import BaseClass.BaseTest;
import Utilities.TestDataUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class HTTPMethodTamperingTest extends BaseTest {

    private Map<String, Object> userTokenHeaders;

    @BeforeClass
    public void setUp() {
        userTokenHeaders = Map.of("Authorization", "Bearer " + TestDataUtil.getToken("user_token"));
    }
    
    @Test
    public void testUnsupportedMethods_shouldBeBlocked() {

        String[] unsupportedMethods = {"PATCH", "TRACE", "OPTIONS", "CONNECT"};

        for (String method : unsupportedMethods) {
            System.out.println("🔍 Testing unsupported HTTP method: " + method);

            Response res;
            try {
                res = restRequest.sendRequest(method, "/users", null, null, null);
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
}
