package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.controllers.GroupController;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(GroupController.class)
public class GroupResourceTest {

}
