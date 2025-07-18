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

public class RoleBasedAccessTest extends BaseTest {

    private String adminToken;
    private String userToken;

    private final Map<String, ?> pathParams = Map.of("id", 123);

    @BeforeClass
    public void setUpTokens() throws IOException {
        JsonNode admin = TestDataUtil.getUser("users.json", "admin");
        JsonNode user = TestDataUtil.getUser("users.json", "user");

        adminToken = TestDataUtil.getField(admin, "token");
        userToken = TestDataUtil.getField(user, "token");
    }

    @Test
    public void testAccessWithUserToken_shouldBeRestricted() {
        System.out.println("----- Running with USER token (non-admin) -----");

        RequestSpecification spec = getSpec(userToken);

        // GET /users
        Response getRes = sendRequest("GET", "/users", spec, null, null);
        System.out.println("⛔ GET /users → Status Code: " + getRes.getStatusCode());
        int getStatus = getRes.getStatusCode();
        Assert.assertTrue(getStatus == 200 || getStatus == 403, "GET /users: Expected 200 (if public) or 403");

        // POST /users
        Response postRes = sendRequest("POST", "/users", spec, null, null);
        System.out.println("⛔ POST /users → Status Code: " + postRes.getStatusCode());
        Assert.assertEquals(postRes.getStatusCode(), 403, "POST /users: Expected 403 Forbidden");

        // PUT /users/:id
        Response putRes = sendRequest("PUT", "/users/{id}", spec, null, pathParams);
        System.out.println("⛔ PUT /users/:id → Status Code: " + putRes.getStatusCode());
        Assert.assertEquals(putRes.getStatusCode(), 403, "PUT /users/:id: Expected 403 Forbidden");

        // DELETE /users/:id
        Response delRes = sendRequest("DELETE", "/users/{id}", spec, null, pathParams);
        System.out.println("⛔ DELETE /users/:id → Status Code: " + delRes.getStatusCode());
        Assert.assertEquals(delRes.getStatusCode(), 403, "DELETE /users/:id: Expected 403 Forbidden");
    }

    @Test
    public void testAccessWithAdminToken_shouldBeAllowed() {
        System.out.println("----- Running with ADMIN token -----");

        RequestSpecification spec = getSpec(adminToken);

        // GET /users
        Response getRes = sendRequest("GET", "/users", spec, null, null);
        System.out.println("✅ GET /users → Status Code: " + getRes.getStatusCode());
        Assert.assertTrue(getRes.getStatusCode() >= 200 && getRes.getStatusCode() < 300, "GET /users: Expected success");

        // POST /users
        Response postRes = sendRequest("POST", "/users", spec, null, null);
        System.out.println("✅ POST /users → Status Code: " + postRes.getStatusCode());
        Assert.assertTrue(postRes.getStatusCode() >= 200 && postRes.getStatusCode() < 300, "POST /users: Expected success");

        // PUT /users/:id
        Response putRes = sendRequest("PUT", "/users/{id}", spec, null, pathParams);
        System.out.println("✅ PUT /users/:id → Status Code: " + putRes.getStatusCode());
        Assert.assertTrue(putRes.getStatusCode() >= 200 && putRes.getStatusCode() < 300, "PUT /users/:id: Expected success");

        // DELETE /users/:id
        Response delRes = sendRequest("DELETE", "/users/{id}", spec, null, pathParams);
        System.out.println("✅ DELETE /users/:id → Status Code: " + delRes.getStatusCode());
        Assert.assertTrue(delRes.getStatusCode() >= 200 && delRes.getStatusCode() < 300, "DELETE /users/:id: Expected success");
    }
}
