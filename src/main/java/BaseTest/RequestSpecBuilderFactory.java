package BaseTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecBuilderFactory {

    public static RequestSpecification getSpec(String token) {
        return new RequestSpecBuilder()
                .setBaseUri("https://api.example.com")
                .addHeader("Authorization", "Bearer " + token)
                .setContentType("application/json")
                .build();
    }

    public static RequestSpecification getSpecWithoutAuth() {
        return new RequestSpecBuilder()
                .setBaseUri("https://api.example.com")
                .setContentType("application/json")
                .build();
    }
}
