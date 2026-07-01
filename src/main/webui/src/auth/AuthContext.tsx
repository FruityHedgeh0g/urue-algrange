import React, { createContext, useContext, useMemo, useState } from "react";
import { RoleId, roleAtLeast } from "./roles";

/**
 * Authentification mockée : tant que le backend n'expose pas de flux de
 * connexion, le rôle courant est piloté localement (voir RoleSwitcher) pour
 * permettre de prévisualiser chaque espace pendant le développement.
 */
export interface MockUserGroup {
  groupId: string;
  name: string;
  sectorName: string;
}

export interface MockUser {
  userId: string;
  firstName: string;
  lastName: string;
  role: RoleId;
  group: MockUserGroup;
}

interface AuthContextValue {
  user: MockUser | null;
  role: RoleId;
  isAuthenticated: boolean;
  setRole: (role: RoleId) => void;
  hasAtLeastRole: (required: RoleId) => boolean;
  updateProfile: (profile: { firstName: string; lastName: string }) => void;
}

const ROLE_STORAGE_KEY = "urue-mock-role";
const PROFILE_STORAGE_KEY = "urue-mock-profile";

const DEFAULT_GROUP: MockUserGroup = { groupId: "group-1", name: "Groupe Algrange Centre", sectorName: "Secteur Algrange" };
const DEFAULT_PROFILE = { firstName: "Jean", lastName: "Dupont" };

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

function readStoredRole(): RoleId {
  try {
    const saved = localStorage.getItem(ROLE_STORAGE_KEY);
    return (saved as RoleId) ?? "visiteur";
  } catch {
    return "visiteur";
  }
}

function readStoredProfile(): { firstName: string; lastName: string } {
  try {
    const saved = localStorage.getItem(PROFILE_STORAGE_KEY);
    return saved ? JSON.parse(saved) : DEFAULT_PROFILE;
  } catch {
    return DEFAULT_PROFILE;
  }
}

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [role, setRoleState] = useState<RoleId>(() => readStoredRole());
  const [profile, setProfile] = useState(() => readStoredProfile());

  const setRole = (next: RoleId) => {
    setRoleState(next);
    try {
      localStorage.setItem(ROLE_STORAGE_KEY, next);
    } catch {
      // stockage indisponible : le rôle reste actif pour la session
    }
  };

  const updateProfile: AuthContextValue["updateProfile"] = (next) => {
    setProfile(next);
    try {
      localStorage.setItem(PROFILE_STORAGE_KEY, JSON.stringify(next));
    } catch {
      // stockage indisponible : le profil reste actif pour la session
    }
  };

  const value = useMemo<AuthContextValue>(() => {
    const user: MockUser | null =
      role === "visiteur" ? null : { userId: "mock-user", role, group: DEFAULT_GROUP, ...profile };
    return {
      user,
      role,
      isAuthenticated: user !== null,
      setRole,
      hasAtLeastRole: (required) => roleAtLeast(role, required),
      updateProfile,
    };
  }, [role, profile]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth doit être utilisé à l'intérieur d'un AuthProvider");
  return ctx;
}
