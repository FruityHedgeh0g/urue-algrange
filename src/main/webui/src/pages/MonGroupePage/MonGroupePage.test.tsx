import { afterEach, describe, expect, it } from "vitest";
import { render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "../../auth/AuthContext";
import { createEventsApi } from "../../features/events/eventsApi";
import { createRegistrationsApi } from "../../features/events/registrationsApi";
import { createGroupsApi } from "../../features/groups/groupsApi";
import MonGroupePage from "./MonGroupePage";

const inDays = (days: number) => new Date(Date.now() + days * 86_400_000).toISOString().slice(0, 19);

const renderPage = () => {
  localStorage.setItem("urue-mock-role", "chef_de_groupe");
  render(
    <QueryClientProvider client={new QueryClient()}>
      <AuthProvider>
        <MonGroupePage />
      </AuthProvider>
    </QueryClientProvider>
  );
};

/** Donne au Chef mocké ("mock-user") un Groupe, et inscrit Sophie avec une Demande pour ce Groupe. */
const seedLedGroupe = async () => {
  const groups = createGroupsApi();
  await groups.createGroup({ name: "Test Nord", description: "", area: "", sectorId: "sector-1" });
  const nord = (await groups.fetchGroups()).find((g) => g.name === "Test Nord")!.groupId;
  await groups.setChef(nord, { userId: "mock-user", firstName: "Jean", lastName: "Dupont" });

  const events = createEventsApi();
  const event = await events.createEvent({ name: "Test Balade", description: "", sectorId: "sector-1", startDateTime: inDays(10), endDateTime: inDays(11) });
  await events.changeStatus(event.eventId, "ouvert");
  await createRegistrationsApi().signUp(event.eventId, { userId: "p-1", firstName: "Sophie", lastName: "Kremer", phone: "06 00 00 00 00" }, nord);
};

describe("MonGroupePage", () => {
  afterEach(() => localStorage.clear());

  it("says so when the Chef leads no Groupe", async () => {
    renderPage();
    expect(await screen.findByText("Vous ne menez aucun groupe pour le moment.")).toBeInTheDocument();
  });

  it("shows pending Demandes per Event and accepts one into the Groupe", async () => {
    await seedLedGroupe();
    renderPage();

    expect(await screen.findByRole("heading", { name: /Test Nord/ })).toBeInTheDocument();
    const balade = await screen.findByRole("region", { name: "Test Balade" });
    await userEvent.click(within(within(balade).getByRole("list", { name: "Demandes en attente" })).getByRole("button", { name: "Accepter" }));

    await waitFor(() => expect(within(within(balade).getByRole("list", { name: "Membres du groupe" })).getByText(/Kremer/)).toBeInTheDocument());
    expect(within(balade).queryByRole("list", { name: "Demandes en attente" })).not.toBeInTheDocument();
  });

  it("takes a member out of the Groupe", async () => {
    await seedLedGroupe();
    renderPage();
    const balade = await screen.findByRole("region", { name: "Test Balade" });
    await userEvent.click(within(within(balade).getByRole("list", { name: "Demandes en attente" })).getByRole("button", { name: "Accepter" }));

    const members = await within(balade).findByRole("list", { name: "Membres du groupe" });
    await userEvent.click(within(members).getByRole("button", { name: "Sortir du groupe" }));
    await waitFor(() => expect(within(balade).getByText("Aucun membre pour cet événement.")).toBeInTheDocument());
  });
});
