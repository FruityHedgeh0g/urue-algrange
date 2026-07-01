import { afterEach, describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./AuthContext";
import RequireRole from "./RequireRole";

const renderProtected = (initialPath = "/protected") =>
  render(
    <MemoryRouter initialEntries={[initialPath]}>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<p>Accueil</p>} />
          <Route
            path="/protected"
            element={
              <RequireRole minRole="membre">
                <p>Contenu protégé</p>
              </RequireRole>
            }
          />
        </Routes>
      </AuthProvider>
    </MemoryRouter>
  );

describe("RequireRole", () => {
  afterEach(() => {
    localStorage.clear();
  });

  it("redirects to the home page when the role is insufficient", () => {
    localStorage.setItem("urue-mock-role", "visiteur");
    renderProtected();
    expect(screen.getByText("Accueil")).toBeInTheDocument();
    expect(screen.queryByText("Contenu protégé")).not.toBeInTheDocument();
  });

  it("renders the children when the role is sufficient", () => {
    localStorage.setItem("urue-mock-role", "membre");
    renderProtected();
    expect(screen.getByText("Contenu protégé")).toBeInTheDocument();
  });
});
