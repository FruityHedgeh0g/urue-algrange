package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.controllers.UserController;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(UserController.class)
public class UserResourceTest {
}
