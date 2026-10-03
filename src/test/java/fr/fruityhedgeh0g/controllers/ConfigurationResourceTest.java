package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.entities.configurations.ConfigurationEntity;
import fr.fruityhedgeh0g.repositories.ConfigurationRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

/** Site-wide settings: read by everyone, changed by the Super admin only. */
@QuarkusTest
@TestHTTPEndpoint(ConfigurationController.class)
public class ConfigurationResourceTest {

    static final String NAME = "test.setting";

    @Inject
    ConfigurationRepository configurationRepository;

    @BeforeEach
    void seed() {
        QuarkusTransaction.requiringNew().run(() ->
                configurationRepository.persist(ConfigurationEntity.builder().name(NAME).value("Avant").build()));
    }

    @AfterEach
    void cleanUp() {
        QuarkusTransaction.requiringNew().run(() -> configurationRepository.delete("name", NAME));
    }

    private io.restassured.response.ValidatableResponse change(String name, String value) {
        return given().contentType(ContentType.JSON).body("{\"value\":\"" + value + "\"}").when().put("/" + name).then();
    }

    @Test
    void anonymousVisiteurReadsTheSettings() {
        given().when().get().then().statusCode(200).body("name", hasItem(NAME));
    }

    @Test
    void anonymousVisiteurCannotChangeThem() {
        change(NAME, "Après").statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"benevole", "membre", "chef_de_groupe", "bureau", "admin"})
    void anAdminCannotChangeThem() {
        change(NAME, "Après").statusCode(403);
    }

    @Test
    @TestSecurity(user = "superadmin", roles = {"benevole", "membre", "chef_de_groupe", "bureau", "admin", "super_admin"})
    void theSuperAdminChangesThem() {
        change(NAME, "Après").statusCode(200).body("value", equalTo("Après"));
        given().when().get().then().body("find { it.name == '" + NAME + "' }.value", equalTo("Après"));
    }

    @Test
    @TestSecurity(user = "superadmin", roles = {"benevole", "membre", "chef_de_groupe", "bureau", "admin", "super_admin"})
    void anUnknownSettingIsNotCreated() {
        change("test.unknown", "Après").statusCode(404);
    }
}
