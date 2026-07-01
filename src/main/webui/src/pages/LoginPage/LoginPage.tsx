import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../auth/AuthContext";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import styles from "./LoginPage.module.css";

export const LoginPage: React.FC = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | undefined>();
  const { setRole } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!email || !password) {
      setError("Merci de renseigner votre e-mail et votre mot de passe.");
      return;
    }
    setRole("membre");
    navigate("/");
  };

  return (
    <div className="container-narrow">
      <h1>Connexion</h1>
      <form className={styles.form} onSubmit={handleSubmit} noValidate>
        <FormField
          label="E-mail"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          error={error && !email ? error : undefined}
          required
        />
        <FormField
          label="Mot de passe"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          error={error && !password ? error : undefined}
          required
        />
        <Button type="submit" label="Se connecter" />
      </form>
      <p className={styles.footer}>
        Pas encore de compte ? <Link to="/inscription">Inscrivez-vous</Link>
      </p>
    </div>
  );
};

export default LoginPage;
