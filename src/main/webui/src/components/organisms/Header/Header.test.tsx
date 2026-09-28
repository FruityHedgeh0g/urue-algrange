import { afterEach, describe, expect, it } from "vitest";
import { render, screen, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter } from "react-router-dom";
import { ThemeProvider } from "../../../theme/ThemeContext";
import { AuthProvider } from "../../../auth/AuthContext";
import Header from "./Header";

const renderHeader = () =>
  render(
    <QueryClientProvider client={new QueryClient()}>
      <MemoryRouter>
        <ThemeProvider>
          <AuthProvider>
            <Header />
          </AuthProvider>
        </ThemeProvider>
      </MemoryRouter>
    </QueryClientProvider>
  );

describe("Header", () => {
  afterEach(() => {
    localStorage.clear();
  });

  it("opens the mobile drawer with the access-map entries and closes it with Escape", async () => {
    renderHeader();
    await userEvent.click(screen.getByRole("button", { name: "Ouvrir le menu" }));

    const drawer = document.getElementById("mobile-nav") as HTMLElement;
    expect(within(drawer).getByRole("link", { name: "Qui sommes-nous ?" })).toBeInTheDocument();
    expect(within(drawer).queryByRole("link", { name: "Administration" })).not.toBeInTheDocument();

    await userEvent.keyboard("{Escape}");
    expect(document.getElementById("mobile-nav")).toBeNull();
  });

  it("shows the administration entry and a logout button to a bureau member", async () => {
    localStorage.setItem("urue-mock-role", "bureau");
    renderHeader();
    expect(screen.getAllByRole("link", { name: "Administration" }).length).toBeGreaterThan(0);
    expect(screen.getByRole("button", { name: "Se déconnecter" })).toBeInTheDocument();
  });
});
