import { describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import FormField from "./FormField";

describe("FormField", () => {
  it("associates the label with its input for accessibility", () => {
    render(<FormField label="E-mail" value="" onChange={() => {}} />);
    expect(screen.getByLabelText("E-mail")).toBeInTheDocument();
  });

  it("renders a textarea when multiline is set", () => {
    render(<FormField label="Message" multiline value="" onChange={() => {}} />);
    expect(screen.getByLabelText("Message").tagName).toBe("TEXTAREA");
  });

  it("displays the error message and marks the field as invalid", () => {
    render(<FormField label="E-mail" value="" onChange={() => {}} error="Adresse invalide" />);
    expect(screen.getByText("Adresse invalide")).toBeInTheDocument();
    expect(screen.getByLabelText("E-mail")).toHaveAttribute("aria-invalid", "true");
  });
});
