package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.controllers.EventController;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(EventController.class)
public class EventResourceTest {
}
