package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.controllers.PostController;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(PostController.class)
public class PostResourceTest {
}
