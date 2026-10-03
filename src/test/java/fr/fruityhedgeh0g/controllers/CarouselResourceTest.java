package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.repositories.CarouselItemRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

/** The home page's carousel: Visiteurs see its active slides, the Bureau manages them all. */
@QuarkusTest
@TestHTTPEndpoint(CarouselController.class)
class CarouselResourceTest {

    @Inject CarouselItemRepository itemRepository;

    @AfterEach
    void cleanUp() {
        QuarkusTransaction.requiringNew().run(() -> itemRepository.deleteAll());
    }

    private ValidatableResponse create(String title, boolean active, String linkTo) {
        String link = linkTo == null ? "null" : "\"" + linkTo + "\"";
        return given().contentType(ContentType.JSON)
                .body("{\"title\":\"" + title + "\",\"caption\":\"\",\"linkTo\":" + link + ",\"active\":" + active + "}")
                .when().post().then();
    }

    @Test
    @TestSecurity(user = "bureau", roles = {"benevole", "membre", "chef_de_groupe", "bureau"})
    void theBureauWritesAndOrdersSlides() {
        create("Balade", true, "/evenements").statusCode(200).body("order", equalTo(1));
        String loto = create("Loto", true, null).statusCode(200).body("order", equalTo(2)).extract().path("id");

        given().when().post("/" + loto + "/move/up").then().statusCode(200).body("title", contains("Loto", "Balade"));
        given().contentType(ContentType.JSON).body("{\"title\":\"Grand loto\",\"active\":false}")
                .when().put("/" + loto).then().statusCode(200).body("active", equalTo(false));
        given().when().get().then().body("title", contains("Grand loto", "Balade"));
    }

    @Test
    @TestSecurity(user = "bureau", roles = {"benevole", "membre", "chef_de_groupe", "bureau"})
    void aSlideLinksToAPageOfTheSite() {
        create("Ailleurs", true, "https://example.org").statusCode(400);
        create("Ailleurs", true, "//example.org").statusCode(400);
        create(" ", true, null).statusCode(400);
    }

    @Test
    void visiteursSeeOnlyTheActiveSlides() {
        List.of(true, false).forEach(active -> QuarkusTransaction.requiringNew().run(() -> {
            var item = new fr.fruityhedgeh0g.entities.CarouselItemEntity();
            item.setTitle(active ? "Visible" : "De côté");
            item.setActive(active);
            item.setPosition(active ? 1 : 2);
            itemRepository.persist(item);
        }));
        given().when().get().then().statusCode(200).body("title", contains("Visible"));
    }

    @Test
    void anonymousVisiteurCannotWrite() {
        create("Balade", true, null).statusCode(401);
        given().when().get().then().body("$", empty());
    }

    @Test
    @TestSecurity(user = "membre", roles = {"benevole", "membre"})
    void aMembreCannotWrite() {
        create("Balade", true, null).statusCode(403);
    }
}
