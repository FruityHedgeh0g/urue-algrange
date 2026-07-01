import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";
import { RoleId } from "./roles";

export interface RequireRoleProps {
  minRole: RoleId;
  children: React.ReactNode;
}

/** Protège une route : redirige vers l'accueil si le rôle courant est insuffisant. */
export const RequireRole: React.FC<RequireRoleProps> = ({ minRole, children }) => {
  const { hasAtLeastRole } = useAuth();
  if (!hasAtLeastRole(minRole)) {
    return <Navigate to="/" replace />;
  }
  return <>{children}</>;
};

export default RequireRole;
