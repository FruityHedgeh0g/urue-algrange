import { afterEach, describe, expect, it } from "vitest";
import { render, screen, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "../../auth/AuthContext";
import SectorPage from "./SectorPage";

const renderPage = (role = "bureau") => {
  localStorage.setItem("urue-mock-role", role);
  render(
    <QueryClientProvider client={new QueryClient()}>
      <AuthProvider>
        <SectorPage />
      </AuthProvider>
    </QueryClientProvider>
  );
};

describe("SectorPage (Mon secteur)", () => {
  afterEach(() => localStorage.clear());

  it("shows the Secteur and its Groupes with their Chef, without anyone's permanent Groupe", async () => {
    renderPage();

    expect(await screen.findByRole("heading", { name: "Secteur Algrange" })).toBeInTheDocument();
    // Only the Super admin renames a Secteur; the Bureau keeps its description
    expect(screen.queryByLabelText("Nom du secteur")).not.toBeInTheDocument();
    expect(screen.getByLabelText("Description")).toBeEnabled();
    const groupes = await screen.findByRole("list", { name: "Groupes du secteur" });
    expect(within(groupes).getByText("Groupe Algrange Centre")).toBeInTheDocument();
    expect(within(groupes).getByText(/Marc Weber/)).toBeInTheDocument();
    expect(screen.queryByText(/Inscrits de mon secteur/)).not.toBeInTheDocument();
  });

  it("shows the Bureau its own Secteur only", async () => {
    renderPage();
    expect(await screen.findByRole("heading", { name: "Secteur Algrange" })).toBeInTheDocument();
    expect(screen.queryByLabelText("Secteur")).not.toBeInTheDocument();
  });

  it("lets the Super admin switch between Secteurs", async () => {
    renderPage("super_admin");
    await userEvent.selectOptions(await screen.findByLabelText("Secteur"), "sector-2");

    expect(await screen.findByRole("heading", { name: "Secteur Thionville" })).toBeInTheDocument();
    expect(within(screen.getByRole("list", { name: "Groupes du secteur" })).getByText("Groupe Thionville")).toBeInTheDocument();
  });
});
