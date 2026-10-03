import React from "react";
import { Navigate } from "react-router-dom";
import { useAccess } from "./useAccess";
import { useAuth } from "./AuthContext";
import { AccessId, entry } from "./access";

export interface RequireAccessProps {
  id: AccessId;
  children: React.ReactNode;
}

/** Protège une route selon la carte d'accès : redirige vers l'accueil si l'entrée n'est pas accessible. */
export const RequireAccess: React.FC<RequireAccessProps> = ({ id, children }) => {
  const access = useAccess();
  const { isLoading } = useAuth();
  // Un lien direct vers une page protégée attend de savoir qui est connecté avant de rediriger
  if (isLoading) return null;
  if (entry(id).feature && !access.ready) return null;
  if (!access.canAccess(id)) return <Navigate to="/" replace />;
  return <>{children}</>;
};

export default RequireAccess;
