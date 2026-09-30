import { afterEach, describe, expect, it } from "vitest";
import { render, screen, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "../../auth/AuthContext";
import SectorPage from "./SectorPage";

const renderPage = () => {
  localStorage.setItem("urue-mock-role", "bureau");
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

    expect(await screen.findByLabelText("Nom du secteur")).toHaveValue("Secteur Algrange");
    const groupes = await screen.findByRole("list", { name: "Groupes du secteur" });
    expect(within(groupes).getByText("Groupe Algrange Centre")).toBeInTheDocument();
    expect(within(groupes).getByText(/Marc Weber/)).toBeInTheDocument();
    expect(screen.queryByText(/Inscrits de mon secteur/)).not.toBeInTheDocument();
  });

  it("switches between Secteurs", async () => {
    renderPage();
    await userEvent.selectOptions(await screen.findByLabelText("Secteur"), "sector-2");

    expect(await screen.findByLabelText("Nom du secteur")).toHaveValue("Secteur Thionville");
    expect(within(screen.getByRole("list", { name: "Groupes du secteur" })).getByText("Groupe Thionville")).toBeInTheDocument();
  });
});
