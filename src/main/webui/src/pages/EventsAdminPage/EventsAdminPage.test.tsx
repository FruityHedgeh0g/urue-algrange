import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "../../auth/AuthContext";
import { createEventsApi } from "../../features/events/eventsApi";
import EventsAdminPage from "./EventsAdminPage";

const inDays = (days: number, hour: number) => {
  const d = new Date();
  d.setDate(d.getDate() + days);
  d.setHours(hour, 0, 0, 0);
  return d.toISOString().slice(0, 19);
};

const seed = async () => {
  const api = createEventsApi();
  const base = { description: "", sectorId: "sector-1" };
  await api.createEvent({ ...base, name: "Test Préparation", startDateTime: inDays(30, 9), endDateTime: inDays(30, 18) });
  const open = await api.createEvent({ ...base, name: "Test Ouvert", startDateTime: inDays(40, 9), endDateTime: inDays(40, 18) });
  await api.changeStatus(open.eventId, "ouvert");
};

const renderPage = () => {
  localStorage.setItem("urue-mock-role", "bureau");
  render(
    <QueryClientProvider client={new QueryClient()}>
      <AuthProvider>
        <EventsAdminPage />
      </AuthProvider>
    </QueryClientProvider>
  );
};

const statusOptions = () => within(screen.getByLabelText("Statut")).getAllByRole("option").map((o) => o.textContent);

describe("EventsAdminPage", () => {
  beforeEach(seed);
  afterEach(() => localStorage.clear());

  it("shows each Event's status", async () => {
    renderPage();
    expect(await screen.findByRole("button", { name: /Test Préparation/ })).toHaveTextContent("Planification");
    expect(screen.getByRole("button", { name: /Test Ouvert/ })).toHaveTextContent("Ouvert");
  });

  it("offers only the allowed transitions", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Test Préparation/ }));
    expect(statusOptions()).toEqual(["Planification", "Ouvert", "Annulé"]);
    await userEvent.click(screen.getByRole("button", { name: /Test Préparation/ }));

    await userEvent.click(screen.getByRole("button", { name: /Test Ouvert/ }));
    expect(statusOptions()).toEqual(["Ouvert", "Complet", "Annulé"]);
  });

  it("opens an Event in Planification", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Test Préparation/ }));
    await userEvent.selectOptions(screen.getByLabelText("Statut"), "ouvert");
    await userEvent.click(screen.getByRole("button", { name: "Enregistrer" }));
    await waitFor(() => expect(screen.getByRole("button", { name: /Test Préparation/ })).toHaveTextContent("Ouvert"));
  });

  it("sets and clears the maximum number of Participants", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Test Ouvert/ }));
    await userEvent.type(screen.getByLabelText("Participants maximum"), "40");
    await userEvent.click(screen.getByRole("button", { name: "Enregistrer" }));
    await waitFor(() => expect(screen.getByRole("button", { name: /Test Ouvert/ })).toHaveTextContent("40 places"));

    await userEvent.click(screen.getByRole("button", { name: /Test Ouvert/ }));
    await userEvent.clear(screen.getByLabelText("Participants maximum"));
    await userEvent.click(screen.getByRole("button", { name: "Enregistrer" }));
    await waitFor(() => expect(screen.getByRole("button", { name: /Test Ouvert/ })).not.toHaveTextContent("places"));
  });

  it("offers no delete: Events are cancelled instead", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Test Ouvert/ }));
    expect(screen.queryByRole("button", { name: "Supprimer" })).not.toBeInTheDocument();
  });

  it("creates an Event in Planification", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: "+ Nouvel événement" }));
    await userEvent.type(screen.getByLabelText("Nom"), "Test Nouveau");
    await userEvent.type(screen.getByLabelText("Début"), inDays(50, 9).slice(0, 16));
    await userEvent.type(screen.getByLabelText("Fin"), inDays(50, 18).slice(0, 16));
    await userEvent.click(screen.getByRole("button", { name: "Créer l'événement" }));
    expect(await screen.findByRole("button", { name: /Test Nouveau/ })).toHaveTextContent("Planification");
  });
});
