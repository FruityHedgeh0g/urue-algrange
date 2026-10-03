import { afterEach, describe, expect, it, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { AuthProvider, useAuth } from "./AuthContext";
import RequireAccess from "./RequireAccess";

const json = (status: number, body: unknown) =>
  new Response(JSON.stringify(body), { status, headers: { "Content-Type": "application/json" } });

const ME = {
  userId: "7c9e6679-7425-40de-944b-e07fc1f90ae7",
  firstName: "Camille",
  lastName: "Martin",
  role: "membre",
  phone: null,
  sector: { sectorId: "s-1", name: "Algrange" },
};

const Who = () => {
  const { user, role, isLoading } = useAuth();
  if (isLoading) return <p>Chargement</p>;
  return <p>{user ? `${user.firstName} ${user.lastName} (${role}, ${user.sector?.name})` : `Visiteur (${role})`}</p>;
};

const renderAt = (path: string) =>
  render(
    <QueryClientProvider client={new QueryClient()}>
      <MemoryRouter initialEntries={[path]}>
        <AuthProvider>
          <Routes>
            <Route path="/" element={<Who />} />
            <Route path="/mon-espace" element={<RequireAccess id="account"><p>Mon espace</p></RequireAccess>} />
          </Routes>
        </AuthProvider>
      </MemoryRouter>
    </QueryClientProvider>
  );

describe("AuthProvider (session)", () => {
  afterEach(() => vi.unstubAllGlobals());

  it("reads the logged-in person from /api/users/me, without being redirected to Keycloak", async () => {
    const fetch = vi.fn().mockResolvedValue(json(200, ME));
    vi.stubGlobal("fetch", fetch);
    renderAt("/");

    expect(await screen.findByText("Camille Martin (membre, Algrange)")).toBeInTheDocument();
    const [url, init] = fetch.mock.calls[0];
    expect(url).toBe("/api/users/me");
    expect(new Headers(init.headers).get("X-Requested-With")).toBe("JavaScript");
  });

  it.each([401, 499])("treats a %i as a Visiteur", async (status) => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(new Response(null, { status })));
    renderAt("/");
    expect(await screen.findByText("Visiteur (visiteur)")).toBeInTheDocument();
  });

  it("treats a site without backend (index.html for every path) as a Visiteur", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(new Response("<!doctype html>", { status: 200, headers: { "Content-Type": "text/html" } })));
    renderAt("/");
    expect(await screen.findByText("Visiteur (visiteur)")).toBeInTheDocument();
  });

  it("opens a protected deep link once the person is known, instead of sending them home", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(json(200, ME)));
    renderAt("/mon-espace");
    expect(await screen.findByText("Mon espace")).toBeInTheDocument();
  });

  it("sends a Visiteur home from a protected deep link", async () => {
    vi.stubGlobal("fetch", vi.fn().mockResolvedValue(new Response(null, { status: 499 })));
    renderAt("/mon-espace");
    expect(await screen.findByText("Visiteur (visiteur)")).toBeInTheDocument();
  });
});
