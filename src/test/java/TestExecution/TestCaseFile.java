package TestExecution;

import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import BaseTest.*;
import static io.restassured.RestAssured.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestCaseFile extends BaseRequest {

    @Test(priority = 1)
    public void testAuthenticationWithoutToken() {
        RequestSpecification spec = RequestSpecBuilderFactory.getSpecWithoutAuth();
        Response response = sendRequest("GET", "/users", spec, null, null);
        Assert.assertTrue(response.statusCode() == 401 || response.statusCode() == 403);
    }

    @Test(priority = 2)
    public void testAuthorizationWithUserToken() throws IOException {
        JsonNode userData = TestDataUtil.getTestUser("user");
        String token = userData.get("token").asText();
        RequestSpecification spec = RequestSpecBuilderFactory.getSpec(token);
        Map<String, Object> pathParam = new HashMap<>();
        pathParam.put("id", 5);

        Response response = sendRequest("DELETE", "/users/{id}", spec, null, pathParam);
        Assert.assertEquals(response.statusCode(), 403);
    }

    @Test(priority = 3)
    public void testSQLInjectionOnCreateUser() throws IOException {
        JsonNode malicious = TestDataUtil.getTestUser("malicious");
        JsonNode admin = TestDataUtil.getTestUser("admin");
        RequestSpecification spec = RequestSpecBuilderFactory.getSpec(admin.get("token").asText());

        Response response = sendRequest("POST", "/users", spec, malicious, null);
        Assert.assertTrue(response.statusCode() == 400 || response.statusCode() == 422);
    }
}




//
//public class TestCase_1 extends ChromeDriverManager {
//
//    @Test(priority = 1)
//    public void loginAndCheckDashboard() throws InterruptedException {
//        Homepage homepage = new Homepage(getDriver());  // pass initialized driver
//        homepage.login("arun@regression.com", "Rently@1234");
//    }
//
//    @Test(priority = 2)
//    public void loginAndCheckDashboard2() throws InterruptedException {
//        Homepage homepage = new Homepage(getDriver());  // pass initialized driver
//        homepage.login("arun@regression.com", "Rently@1234");
//    }
//}
