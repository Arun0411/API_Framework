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
import java.util.Map;

public class IDORTest extends BaseTest {

    private String userToken;

    // Assuming 123 is a different user's ID (not the one logged in)
    private final Map<String, ?> otherUserPath = Map.of("id", 123);

    @BeforeClass
    public void setUpUserToken() throws IOException {
        JsonNode user = TestDataUtil.getUser("users.json", "user");
        userToken = TestDataUtil.getField(user, "token");
    }

    @Test
    public void testIDOR_ViewOtherUserData_shouldBeForbidden() {
        System.out.println("📁 Test: User A tries to VIEW another user’s data (IDOR)");

        RequestSpecification spec = getSpec(userToken);

        Response res = sendRequest("GET", "/users/{id}", spec, null, otherUserPath);
        int code = res.getStatusCode();

        System.out.println("GET /users/123 → Status Code: " + code);
        Assert.assertTrue(code == 403 || code == 401, "Expected 403 or 401 but got " + code);
    }

    @Test
    public void testIDOR_EditOtherUserData_shouldBeForbidden() {
        System.out.println("✏️ Test: User A tries to EDIT another user’s data (IDOR)");

        RequestSpecification spec = getSpec(userToken);
        Map<String, String> payload = Map.of(
                "name", "Hacked User",
                "email", "hacked@example.com"
        );

        Response res = sendRequest("PUT", "/users/{id}", spec, payload, otherUserPath);
        int code = res.getStatusCode();

        System.out.println("PUT /users/123 → Status Code: " + code);
        Assert.assertEquals(code, 403, "Expected 403 Forbidden when editing another user's data");
    }

    @Test
    public void testIDOR_DeleteOtherUser_shouldBeForbidden() {
        System.out.println("🗑️ Test: User A tries to DELETE another user (IDOR)");

        RequestSpecification spec = getSpec(userToken);

        Response res = sendRequest("DELETE", "/users/{id}", spec, null, otherUserPath);
        int code = res.getStatusCode();

        System.out.println("DELETE /users/123 → Status Code: " + code);
        Assert.assertEquals(code, 403, "Expected 403 Forbidden when deleting another user's account");
    }
}
