import { afterEach, describe, expect, it } from "vitest";
import { render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { AuthProvider } from "../../auth/AuthContext";
import { testUser } from "../../test/testUser";
import MembersAdminPage from "./MembersAdminPage";

const renderAs = (role: string) => {
  const user = testUser(role);
  render(
    <QueryClientProvider client={new QueryClient()}>
      <AuthProvider user={user}>
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

  it("gives nobody a permanent Groupe: who rides with a Groupe is decided per Event", async () => {
    renderAs("bureau");
    const sophie = await screen.findByRole("button", { name: /Sophie Kremer/ });
    expect(sophie).not.toHaveTextContent("Groupe Algrange Centre");
    await userEvent.click(sophie);
    expect(screen.queryByLabelText("Groupe")).not.toBeInTheDocument();
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

  it("shows the Secteur of a new Membre, given by the promoting Bureau member", async () => {
    renderAs("bureau");
    await userEvent.click(await screen.findByRole("button", { name: /Sophie Kremer/ }));
    await userEvent.selectOptions(screen.getByLabelText("Rôle"), "membre");
    await userEvent.click(screen.getByRole("button", { name: "Enregistrer" }));
    await waitFor(() => expect(screen.getByRole("button", { name: /Sophie Kremer/ })).toHaveTextContent("Secteur Algrange"));
  });

  it("asks the Super admin for the Secteur of a new Admin", async () => {
    renderAs("super_admin");
    await userEvent.click(await screen.findByRole("button", { name: /Sophie Kremer/ }));
    expect(screen.queryByLabelText("Secteur")).not.toBeInTheDocument();
    await userEvent.selectOptions(screen.getByLabelText("Rôle"), "admin");
    await userEvent.selectOptions(screen.getByLabelText("Secteur"), "sector-2");
    await userEvent.click(screen.getByRole("button", { name: "Enregistrer" }));
    await waitFor(() => expect(screen.getByRole("button", { name: /Sophie Kremer/ })).toHaveTextContent("Secteur Thionville"));
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

  it("lets only an Admin correct someone's names", async () => {
    renderAs("bureau");
    await userEvent.click(await screen.findByRole("button", { name: /Sophie Kremer/ }));
    expect(screen.getByLabelText("Prénom")).toBeDisabled();
  });

  it("lets an Admin correct someone's names", async () => {
    renderAs("admin");
    await userEvent.click(await screen.findByRole("button", { name: /Sophie Kremer/ }));
    await userEvent.clear(screen.getByLabelText("Prénom"));
    await userEvent.type(screen.getByLabelText("Prénom"), "Sophia");
    await userEvent.click(screen.getByRole("button", { name: "Enregistrer" }));
    expect(await screen.findByRole("button", { name: /Sophia Kremer/ })).toBeInTheDocument();
  });
});
