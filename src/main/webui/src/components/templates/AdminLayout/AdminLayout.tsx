import React from "react";
import SpaceLayout from "../SpaceLayout/SpaceLayout";

/** Espace "Administration" (Bureau et supérieur), accessible depuis la barre de navigation principale. */
export const AdminLayout: React.FC = () => (
  <SpaceLayout
    section="admin"
    eyebrow="Bureau & administration"
    title="Administration"
    lead="Gérez les inscrits, les secteurs, les événements et le contenu du site."
  />
);

export default AdminLayout;
