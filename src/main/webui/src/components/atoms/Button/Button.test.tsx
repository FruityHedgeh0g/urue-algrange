import { describe, expect, it, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import Button from "./Button";

describe("Button", () => {
  it("renders its label", () => {
    render(<Button label="Envoyer" />);
    expect(screen.getByRole("button", { name: "Envoyer" })).toBeInTheDocument();
  });

  it("calls onClick when clicked", async () => {
    const onClick = vi.fn();
    render(<Button label="Envoyer" onClick={onClick} />);
    await userEvent.click(screen.getByRole("button", { name: "Envoyer" }));
    expect(onClick).toHaveBeenCalledOnce();
  });

  it("does not call onClick when disabled", async () => {
    const onClick = vi.fn();
    render(<Button label="Envoyer" onClick={onClick} disabled />);
    await userEvent.click(screen.getByRole("button", { name: "Envoyer" }));
    expect(onClick).not.toHaveBeenCalled();
  });
});
