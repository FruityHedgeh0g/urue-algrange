package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.controllers.FeatureController;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(FeatureController.class)
public class FeatureResourceTest {
}
