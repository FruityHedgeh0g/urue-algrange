import { afterEach, describe, expect, it } from "vitest";
import { render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "../../auth/AuthContext";
import GroupsAdminPage from "./GroupsAdminPage";

const renderPage = () => {
  localStorage.setItem("urue-mock-role", "bureau");
  render(
    <QueryClientProvider client={new QueryClient()}>
      <AuthProvider>
        <GroupsAdminPage />
      </AuthProvider>
    </QueryClientProvider>
  );
};

const chefOptions = () =>
  within(screen.getByLabelText("Chef de groupe")).getAllByRole("option").map((o) => o.textContent);

describe("GroupsAdminPage", () => {
  afterEach(() => {
    localStorage.clear();
  });

  it("lists each Groupe with its area and Chef", async () => {
    renderPage();
    const algrange = await screen.findByRole("button", { name: /Groupe Algrange Centre/ });
    expect(algrange).toHaveTextContent("Centre-ville d'Algrange");
    expect(algrange).toHaveTextContent("Chef : Marc Weber");
  });

  it("offers as Chef only people with Role at least chef_de_groupe", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Groupe Algrange Centre/ }));
    expect(chefOptions()).toEqual([
      "Aucun chef",
      "Marc Weber",
      "Nathalie Roth (mène Groupe Thionville)",
      "Claire Hoffmann",
      "Luc Schmitt",
    ]);
  });

  it("moves a Chef who already leads another Groupe", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Groupe Algrange Centre/ }));
    await userEvent.selectOptions(screen.getByLabelText("Chef de groupe"), "user-6");
    await userEvent.click(screen.getByRole("button", { name: "Enregistrer" }));

    await waitFor(() =>
      expect(screen.getByRole("button", { name: /Groupe Algrange Centre/ })).toHaveTextContent("Chef : Nathalie Roth")
    );
    expect(screen.getByRole("button", { name: /Groupe Thionville/ })).toHaveTextContent("Sans chef");
  });

  it("clears a Groupe's Chef", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Groupe Algrange Centre/ }));
    await userEvent.selectOptions(screen.getByLabelText("Chef de groupe"), "");
    await userEvent.click(screen.getByRole("button", { name: "Enregistrer" }));

    await waitFor(() =>
      expect(screen.getByRole("button", { name: /Groupe Algrange Centre/ })).toHaveTextContent("Sans chef")
    );
  });

  it("creates a Groupe with its area", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: "+ Nouveau groupe" }));
    await userEvent.type(screen.getByLabelText(/Nom du groupe/), "Groupe Nord");
    await userEvent.type(screen.getByLabelText("Zone couverte"), "Nord du secteur");
    await userEvent.click(screen.getByRole("button", { name: "Créer le groupe" }));

    expect(await screen.findByRole("button", { name: /Groupe Nord/ })).toHaveTextContent("Nord du secteur");
  });
});
