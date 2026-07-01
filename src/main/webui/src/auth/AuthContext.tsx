import React, { createContext, useContext, useMemo, useState } from "react";
import { RoleId, roleAtLeast } from "./roles";

/**
 * Authentification mockée : tant que le backend n'expose pas de flux de
 * connexion, le rôle courant est piloté localement (voir RoleSwitcher) pour
 * permettre de prévisualiser chaque espace pendant le développement.
 */
export interface MockUser {
  userId: string;
  firstName: string;
  lastName: string;
  role: RoleId;
}

interface AuthContextValue {
  user: MockUser | null;
  role: RoleId;
  isAuthenticated: boolean;
  setRole: (role: RoleId) => void;
  hasAtLeastRole: (required: RoleId) => boolean;
}

const AUTH_STORAGE_KEY = "urue-mock-role";

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

function buildMockUser(role: RoleId): MockUser | null {
  if (role === "visiteur") return null;
  return { userId: "mock-user", firstName: "Jean", lastName: "Dupont", role };
}

function readStoredRole(): RoleId {
  try {
    const saved = localStorage.getItem(AUTH_STORAGE_KEY);
    return (saved as RoleId) ?? "visiteur";
  } catch {
    return "visiteur";
  }
}

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [role, setRoleState] = useState<RoleId>(() => readStoredRole());

  const setRole = (next: RoleId) => {
    setRoleState(next);
    try {
      localStorage.setItem(AUTH_STORAGE_KEY, next);
    } catch {
      // stockage indisponible : le rôle reste actif pour la session
    }
  };

  const value = useMemo<AuthContextValue>(() => {
    const user = buildMockUser(role);
    return {
      user,
      role,
      isAuthenticated: user !== null,
      setRole,
      hasAtLeastRole: (required) => roleAtLeast(role, required),
    };
  }, [role]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth doit être utilisé à l'intérieur d'un AuthProvider");
  return ctx;
}
