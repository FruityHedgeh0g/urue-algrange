import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter } from "react-router-dom";
import { AuthProvider } from "../../auth/AuthContext";
import { createEventsApi } from "../../features/events/eventsApi";
import { createRegistrationsApi } from "../../features/events/registrationsApi";
import MyEventsPage from "./MyEventsPage";
import { testUser } from "../../test/testUser";

/** La personne connectée (test/testUser). */
const ME = { userId: "mock-user", firstName: "Jean", lastName: "Dupont", phone: "06 12 34 56 78" };

const inDays = (days: number) => new Date(Date.now() + days * 86_400_000).toISOString().slice(0, 19);

describe("MyEventsPage", () => {
  beforeEach(async () => {
    const events = createEventsApi();
    const registrations = createRegistrationsApi();
    const base = { description: "", sectorId: "sector-1", startDateTime: inDays(10), endDateTime: inDays(11) };

    const open = await events.createEvent({ ...base, name: "Test Balade" });
    await events.changeStatus(open.eventId, "ouvert");
    await registrations.signUp(open.eventId, ME, "group-1");

    const full = await events.createEvent({ ...base, name: "Test Loto" });
    await events.changeStatus(full.eventId, "ouvert");
    await events.changeStatus(full.eventId, "complet");
    await registrations.signUp(full.eventId, ME);
  });

  afterEach(() => localStorage.clear());

  it("shows Participant or en attente per Event", async () => {
    render(
      <QueryClientProvider client={new QueryClient()}>
        <MemoryRouter>
          <AuthProvider user={testUser("benevole")}>
            <MyEventsPage />
          </AuthProvider>
        </MemoryRouter>
      </QueryClientProvider>
    );

    expect(await screen.findByRole("link", { name: /Test Balade/ })).toHaveTextContent("Participant");
    expect(screen.getByRole("link", { name: /Test Balade/ })).toHaveTextContent("Demande pour Groupe Algrange Centre : en attente");
    expect(screen.getByRole("link", { name: /Test Loto/ })).toHaveTextContent("En attente");
  });
});
