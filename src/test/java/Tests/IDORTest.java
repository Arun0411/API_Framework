package Tests;

import BaseClass.BaseTest;
import Utilities.TestDataUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class IDORTest extends BaseTest {

    // Assuming 123 is a different user's ID (not the one logged in)
    private final Map<String, Object> otherUserPath = Map.of("id", 123);

    private Map<String, Object> userTokenHeaders;

    @BeforeClass
    public void setUp() {
        userTokenHeaders = Map.of("Authorization", "Bearer " + TestDataUtil.getToken("user_token"));
    }

    @Test
    public void testIDOR_ViewOtherUserData_shouldBeForbidden() {
        System.out.println("📁 Test: User A tries to VIEW another user’s data (IDOR)");
        
        Response res = restRequest.sendRequest("GET", "/users/{id}", null, otherUserPath, userTokenHeaders);
        int code = res.getStatusCode();

        System.out.println("GET /users/123 → Status Code: " + code);
        Assert.assertTrue(code == 403 || code == 401, "Expected 403 or 401 but got " + code);
    }

    @Test
    public void testIDOR_EditOtherUserData_shouldBeForbidden() {
        System.out.println("✏️ Test: User A tries to EDIT another user’s data (IDOR)");

        
        Map<String, Object> payload = Map.of(
                "name", "Hacked User",
                "email", "hacked@example.com"
        );

        Response res = restRequest.sendRequest("PUT", "/users/{id}", payload, otherUserPath, userTokenHeaders);
        int code = res.getStatusCode();

        System.out.println("PUT /users/123 → Status Code: " + code);
        Assert.assertEquals(code, 403, "Expected 403 Forbidden when editing another user's data");
    }

    @Test
    public void testIDOR_DeleteOtherUser_shouldBeForbidden() {
        System.out.println("🗑️ Test: User A tries to DELETE another user (IDOR)");

        Response res = restRequest.sendRequest("DELETE", "/users/{id}", null, otherUserPath, userTokenHeaders);
        int code = res.getStatusCode();

        System.out.println("DELETE /users/123 → Status Code: " + code);
        Assert.assertEquals(code, 403, "Expected 403 Forbidden when deleting another user's account");
    }
}
