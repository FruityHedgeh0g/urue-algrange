import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";

// Sélectionne la div racine du HTML
const rootElement = document.getElementById("root") as HTMLElement;

// Création du root React moderne (React 18+)
const root = ReactDOM.createRoot(rootElement);

// Rendu de l’application principale
root.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);
