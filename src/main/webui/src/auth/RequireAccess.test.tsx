import { afterEach, describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./AuthContext";
import RequireAccess from "./RequireAccess";
import { AccessId } from "./access";

const renderProtected = (id: AccessId) =>
  render(
    <QueryClientProvider client={new QueryClient()}>
      <MemoryRouter initialEntries={["/protected"]}>
        <AuthProvider>
          <Routes>
            <Route path="/" element={<p>Accueil</p>} />
            <Route
              path="/protected"
              element={
                <RequireAccess id={id}>
                  <p>Contenu protégé</p>
                </RequireAccess>
              }
            />
          </Routes>
        </AuthProvider>
      </MemoryRouter>
    </QueryClientProvider>
  );

describe("RequireAccess", () => {
  afterEach(() => {
    localStorage.clear();
  });

  it("redirects to the home page when the role is insufficient", () => {
    localStorage.setItem("urue-mock-role", "visiteur");
    renderProtected("account");
    expect(screen.getByText("Accueil")).toBeInTheDocument();
    expect(screen.queryByText("Contenu protégé")).not.toBeInTheDocument();
  });

  it("renders the children when the role is sufficient", () => {
    localStorage.setItem("urue-mock-role", "benevole");
    renderProtected("account");
    expect(screen.getByText("Contenu protégé")).toBeInTheDocument();
  });

  it("treats an unknown stored role as a visitor", () => {
    localStorage.setItem("urue-mock-role", "super-admin");
    renderProtected("account");
    expect(screen.getByText("Accueil")).toBeInTheDocument();
  });

  it("redirects once the entry's feature is known to be inactive", async () => {
    localStorage.setItem("urue-feature-flag-overrides", JSON.stringify({ "galerie-photos": false }));
    renderProtected("gallery");
    expect(await screen.findByText("Accueil")).toBeInTheDocument();
  });

  it("renders a feature-gated entry once its feature is known to be active", async () => {
    renderProtected("gallery");
    expect(await screen.findByText("Contenu protégé")).toBeInTheDocument();
  });
});
