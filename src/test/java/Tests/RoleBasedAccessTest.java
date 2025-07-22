package Tests;

import BaseClass.BaseTest;
import Utilities.TestDataUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class RoleBasedAccessTest extends BaseTest {

    private Map<String, Object> userTokenHeaders;
    private Map<String, Object> adminTokenHeaders;

    @BeforeClass
    public void setUp() {
        userTokenHeaders = Map.of("Authorization", "Bearer " + TestDataUtil.getToken("user_token"));
        adminTokenHeaders = Map.of("Authorization", "Bearer " + TestDataUtil.getToken("admin_token"));
    }

    private final Map<String, Object> pathParams = Map.of("id", 123);

    @Test
    public void testAccessWithUserToken_shouldBeRestricted() {
        System.out.println("----- Running with USER token (non-admin) -----");

        // GET /users
        Response getRes = restRequest.sendRequest("GET", "/users", null, null, userTokenHeaders);
        System.out.println("⛔ GET /users → Status Code: " + getRes.getStatusCode());
        int getStatus = getRes.getStatusCode();
        Assert.assertTrue(getStatus == 200 || getStatus == 403, "GET /users: Expected 200 (if public) or 403");

        // POST /users
        Response postRes = restRequest.sendRequest("POST", "/users", null, null, userTokenHeaders);
        System.out.println("⛔ POST /users → Status Code: " + postRes.getStatusCode());
        Assert.assertEquals(postRes.getStatusCode(), 403, "POST /users: Expected 403 Forbidden");

        // PUT /users/:id
        Response putRes = restRequest.sendRequest("PUT", "/users/{id}", null, pathParams, userTokenHeaders);
        System.out.println("⛔ PUT /users/:id → Status Code: " + putRes.getStatusCode());
        Assert.assertEquals(putRes.getStatusCode(), 403, "PUT /users/:id: Expected 403 Forbidden");

        // DELETE /users/:id
        Response delRes = restRequest.sendRequest("DELETE", "/users/{id}", null, pathParams, userTokenHeaders);
        System.out.println("⛔ DELETE /users/:id → Status Code: " + delRes.getStatusCode());
        Assert.assertEquals(delRes.getStatusCode(), 403, "DELETE /users/:id: Expected 403 Forbidden");
    }

    @Test
    public void testAccessWithAdminToken_shouldBeAllowed() {
        System.out.println("----- Running with ADMIN token -----");

        // GET /users
        Response getRes = restRequest.sendRequest("GET", "/users", null, null, adminTokenHeaders);
        System.out.println("✅ GET /users → Status Code: " + getRes.getStatusCode());
        Assert.assertTrue(getRes.getStatusCode() >= 200 && getRes.getStatusCode() < 300, "GET /users: Expected success");

        // POST /users
        Response postRes = restRequest.sendRequest("POST", "/users", null, null, adminTokenHeaders);
        System.out.println("✅ POST /users → Status Code: " + postRes.getStatusCode());
        Assert.assertTrue(postRes.getStatusCode() >= 200 && postRes.getStatusCode() < 300, "POST /users: Expected success");

        // PUT /users/:id
        Response putRes = restRequest.sendRequest("PUT", "/users/{id}", null, pathParams, adminTokenHeaders);
        System.out.println("✅ PUT /users/:id → Status Code: " + putRes.getStatusCode());
        Assert.assertTrue(putRes.getStatusCode() >= 200 && putRes.getStatusCode() < 300, "PUT /users/:id: Expected success");

        // DELETE /users/:id
        Response delRes = restRequest.sendRequest("DELETE", "/users/{id}", null, pathParams, adminTokenHeaders);
        System.out.println("✅ DELETE /users/:id → Status Code: " + delRes.getStatusCode());
        Assert.assertTrue(delRes.getStatusCode() >= 200 && delRes.getStatusCode() < 300, "DELETE /users/:id: Expected success");
    }
}
