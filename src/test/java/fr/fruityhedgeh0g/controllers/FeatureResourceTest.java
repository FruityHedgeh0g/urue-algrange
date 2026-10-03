package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.entities.configurations.FeatureEntity;
import fr.fruityhedgeh0g.repositories.FeatureRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

/** Fonctionnalités: read by everyone, switched by the Super admin only (#27). */
@QuarkusTest
@TestHTTPEndpoint(FeatureController.class)
class FeatureResourceTest {

    static final String NAME = "test-feature";

    @Inject FeatureRepository featureRepository;

    @BeforeEach
    void seed() {
        QuarkusTransaction.requiringNew().run(() ->
                featureRepository.persist(FeatureEntity.builder().name(NAME).description("Test").isActive(false).build()));
    }

    @AfterEach
    void cleanUp() {
        QuarkusTransaction.requiringNew().run(() -> featureRepository.delete("name", NAME));
    }

    private ValidatableResponse turnOn(String name) {
        return given().contentType(ContentType.JSON).body("{\"isActive\":true}").when().put("/" + name).then();
    }

    @Test
    void anonymousVisiteurReadsThem() {
        given().when().get().then().statusCode(200).body("name", hasItem(NAME));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"benevole", "membre", "chef_de_groupe", "bureau", "admin"})
    void anAdminCannotSwitchThem() {
        turnOn(NAME).statusCode(403);
    }

    @Test
    @TestSecurity(user = "superadmin", roles = {"benevole", "membre", "chef_de_groupe", "bureau", "admin", "super_admin"})
    void theSuperAdminSwitchesThem() {
        turnOn(NAME).statusCode(200).body("isActive", equalTo(true));
        turnOn("test-unknown").statusCode(404);
    }
}
