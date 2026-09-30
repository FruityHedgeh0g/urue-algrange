import React, { createContext, useContext, useMemo, useState } from "react";
import { ROLE_HIERARCHY, RoleId, roleAtLeast } from "./roles";

/**
 * Authentification mockée : tant que le backend n'expose pas de flux de
 * connexion, le rôle courant est piloté localement (voir RoleSwitcher) pour
 * permettre de prévisualiser chaque espace pendant le développement.
 */
/** Reflète NestedSectorDto : le Secteur d'une personne à partir de Membre (ADR 0004). */
export interface UserSector {
  sectorId: string;
  name: string;
}

export interface MockUser {
  userId: string;
  firstName: string;
  lastName: string;
  role: RoleId;
  /** Aucun pour un Bénévole (le vivier commun) ni pour le Super admin (au-dessus des Secteurs). */
  sector: UserSector | null;
  /** Nécessaire pour s'inscrire à un Événement. */
  phone?: string;
}

interface AuthContextValue {
  user: MockUser | null;
  role: RoleId;
  isAuthenticated: boolean;
  setRole: (role: RoleId) => void;
  hasAtLeastRole: (required: RoleId) => boolean;
  updateProfile: (profile: Profile) => void;
}

const ROLE_STORAGE_KEY = "urue-mock-role";
const PROFILE_STORAGE_KEY = "urue-mock-profile";

interface Profile {
  firstName: string;
  lastName: string;
  phone?: string;
}

const DEFAULT_PROFILE: Profile = { firstName: "Jean", lastName: "Dupont", phone: "06 12 34 56 78" };
const DEFAULT_SECTOR: UserSector = { sectorId: "sector-1", name: "Secteur Algrange" };

/** Le Secteur mocké : de Membre à Admin, le Secteur d'Algrange. */
const sectorFor = (role: RoleId): UserSector | null => (roleAtLeast(role, "membre") && role !== "super_admin" ? DEFAULT_SECTOR : null);

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

function readStoredRole(): RoleId {
  try {
    const saved = localStorage.getItem(ROLE_STORAGE_KEY);
    return ROLE_HIERARCHY.find((r) => r === saved) ?? "visiteur";
  } catch {
    return "visiteur";
  }
}

function readStoredProfile(): Profile {
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
      role === "visiteur" ? null : { userId: "mock-user", role, sector: sectorFor(role), ...profile };
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
