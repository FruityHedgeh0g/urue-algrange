import React, { useEffect, useState } from "react";
import "./Navbar.css";
import { DropdownMenu } from "./DropdownMenu";


const THEME_KEY = "urue-theme";

function getSystemPrefersDark(): boolean{
  if (typeof window === 'undefined' || !window.matchMedia) return false;
  return window.matchMedia('(prefers-color-scheme: dark)').matches;
}

function applyTheme(theme: 'light'|'dark'|null){
  const root = document.documentElement;
  if(theme === 'dark'){
    root.setAttribute('data-theme','dark');
  } else if(theme === 'light'){
    root.removeAttribute('data-theme');
  } else {
    // no preference: remove attribute to let CSS @media pick
    root.removeAttribute('data-theme');
  }
}

const Navbar: React.FC = () => {
  const [isDark, setIsDark] = useState<boolean>(false);

  // Initialize theme from storage or system
  useEffect(() => {
    try{
      const saved = localStorage.getItem(THEME_KEY) as 'light'|'dark'|null;
      const shouldDark = saved ? saved === 'dark' : getSystemPrefersDark();
      setIsDark(shouldDark);
      applyTheme(saved ?? null);
    }catch{
      const shouldDark = getSystemPrefersDark();
      setIsDark(shouldDark);
      applyTheme(null);
    }
  }, []);

  // Keep state and DOM in sync when toggling
  const toggleTheme = () => {
    setIsDark(prev => {
      const next = !prev;
      const val: 'light'|'dark' = next ? 'dark' : 'light';
      try{ localStorage.setItem(THEME_KEY, val); }catch{}
      applyTheme(val);
      return next;
    });
  };

  return (
    <header className="navbar">
      <div className="nav-left">
        <nav className="nav-links" aria-label="Primary">
          <DropdownMenu
            label="Services"
            items={[
              { label: "Collecte 2025", href: "#collecte" },
              { label: "Bénévolat", href: "#benevolat" },
              { label: "Dons", href: "#don" },
            ]}
          />
          <DropdownMenu
            label="Association"
            items={[
              { label: "Qui sommes-nous?", href: "#about" },
              { label: "Équipe", href: "#team" },
              { label: "Contact", href: "#contact" },
            ]}
          />
          <DropdownMenu
            label="Actualités"
            items={[
              { label: "Événements", href: "#events" },
              { label: "Blog", href: "#blog" },
            ]}
          />
        </nav>
      </div>

      <div className="nav-center">
        <a className="logo-image" href="#" aria-label="Accueil">
          <img src={`logo_asso_transparent.png`} alt="Une Rose Un Espoir - Algrange" />
        </a>
      </div>

      <div className="nav-right" aria-label="Actions">
        <button
          type="button"
          className="theme-toggle"
          onClick={toggleTheme}
          aria-pressed={isDark}
          aria-label={isDark ? "Passer en mode clair" : "Passer en mode sombre"}
          title={isDark ? "Mode clair" : "Mode sombre"}
        >
          <span className="icon" aria-hidden>{isDark ? '☀️' : '🌙'}</span>
        </button>
        <button className="login-btn" onClick={() => alert("Connexion")}>Se connecter</button>
      </div>
    </header>
  );
};

export default Navbar;
