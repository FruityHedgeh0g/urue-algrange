package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.security.DatabaseRoleAugmentor;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(GroupController.class)
public class GroupResourceTest {

    @Test
    @TestSecurity(user = "alice", augmentors = DatabaseRoleAugmentor.class)
    void authenticatedPersonListsGroupes() {
        given().when().get("/").then().statusCode(200);
    }
}
