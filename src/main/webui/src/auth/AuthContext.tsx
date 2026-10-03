import React, { createContext, useContext, useMemo, useState } from "react";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { RoleId, roleAtLeast } from "./roles";
import { CurrentUser, fetchMe, loginUrl, LOGOUT_URL, Profile, updateMe } from "./session";

export type { CurrentUser, Profile, UserSector } from "./session";

interface AuthContextValue {
  user: CurrentUser | null;
  role: RoleId;
  isAuthenticated: boolean;
  /** true tant que l'on ne sait pas encore qui est connecté. */
  isLoading: boolean;
  hasAtLeastRole: (required: RoleId) => boolean;
  updateProfile: (profile: Profile) => Promise<void>;
  /** Part vers Keycloak, puis revient sur `redirect` (la page courante par défaut). */
  login: (redirect?: string) => void;
  /** Part vers le formulaire d'inscription de Keycloak. */
  register: () => void;
  logout: () => void;
}

const ME_KEY = ["me"] as const;

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

const currentPath = () => `${window.location.pathname}${window.location.search}${window.location.hash}`;

function navigateTo(url: string) {
  window.location.assign(url);
}

function contextValue(
  user: CurrentUser | null,
  isLoading: boolean,
  updateProfile: AuthContextValue["updateProfile"]
): AuthContextValue {
  const role: RoleId = user?.role ?? "visiteur";
  return {
    user,
    role,
    isAuthenticated: user !== null,
    isLoading,
    hasAtLeastRole: (required) => roleAtLeast(role, required),
    updateProfile,
    login: (redirect = currentPath()) => navigateTo(loginUrl(redirect)),
    register: () => navigateTo(loginUrl("/", true)),
    logout: () => navigateTo(LOGOUT_URL),
  };
}

/** La personne connectée, lue sur GET /api/users/me ; un Visiteur n'a pas de session. */
const SessionAuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const queryClient = useQueryClient();
  const me = useQuery({ queryKey: ME_KEY, queryFn: fetchMe, staleTime: Infinity, retry: false });

  const value = useMemo(
    () =>
      contextValue(me.data ?? null, me.isPending, async (profile) => {
        queryClient.setQueryData(ME_KEY, await updateMe(profile));
      }),
    [me.data, me.isPending, queryClient]
  );
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

/** Une personne donnée d'avance, sans attendre /api/users/me : pour les tests (test/testUser). */
const FixedAuthProvider: React.FC<{ children: React.ReactNode; user: CurrentUser | null }> = ({ children, user }) => {
  const [current, setCurrent] = useState(user);
  const value = useMemo(
    () =>
      contextValue(current, false, async (profile) => {
        await updateMe(profile);
        setCurrent((prev) => (prev ? { ...prev, ...profile } : prev));
      }),
    [current]
  );
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const AuthProvider: React.FC<{ children: React.ReactNode; user?: CurrentUser | null }> = ({ children, user }) =>
  user === undefined ? <SessionAuthProvider>{children}</SessionAuthProvider> : <FixedAuthProvider user={user}>{children}</FixedAuthProvider>;

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth doit être utilisé à l'intérieur d'un AuthProvider");
  return ctx;
}
