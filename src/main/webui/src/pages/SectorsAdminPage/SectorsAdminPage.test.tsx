import { afterEach, describe, expect, it } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "../../auth/AuthContext";
import SectorsAdminPage from "./SectorsAdminPage";

const renderPage = () => {
  localStorage.setItem("urue-mock-role", "super_admin");
  render(
    <QueryClientProvider client={new QueryClient()}>
      <AuthProvider>
        <SectorsAdminPage />
      </AuthProvider>
    </QueryClientProvider>
  );
};

describe("SectorsAdminPage (Super admin)", () => {
  afterEach(() => localStorage.clear());

  it("closes a Secteur instead of deleting it, then reopens it", async () => {
    renderPage();
    await userEvent.click(await screen.findByRole("button", { name: /Secteur Thionville/ }));
    expect(screen.queryByRole("button", { name: "Supprimer" })).not.toBeInTheDocument();

    await userEvent.click(screen.getByRole("button", { name: "Fermer le secteur" }));
    await waitFor(() => expect(screen.getByRole("button", { name: /Secteur Thionville/ })).toHaveTextContent("Fermé"));

    await userEvent.click(await screen.findByRole("button", { name: "Rouvrir le secteur" }));
    await waitFor(() => expect(screen.getByRole("button", { name: /Secteur Thionville/ })).not.toHaveTextContent("Fermé"));
  });
});
