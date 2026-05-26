package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.controllers.MediaController;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(MediaController.class)
public class MediaResourceTest {
}
