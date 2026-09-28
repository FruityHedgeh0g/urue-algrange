import React from "react";
import { useAuth } from "../../../auth/AuthContext";
import SpaceLayout from "../SpaceLayout/SpaceLayout";

export const AccountLayout: React.FC = () => {
  const { user } = useAuth();
  return (
    <SpaceLayout
      section="account"
      eyebrow="Espace membres"
      title="Mon espace"
      lead={user ? `Bonjour ${user.firstName}, retrouvez ici votre profil et vos inscriptions.` : undefined}
    />
  );
};

export default AccountLayout;
