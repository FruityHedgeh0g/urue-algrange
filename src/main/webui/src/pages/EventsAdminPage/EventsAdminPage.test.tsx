import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "../../auth/AuthContext";
import { createEventsApi } from "../../features/events/eventsApi";
import { createRegistrationsApi } from "../../features/events/registrationsApi";
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
  await api.updateEvent(open.eventId, { ...base, name: "Test Ouvert", startDateTime: inDays(40, 9), endDateTime: inDays(40, 18), maxParticipants: 1 });
  const registrations = createRegistrationsApi();
  for (const [userId, lastName] of [["p-1", "Weber"], ["p-2", "Kremer"]]) {
    await registrations.signUp(open.eventId, { userId, firstName: "Test", lastName, phone: "06 00 00 00 00" });
  }
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

  it("shows the roster: Participants and Liste d'attente in sign-up order", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Test Ouvert/ }));
    const participants = await screen.findByRole("list", { name: "Participants" });
    expect(within(participants).getByText(/Weber/)).toBeInTheDocument();
    const waiting = screen.getByRole("list", { name: "Liste d'attente" });
    expect(within(waiting).getByText(/Kremer/)).toBeInTheDocument();
    expect(within(waiting).getByRole("button", { name: "Faire monter" })).toBeDisabled();
  });

  it("removes a Participant, then moves someone up by hand", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Test Ouvert/ }));
    const participants = await screen.findByRole("list", { name: "Participants" });
    await userEvent.click(within(participants).getByRole("button", { name: "Retirer" }));

    // La place libérée ne profite à personne automatiquement
    await waitFor(() => expect(screen.getByText("Aucun participant.")).toBeInTheDocument());
    const waiting = screen.getByRole("list", { name: "Liste d'attente" });
    await userEvent.click(within(waiting).getByRole("button", { name: "Faire monter" }));

    await waitFor(() =>
      expect(within(screen.getByRole("list", { name: "Participants" })).getByText(/Kremer/)).toBeInTheDocument()
    );
    expect(screen.getByText("Personne en attente.")).toBeInTheDocument();
  });

  it("lets the Bureau place a Participant in a Groupe, then take them out", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Test Ouvert/ }));
    const participants = await screen.findByRole("list", { name: "Participants" });
    await userEvent.selectOptions(within(participants).getByLabelText("Groupe"), "group-1");

    await waitFor(() => expect(within(participants).getByText(/Groupe Algrange Centre/)).toBeInTheDocument());
    await userEvent.click(within(participants).getByRole("button", { name: "Sortir du groupe" }));
    await waitFor(() => expect(within(participants).getByLabelText("Groupe")).toHaveValue(""));
  });

  it("lets the Bureau decide a pending Demande de groupe", async () => {
    const eventId = (await createEventsApi().fetchEvents(true)).find((e) => e.name === "Test Ouvert")!.eventId;
    await createRegistrationsApi().signUp(eventId, { userId: "p-3", firstName: "Test", lastName: "Meyer", phone: "06 00 00 00 00" }, "group-1");
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Test Ouvert/ }));
    const waiting = await screen.findByRole("list", { name: "Liste d'attente" });
    const meyer = within(waiting).getByText(/Meyer/).closest("li") as HTMLElement;
    expect(meyer).toHaveTextContent("Demande : Groupe Algrange Centre");
    await userEvent.click(within(meyer).getByRole("button", { name: "Accepter" }));
    await waitFor(() => expect(within(screen.getByRole("list", { name: "Liste d'attente" })).getByText(/Meyer/).closest("li")).toHaveTextContent("Groupe : Groupe Algrange Centre"));
  });

  it("sets a Groupe's maximum and shows the Groupe's Liste d'attente in sign-up order", async () => {
    const eventId = (await createEventsApi().fetchEvents(true)).find((e) => e.name === "Test Ouvert")!.eventId;
    const registrations = createRegistrationsApi();
    for (const [userId, lastName] of [["p-3", "Meyer"], ["p-4", "Klein"]]) {
      await registrations.signUp(eventId, { userId, firstName: "Test", lastName, phone: "06 00 00 00 00" }, "group-1");
    }
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Test Ouvert/ }));

    const groupes = await screen.findByRole("list", { name: "Groupes" });
    const centre = within(groupes).getByText("Groupe Algrange Centre").closest("li") as HTMLElement;
    await userEvent.type(within(centre).getByLabelText("Maximum"), "1");
    await userEvent.click(within(centre).getByRole("button", { name: "Appliquer" }));

    await waitFor(() => expect(centre).toHaveTextContent("0 / 1"));
    const waiting = within(centre).getByRole("list", { name: "Liste d'attente de Groupe Algrange Centre" });
    expect(within(waiting).getAllByRole("listitem").map((li) => li.textContent)).toEqual([
      expect.stringContaining("Meyer"),
      expect.stringContaining("Klein"),
    ]);
  });

  it("sets and clears the maximum number of Participants", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Test Ouvert/ }));
    await userEvent.clear(screen.getByLabelText("Participants maximum"));
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
