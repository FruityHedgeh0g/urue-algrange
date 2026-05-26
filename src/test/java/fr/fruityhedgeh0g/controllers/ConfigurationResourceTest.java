package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.controllers.ConfigurationController;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(ConfigurationController.class)
public class ConfigurationResourceTest {
}
