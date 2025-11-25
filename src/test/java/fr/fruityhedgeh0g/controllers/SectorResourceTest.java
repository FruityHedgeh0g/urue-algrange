package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.controllers.SectorController;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(SectorController.class)
public class SectorResourceTest {

}
