import "@testing-library/jest-dom/vitest";
import { afterEach, beforeEach, vi } from "vitest";
import { resetFakeApi } from "./fakeApi";

beforeEach(() => resetFakeApi());
afterEach(() => vi.unstubAllGlobals());
