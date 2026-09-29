package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.enums.RoleEnum;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.security.DatabaseRoleAugmentor;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Président: PUT /api/users/{id}/president, one Bureau member at most. */
@QuarkusTest
@TestHTTPEndpoint(UserController.class)
public class PresidentResourceTest {

    static final String BUREAU_ID = "00000000-0000-0000-0002-000000000005";
    static final String ADMIN_ID = "00000000-0000-0000-0002-000000000006";
    static final String SUPER_ADMIN_ID = "00000000-0000-0000-0002-000000000007";

    @Inject
    UserRepository userRepository;

    private final List<UUID> created = new ArrayList<>();

    @BeforeEach
    void seedActors() {
        persist(UUID.fromString(BUREAU_ID), RoleEnum.BUREAU);
        persist(UUID.fromString(ADMIN_ID), RoleEnum.ADMIN);
        persist(UUID.fromString(SUPER_ADMIN_ID), RoleEnum.SUPER_ADMIN);
    }

    @AfterEach
    void removePersons() {
        QuarkusTransaction.requiringNew().run(() -> created.forEach(userRepository::deleteById));
        created.clear();
    }

    private UUID persist(UUID id, RoleEnum role) {
        QuarkusTransaction.requiringNew().run(() -> userRepository.persist(
                UserEntity.builder().userId(id).firstName("Test").lastName(role.name()).role(role).build()
        ));
        created.add(id);
        return id;
    }

    private UUID persistPerson(RoleEnum role) {
        return persist(UUID.randomUUID(), role);
    }

    private boolean isPresident(UUID id) {
        return QuarkusTransaction.requiringNew().call(() -> userRepository.findById(id).isPresident());
    }

    private io.restassured.response.ValidatableResponse appoint(UUID personId) {
        return given().when().put("/{id}/president", personId).then();
    }

    @Test
    @TestSecurity(user = "admin", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = ADMIN_ID))
    void adminFlagsABureauMember() {
        UUID bureau = persistPerson(RoleEnum.BUREAU);
        appoint(bureau).statusCode(200).body("president", equalTo(true));
        assertTrue(isPresident(bureau));
    }

    @Test
    @TestSecurity(user = "admin", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = ADMIN_ID))
    void flaggingSomeoneElseClearsThePreviousPresident() {
        UUID first = persistPerson(RoleEnum.BUREAU);
        UUID second = persistPerson(RoleEnum.BUREAU);

        appoint(first).statusCode(200);
        appoint(second).statusCode(200);

        assertFalse(isPresident(first));
        assertTrue(isPresident(second));
    }

    @Test
    @TestSecurity(user = "admin", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = ADMIN_ID))
    void flaggingSomeoneOutsideTheBureauIsRefused() {
        UUID membre = persistPerson(RoleEnum.MEMBRE);
        appoint(membre).statusCode(400);
        appoint(UUID.fromString(ADMIN_ID)).statusCode(400);
        assertFalse(isPresident(membre));
    }

    @Test
    @TestSecurity(user = "admin", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = ADMIN_ID))
    void refusedFlagKeepsThePresident() {
        UUID president = persistPerson(RoleEnum.BUREAU);
        appoint(president).statusCode(200);
        appoint(persistPerson(RoleEnum.MEMBRE)).statusCode(400);
        assertTrue(isPresident(president));
    }

    @Test
    @TestSecurity(user = "admin", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = ADMIN_ID))
    void unknownPersonIsNotFound() {
        appoint(UUID.randomUUID()).statusCode(404);
    }

    @Test
    @TestSecurity(user = "bureau", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = BUREAU_ID))
    void bureauCannotFlag() {
        appoint(persistPerson(RoleEnum.BUREAU)).statusCode(403);
    }

    @Test
    @TestSecurity(user = "admin", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = ADMIN_ID))
    void losingTheBureauRoleClearsTheFlag() {
        UUID president = persistPerson(RoleEnum.BUREAU);
        appoint(president).statusCode(200);

        given().contentType(ContentType.JSON).body(Map.of("role", "membre"))
                .when().put("/{id}/role", president).then().statusCode(200).body("president", equalTo(false));

        assertFalse(isPresident(president));
    }

    @Test
    @TestSecurity(user = "super_admin", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = SUPER_ADMIN_ID))
    void promotionToAdminClearsTheFlag() {
        UUID president = persistPerson(RoleEnum.BUREAU);
        appoint(president).statusCode(200);

        given().contentType(ContentType.JSON).body(Map.of("role", "admin"))
                .when().put("/{id}/role", president).then().statusCode(200);

        assertFalse(isPresident(president));
    }

    @Test
    @TestSecurity(user = "admin", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = ADMIN_ID))
    void thePresidentIsListed() {
        UUID president = persistPerson(RoleEnum.BUREAU);
        appoint(president).statusCode(200);

        List<Boolean> flags = given().when().get("/").then().statusCode(200)
                .extract().jsonPath().getList("findAll { it.president }.president");
        assertEquals(1, flags.size());
        given().when().get("/").then()
                .body("find { it.userId == '" + president + "' }.president", equalTo(true));
    }
}
