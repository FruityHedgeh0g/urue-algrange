import { afterEach, beforeEach, describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { AuthProvider } from "../../auth/AuthContext";
import { createEventsApi } from "../../features/events/eventsApi";
import { createRegistrationsApi } from "../../features/events/registrationsApi";
import EventDetailPage from "./EventDetailPage";

const inDays = (days: number) => new Date(Date.now() + days * 86_400_000).toISOString().slice(0, 19);

let eventId: string;

const renderPage = () =>
  render(
    <QueryClientProvider client={new QueryClient()}>
      <MemoryRouter initialEntries={[`/evenements/${eventId}`]}>
        <AuthProvider>
          <Routes>
            <Route path="/evenements/:eventId" element={<EventDetailPage />} />
          </Routes>
        </AuthProvider>
      </MemoryRouter>
    </QueryClientProvider>
  );

describe("EventDetailPage sign-up", () => {
  beforeEach(async () => {
    const events = createEventsApi();
    const event = await events.createEvent({
      name: "Test Balade",
      description: "",
      sectorId: "sector-1",
      startDateTime: inDays(10),
      endDateTime: inDays(11),
    });
    await events.changeStatus(event.eventId, "ouvert");
    eventId = event.eventId;
    localStorage.setItem("urue-mock-role", "benevole");
  });

  afterEach(() => localStorage.clear());

  it("asks for the phone number, saves it and signs up", async () => {
    localStorage.setItem("urue-mock-profile", JSON.stringify({ firstName: "Jean", lastName: "Dupont", phone: "" }));
    renderPage();

    await userEvent.click(await screen.findByRole("button", { name: "M'inscrire comme pilote" }));
    await userEvent.type(await screen.findByLabelText("Téléphone"), "06 12 34 56 78");
    await userEvent.click(screen.getByRole("button", { name: "Enregistrer et m'inscrire" }));

    expect(await screen.findByText("Vous êtes inscrit comme pilote")).toBeInTheDocument();
    expect(JSON.parse(localStorage.getItem("urue-mock-profile") as string).phone).toBe("06 12 34 56 78");
  });

  it("signs up with a chosen Groupe and shows the pending Demande", async () => {
    localStorage.setItem("urue-mock-profile", JSON.stringify({ firstName: "Jean", lastName: "Dupont", phone: "06 12 34 56 78" }));
    renderPage();

    await userEvent.selectOptions(await screen.findByLabelText("Groupe (facultatif)"), "group-1");
    await userEvent.click(screen.getByRole("button", { name: "M'inscrire comme pilote" }));

    expect(await screen.findByText("Demande pour Groupe Algrange Centre : en attente")).toBeInTheDocument();
  });

  it("signs up as passager of a chosen pilote", async () => {
    await createRegistrationsApi().signUp(eventId, { userId: "p-1", firstName: "Marc", lastName: "Weber", phone: "06 00 00 00 00" });
    localStorage.setItem("urue-mock-profile", JSON.stringify({ firstName: "Jean", lastName: "Dupont", phone: "06 12 34 56 78" }));
    renderPage();

    await userEvent.selectOptions(await screen.findByLabelText("Je roule comme"), "passager");
    expect(screen.queryByLabelText("Groupe (facultatif)")).not.toBeInTheDocument();
    await userEvent.selectOptions(await screen.findByLabelText("Pilote"), "p-1");
    await userEvent.click(screen.getByRole("button", { name: "M'inscrire comme passager" }));

    expect(await screen.findByText("Vous êtes inscrit comme passager de Marc Weber")).toBeInTheDocument();
    expect(screen.queryByRole("button", { name: "Demander ce groupe" })).not.toBeInTheDocument();
  });

  it("reserves another Secteur's Event to its Membres and to Bénévoles", async () => {
    const events = createEventsApi();
    const thionville = await events.createEvent({ name: "Test Thionville", description: "", sectorId: "sector-2", startDateTime: inDays(10), endDateTime: inDays(11) });
    await events.changeStatus(thionville.eventId, "ouvert");
    eventId = thionville.eventId;
    localStorage.setItem("urue-mock-role", "membre");
    renderPage();

    expect(await screen.findByText(/Réservé aux membres de Secteur Thionville et aux bénévoles/)).toBeInTheDocument();
    expect(screen.queryByRole("button", { name: /M'inscrire/ })).not.toBeInTheDocument();
  });

  it("signs up straight away with a phone number, then withdraws", async () => {
    localStorage.setItem("urue-mock-profile", JSON.stringify({ firstName: "Jean", lastName: "Dupont", phone: "06 12 34 56 78" }));
    renderPage();

    await userEvent.click(await screen.findByRole("button", { name: "M'inscrire comme pilote" }));
    expect(await screen.findByText("Vous êtes inscrit comme pilote")).toBeInTheDocument();

    await userEvent.click(screen.getByRole("button", { name: "Me désinscrire" }));
    expect(await screen.findByRole("button", { name: "M'inscrire comme pilote" })).toBeInTheDocument();
  });
});
