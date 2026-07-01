import React from "react";
import PhotoGrid from "../../components/organisms/PhotoGrid/PhotoGrid";

export const GalleryPage: React.FC = () => (
  <div className="container">
    <h1>Galerie photos</h1>
    <p>Revivez nos collectes, événements et actions solidaires en images.</p>
    <PhotoGrid />
  </div>
);

export default GalleryPage;
