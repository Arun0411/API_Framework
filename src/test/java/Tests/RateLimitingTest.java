package Tests;

import Base.BaseTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class RateLimitingTest extends BaseTest {

    @Test
    public void testRateLimitingWithRepeatedLoginRequests() throws InterruptedException {
        System.out.println("===== Rate Limiting & Brute Force Simulation Test =====");

        RequestSpecification spec = getSpecWithoutAuth();

        Map<String, String> payload = Map.of(
                "username", "user@example.com",  // replace with actual user
                "password", "invalidPassword"
        );

        int totalRequests = 20;
        int count429 = 0;
        int otherErrorCount = 0;

        for (int i = 1; i <= totalRequests; i++) {
            Response res = sendRequest("POST", "/auth/login", spec, payload, null);
            int statusCode = res.getStatusCode();
            System.out.println("Request " + i + " → Status Code: " + statusCode);

            if (statusCode == 429) {
                count429++;
            } else if (statusCode != 401 && statusCode != 403) {
                otherErrorCount++;
            }

            Thread.sleep(100); // 100ms delay between requests to simulate brute force
        }

        System.out.println("➤ Total 429 responses: " + count429);
        System.out.println("➤ Total unexpected errors: " + otherErrorCount);

        Assert.assertTrue(count429 > 0, "❌ Rate limiting was not triggered (no 429 errors)");
        Assert.assertTrue(otherErrorCount == 0, "❌ Unexpected errors occurred during brute force simulation");
    }
}
