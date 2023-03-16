package com.redhat.appfoundation.camel.test;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class RouteTest {

    @Test
    @Disabled
    public void testRestRoute() {

        String path="/api/registration";

        given()
                .when().get(path)
                .then()
                .statusCode(200)
                .log()
                ;
    }
}
