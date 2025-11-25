package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.controllers.RoleController;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(RoleController.class)
public class RoleResourceTest {
}
