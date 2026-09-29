package fr.fruityhedgeh0g.controllers;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;

@QuarkusTest
@TestHTTPEndpoint(SectorController.class)
public class SectorResourceTest {

    @Test
    void anonymousVisiteurIsRefusedByTheApi() {
        given().when().get("/").then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "alice")
    void authenticatedPersonListsSecteurs() {
        given().when().get("/").then().statusCode(200).body("$", empty());
    }
}
