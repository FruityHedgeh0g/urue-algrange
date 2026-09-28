import React from "react";
import PhotoGrid from "../../components/organisms/PhotoGrid/PhotoGrid";
import PageHero from "../../components/organisms/PageHero/PageHero";

export const GalleryPage: React.FC = () => (
  <>
    <PageHero
      eyebrow="En images"
      title="Galerie photos"
      lead="Revivez nos collectes, événements et actions solidaires en images."
    />
    <div className="container">
      <PhotoGrid />
    </div>
  </>
);

export default GalleryPage;
