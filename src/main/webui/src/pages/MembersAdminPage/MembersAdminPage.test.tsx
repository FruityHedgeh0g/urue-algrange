import { afterEach, describe, expect, it } from "vitest";
import { render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "../../auth/AuthContext";
import MembersAdminPage from "./MembersAdminPage";

const renderAs = (role: string) => {
  localStorage.setItem("urue-mock-role", role);
  render(
    <QueryClientProvider client={new QueryClient()}>
      <AuthProvider>
        <MembersAdminPage />
      </AuthProvider>
    </QueryClientProvider>
  );
};

const roleOptions = () =>
  within(screen.getByLabelText("Rôle")).getAllByRole("option").map((o) => o.textContent);

describe("MembersAdminPage", () => {
  afterEach(() => {
    localStorage.clear();
  });

  it("offers the Bureau only membre and chef_de_groupe", async () => {
    renderAs("bureau");
    await userEvent.click(await screen.findByRole("button", { name: /Sophie Kremer/ }));
    expect(roleOptions()).toEqual(["Bénévole", "Membre", "Chef de groupe"]);
  });

  it("offers an Admin the bureau Role too", async () => {
    renderAs("admin");
    await userEvent.click(await screen.findByRole("button", { name: /Sophie Kremer/ }));
    expect(roleOptions()).toEqual(["Bénévole", "Membre", "Chef de groupe", "Bureau"]);
  });

  it("offers no promotion on a person at the viewer's level", async () => {
    renderAs("bureau");
    await userEvent.click(await screen.findByRole("button", { name: /Claire Hoffmann/ }));
    expect(screen.queryByLabelText("Rôle")).not.toBeInTheDocument();
  });

  it("promotes a Bénévole to Membre", async () => {
    renderAs("bureau");
    await userEvent.click(await screen.findByRole("button", { name: /Sophie Kremer/ }));
    await userEvent.selectOptions(screen.getByLabelText("Rôle"), "membre");
    await userEvent.click(screen.getByRole("button", { name: "Enregistrer" }));
    await waitFor(() => expect(screen.getByRole("button", { name: /Sophie Kremer/ })).toHaveTextContent("Membre"));
  });

  it("shows the Président", async () => {
    renderAs("bureau");
    expect(await screen.findByRole("button", { name: /Claire Hoffmann/ })).toHaveTextContent("Président");
    expect(screen.getByRole("button", { name: /Luc Schmitt/ })).not.toHaveTextContent("Président");
  });

  it("lets an Admin hand the Président flag to another Bureau member", async () => {
    renderAs("admin");
    await userEvent.click(await screen.findByRole("button", { name: /Luc Schmitt/ }));
    await userEvent.click(screen.getByLabelText("Président"));
    await userEvent.click(screen.getByRole("button", { name: "Enregistrer" }));
    await waitFor(() => expect(screen.getByRole("button", { name: /Luc Schmitt/ })).toHaveTextContent("Président"));
    expect(screen.getByRole("button", { name: /Claire Hoffmann/ })).not.toHaveTextContent("Président");
  });

  it("offers the Président flag only to an Admin, only on Bureau members", async () => {
    renderAs("bureau");
    await userEvent.click(await screen.findByRole("button", { name: /Luc Schmitt/ }));
    expect(screen.queryByLabelText("Président")).not.toBeInTheDocument();
  });
});
