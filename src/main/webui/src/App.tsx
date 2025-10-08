import React, { useState } from "react";
import ClickableButton from "./components/ClickableButton";

function App() {
    const [count, setCount] = useState(0);

    return (
        <main style={{ padding: 24 }}>
            <h1>Démo ClickableBoutton</h1>

            <div style={{ display: "flex", gap: 12, alignItems: "center" }}>
                <ClickableButton
                    label="Compter +1"
                    onClick={() => setCount((c) => c + 1)}
                />

                <ClickableButton
                    label="Avec image"
                    imageSrc="/vite.svg"   // mets le chemin de ton image
                    imageAlt="Logo"
                    onClick={() => alert("Clique détecté")}
                />

                <ClickableButton
                    label="Désactivé"
                    imageSrc="/vite.svg"
                    imageAlt="Logo"
                    disabled
                />
            </div>

            <p style={{ marginTop: 16 }}>Compteur: {count}</p>
        </main>
    );
}

export default App;
