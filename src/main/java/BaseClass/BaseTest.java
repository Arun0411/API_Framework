package BaseClass;

import Utilities.RestRequest;
import org.testng.annotations.BeforeMethod;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected RestRequest restRequest;
    @BeforeMethod
    public void preCondition() {
        restRequest = new RestRequest("https://api.example.com");
    }
}
