package fr.fruityhedgeh0g.controllers;

import fr.fruityhedgeh0g.entities.EventEntity;
import fr.fruityhedgeh0g.entities.EventRegistrationEntity;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.entities.SectorEntity;
import fr.fruityhedgeh0g.entities.UserEntity;
import fr.fruityhedgeh0g.enums.EventStatusEnum;
import fr.fruityhedgeh0g.enums.RoleEnum;
import fr.fruityhedgeh0g.repositories.EventRegistrationRepository;
import fr.fruityhedgeh0g.repositories.EventRepository;
import fr.fruityhedgeh0g.repositories.GroupRepository;
import fr.fruityhedgeh0g.repositories.SectorRepository;
import fr.fruityhedgeh0g.repositories.UserRepository;
import fr.fruityhedgeh0g.security.DatabaseRoleAugmentor;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** A passager signs up with a pilote of the same Event, counts towards every maximum and always follows their pilote. */
@QuarkusTest
@TestHTTPEndpoint(EventController.class)
public class PassagerResourceTest {

    static final String PILOTE_ID = "00000000-0000-0000-0009-000000000001";
    static final String PASSAGER_ID = "00000000-0000-0000-0009-000000000002";
    static final String OTHER_ID = "00000000-0000-0000-0009-000000000003";
    static final String CHEF_ID = "00000000-0000-0000-0009-000000000004";
    static final String BUREAU_ID = "00000000-0000-0000-0009-000000000005";

    @Inject
    UserRepository userRepository;

    @Inject
    SectorRepository sectorRepository;

    @Inject
    GroupRepository groupRepository;

    @Inject
    EventRepository eventRepository;

    @Inject
    EventRegistrationRepository registrationRepository;

    private final List<UUID> persons = new ArrayList<>();
    private UUID sector;
    private UUID nord;
    private UUID event;

    @BeforeEach
    void seed() {
        persistPerson(PILOTE_ID, RoleEnum.BENEVOLE, "Pilote");
        persistPerson(PASSAGER_ID, RoleEnum.BENEVOLE, "Passager");
        persistPerson(OTHER_ID, RoleEnum.BENEVOLE, "Autre");
        persistPerson(CHEF_ID, RoleEnum.CHEF_DE_GROUPE, "Chef");
        persistPerson(BUREAU_ID, RoleEnum.BUREAU, "Bureau");
        QuarkusTransaction.requiringNew().run(() -> {
            SectorEntity s = SectorEntity.builder().name("Test Secteur " + UUID.randomUUID()).build();
            sectorRepository.persist(s);
            sector = s.getSectorId();
            GroupEntity group = GroupEntity.builder().name("Test Nord " + UUID.randomUUID()).sector(s)
                    .chef(userRepository.findById(UUID.fromString(CHEF_ID))).build();
            groupRepository.persist(group);
            nord = group.getGroupId();
        });
        event = persistEvent(null);
    }

    @AfterEach
    void cleanUp() {
        QuarkusTransaction.requiringNew().run(() -> {
            // Passagers first: they point at their pilote's sign-up
            registrationRepository.delete("event.sector.sectorId = ?1 and pilote is not null", sector);
            registrationRepository.delete("event.sector.sectorId", sector);
            eventRepository.list("sector.sectorId", sector).forEach(eventRepository::delete);
            groupRepository.delete("sector.sectorId", sector);
            sectorRepository.deleteById(sector);
        });
        QuarkusTransaction.requiringNew().run(() -> persons.forEach(userRepository::deleteById));
        persons.clear();
    }

    private void persistPerson(String id, RoleEnum role, String lastName) {
        UUID userId = UUID.fromString(id);
        QuarkusTransaction.requiringNew().run(() -> userRepository.persist(
                UserEntity.builder().userId(userId).firstName("Test").lastName(lastName).role(role).phone("06 00 00 00 00").build()
        ));
        persons.add(userId);
    }

    private UUID persistEvent(Integer max) {
        return QuarkusTransaction.requiringNew().call(() -> {
            EventEntity e = new EventEntity();
            e.setName("Test Balade");
            e.setStatus(EventStatusEnum.OUVERT);
            e.setStartDateTime(LocalDateTime.now(EventEntity.ZONE).plusDays(1));
            e.setEndDateTime(LocalDateTime.now(EventEntity.ZONE).plusDays(2));
            e.setMaxParticipants(max);
            e.setSector(sectorRepository.findById(sector));
            eventRepository.persist(e);
            return e.getEventId();
        });
    }

    private void maximumIs(Integer max) {
        QuarkusTransaction.requiringNew().run(() -> eventRepository.findById(event).setMaxParticipants(max));
    }

    private void nordMaximumIs(int max) {
        QuarkusTransaction.requiringNew().run(() -> eventRepository.findById(event).getGroupMaximums().put(nord, max));
    }

    /** Signs a pilote up directly in the database. */
    private void piloteSignedUp(String personId, UUID eventId, boolean waiting) {
        QuarkusTransaction.requiringNew().run(() -> registrationRepository.persist(EventRegistrationEntity.pilote(
                eventRepository.findById(eventId), userRepository.findById(UUID.fromString(personId)), waiting)));
    }

    private void piloteSignedUp(boolean waiting) {
        piloteSignedUp(PILOTE_ID, event, waiting);
    }

    /** Signs the passager up with the pilote directly in the database, mirroring the pilote. */
    private void passagerSignedUp() {
        QuarkusTransaction.requiringNew().run(() -> registrationRepository.persist(EventRegistrationEntity.passager(
                registrationOf(PILOTE_ID), userRepository.findById(UUID.fromString(PASSAGER_ID)))));
    }

    private EventRegistrationEntity registrationOf(String personId) {
        return registrationRepository.findByEventAndPerson(event, UUID.fromString(personId)).orElseThrow();
    }

    private void piloteAsksForNord() {
        QuarkusTransaction.requiringNew().run(() -> registrationOf(PILOTE_ID).requestGroup(groupRepository.findById(nord)));
    }

    private void piloteRidesWithNord() {
        piloteAsksForNord();
        QuarkusTransaction.requiringNew().run(() -> registrationOf(PILOTE_ID).acceptDemande());
    }

    private ValidatableResponse signUpAsPassagerOf(String piloteId) {
        return given().queryParam("piloteId", piloteId).when().put("/{id}/registration", event).then();
    }

    private ValidatableResponse roster() {
        return given().when().get("/{id}/roster", event).then().statusCode(200);
    }

    private String entryOf(String list, String personId) {
        return list + ".find { it.personId == '" + personId + "' }";
    }

    private boolean signedUp(String personId) {
        return QuarkusTransaction.requiringNew().call(() ->
                registrationRepository.findByEventAndPerson(event, UUID.fromString(personId)).isPresent());
    }

    // --- Signing up as passager ---

    @Test
    @TestSecurity(user = "passager", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PASSAGER_ID))
    void aPassagerSignsUpWithTheirPiloteAndFollowsTheirGroupe() {
        piloteSignedUp(false);
        piloteRidesWithNord();

        signUpAsPassagerOf(PILOTE_ID).statusCode(200)
                .body("mode", equalTo("passager"))
                .body("status", equalTo("participant"))
                .body("pilote.userId", equalTo(PILOTE_ID))
                .body("pilote.lastName", equalTo("Pilote"))
                .body("group.groupId", equalTo(nord.toString()));
    }

    @Test
    @TestSecurity(user = "passager", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PASSAGER_ID))
    void aPassagerNeedsAPiloteSignedUpForTheSameEvent() {
        signUpAsPassagerOf(PILOTE_ID).statusCode(400);

        piloteSignedUp(PILOTE_ID, persistEvent(null), false);
        signUpAsPassagerOf(PILOTE_ID).statusCode(400);
        assertTrue(!signedUp(PASSAGER_ID));
    }

    @Test
    @TestSecurity(user = "passager", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PASSAGER_ID))
    void aPassagerOfAPassagerIsRefused() {
        piloteSignedUp(OTHER_ID, event, false);
        QuarkusTransaction.requiringNew().run(() -> registrationRepository.persist(EventRegistrationEntity.passager(
                registrationOf(OTHER_ID), userRepository.findById(UUID.fromString(PILOTE_ID)))));

        signUpAsPassagerOf(PILOTE_ID).statusCode(400);
    }

    @Test
    @TestSecurity(user = "passager", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PASSAGER_ID))
    void aPassagerCannotAlsoAskForAGroupe() {
        piloteSignedUp(false);
        given().queryParam("piloteId", PILOTE_ID).queryParam("groupId", nord.toString())
                .when().put("/{id}/registration", event).then().statusCode(400);

        signUpAsPassagerOf(PILOTE_ID).statusCode(200);
        given().contentType(ContentType.JSON).body(Map.of("groupId", nord.toString()))
                .when().put("/{id}/registration/demande", event).then().statusCode(400);
    }

    @Test
    @TestSecurity(user = "passager", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PASSAGER_ID))
    void thePassagerOfAWaitingPiloteWaitsToo() {
        piloteSignedUp(true);
        signUpAsPassagerOf(PILOTE_ID).statusCode(200).body("status", equalTo("en_attente"));
    }

    @Test
    @TestSecurity(user = "passager", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PASSAGER_ID))
    void anyBenevoleSeesThePilotesOfAnEvent() {
        piloteSignedUp(false);
        given().when().get("/{id}/pilotes", event).then().statusCode(200)
                .body("userId", contains(PILOTE_ID))
                .body("[0].lastName", equalTo("Pilote"))
                .body("[0].phone", nullValue());
    }

    // --- Maximums ---

    @Test
    @TestSecurity(user = "passager", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PASSAGER_ID))
    void aPassagerCannotJoinAPiloteWhenTheEventIsAtItsMaximum() {
        maximumIs(1);
        piloteSignedUp(false);
        signUpAsPassagerOf(PILOTE_ID).statusCode(400);
    }

    @Test
    @TestSecurity(user = "passager", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PASSAGER_ID))
    void aPassagerCannotJoinAParticipantPiloteWhileTheEventIsComplet() {
        piloteSignedUp(false);
        QuarkusTransaction.requiringNew().run(() -> eventRepository.findById(event).setStatus(EventStatusEnum.COMPLET));
        signUpAsPassagerOf(PILOTE_ID).statusCode(400);
    }

    @Test
    @TestSecurity(user = "passager", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PASSAGER_ID))
    void someoneSignedUpAsPiloteCannotTurnPassager() {
        piloteSignedUp(false);
        piloteSignedUp(PASSAGER_ID, event, false);
        signUpAsPassagerOf(PILOTE_ID).statusCode(400);
    }

    @Test
    @TestSecurity(user = "passager", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PASSAGER_ID))
    void aPassagerCannotJoinAPiloteWhoseGroupeIsAtItsMaximum() {
        piloteSignedUp(false);
        piloteRidesWithNord();
        nordMaximumIs(1);
        signUpAsPassagerOf(PILOTE_ID).statusCode(400);
    }

    @Test
    @TestSecurity(user = "bureau", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = BUREAU_ID))
    void aPassagerCountsTowardsTheEventMaximum() {
        maximumIs(2);
        piloteSignedUp(false);
        passagerSignedUp();
        piloteSignedUp(OTHER_ID, event, true);

        given().when().post("/{e}/roster/{p}/promote", event, OTHER_ID).then().statusCode(400);
    }

    @Test
    @TestSecurity(user = "chef", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = CHEF_ID))
    void aPassagerCountsTowardsTheGroupeMaximum() {
        nordMaximumIs(2);
        piloteSignedUp(false);
        passagerSignedUp();
        piloteAsksForNord();
        piloteSignedUp(OTHER_ID, event, false);
        QuarkusTransaction.requiringNew().run(() -> registrationOf(OTHER_ID).placeIn(groupRepository.findById(nord)));

        // Pilote and passager need two places; only one is left
        given().when().post("/{e}/demandes/{p}/accept", event, PILOTE_ID).then().statusCode(400);
    }

    // --- Following the pilote ---

    @Test
    @TestSecurity(user = "bureau", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = BUREAU_ID))
    void movingThePiloteUpMovesThePassagerUp() {
        maximumIs(2);
        piloteSignedUp(true);
        passagerSignedUp();

        given().when().post("/{e}/roster/{p}/promote", event, PILOTE_ID).then().statusCode(200)
                .body("participants.personId", containsInAnyOrder(PILOTE_ID, PASSAGER_ID));
    }

    @Test
    @TestSecurity(user = "bureau", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = BUREAU_ID))
    void thePiloteIsNotMovedUpWithoutRoomForTheirPassagers() {
        maximumIs(1);
        piloteSignedUp(true);
        passagerSignedUp();

        given().when().post("/{e}/roster/{p}/promote", event, PILOTE_ID).then().statusCode(400);
    }

    @Test
    @TestSecurity(user = "chef", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = CHEF_ID))
    void acceptingThePilotesDemandeTakesThePassagerIntoTheGroupe() {
        piloteSignedUp(false);
        passagerSignedUp();
        piloteAsksForNord();

        given().when().post("/{e}/demandes/{p}/accept", event, PILOTE_ID).then().statusCode(200);
        given().when().get("/mon-groupe").then().statusCode(200)
                .body("events.find { it.eventId == '" + event + "' }.members.personId", containsInAnyOrder(PILOTE_ID, PASSAGER_ID));
    }

    @Test
    @TestSecurity(user = "chef", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = CHEF_ID))
    void refusingThePilotesDemandeLeavesThePassagerWithoutAGroupe() {
        piloteSignedUp(false);
        passagerSignedUp();
        piloteAsksForNord();

        given().when().post("/{e}/demandes/{p}/refuse", event, PILOTE_ID).then().statusCode(200);
        assertTrue(QuarkusTransaction.requiringNew().call(() -> registrationOf(PASSAGER_ID).getGroup() == null));
    }

    @Test
    @TestSecurity(user = "bureau", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = BUREAU_ID))
    void placingThePiloteInAGroupePlacesThePassager() {
        piloteSignedUp(false);
        passagerSignedUp();

        given().when().put("/{e}/roster/{p}/group/{g}", event, PILOTE_ID, nord).then().statusCode(200)
                .body(entryOf("participants", PASSAGER_ID) + ".group.groupId", equalTo(nord.toString()));
    }

    @Test
    @TestSecurity(user = "chef", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = CHEF_ID))
    void takingThePiloteOutOfTheGroupeTakesThePassagerOut() {
        piloteSignedUp(false);
        piloteRidesWithNord();
        passagerSignedUp();

        given().when().delete("/{e}/roster/{p}/group", event, PILOTE_ID).then().statusCode(200);
        given().when().get("/mon-groupe").then().statusCode(200)
                .body("events.find { it.eventId == '" + event + "' }.members", org.hamcrest.Matchers.empty());
    }

    @Test
    @TestSecurity(user = "bureau", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = BUREAU_ID))
    void aPassagerIsNotMovedWithoutTheirPilote() {
        maximumIs(5);
        piloteSignedUp(true);
        passagerSignedUp();
        given().when().post("/{e}/roster/{p}/promote", event, PASSAGER_ID).then().statusCode(400);

        QuarkusTransaction.requiringNew().run(() -> registrationOf(PILOTE_ID).moveUp());
        given().when().put("/{e}/roster/{p}/group/{g}", event, PASSAGER_ID, nord).then().statusCode(400);
    }

    @Test
    @TestSecurity(user = "bureau", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = BUREAU_ID))
    void removingThePiloteRemovesThePassager() {
        piloteSignedUp(false);
        passagerSignedUp();

        given().when().delete("/{e}/roster/{p}", event, PILOTE_ID).then().statusCode(200)
                .body("participants", org.hamcrest.Matchers.empty());
    }

    @Test
    @TestSecurity(user = "pilote", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PILOTE_ID))
    void thePilotesWithdrawalWithdrawsThePassager() {
        piloteSignedUp(false);
        passagerSignedUp();

        given().when().delete("/{id}/registration", event).then().statusCode(204);
        assertTrue(!signedUp(PASSAGER_ID));
    }

    @Test
    @TestSecurity(user = "passager", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = PASSAGER_ID))
    void aPassagerWithdrawsAloneAndThePiloteStays() {
        piloteSignedUp(false);
        passagerSignedUp();

        given().when().delete("/{id}/registration", event).then().statusCode(204);
        assertTrue(signedUp(PILOTE_ID));
        assertTrue(!signedUp(PASSAGER_ID));
    }

    @Test
    @TestSecurity(user = "bureau", augmentors = DatabaseRoleAugmentor.class)
    @OidcSecurity(claims = @Claim(key = "sub", value = BUREAU_ID))
    void theRosterNamesEachPassagersPilote() {
        piloteSignedUp(false);
        passagerSignedUp();
        roster().body(entryOf("participants", PASSAGER_ID) + ".mode", equalTo("passager"))
                .body(entryOf("participants", PILOTE_ID) + ".passagers", equalTo(1))
                .body(entryOf("participants", PASSAGER_ID) + ".pilote.userId", equalTo(PILOTE_ID))
                .body(entryOf("participants", PILOTE_ID) + ".pilote", nullValue());
    }
}
