import { createBrowserRouter } from "react-router-dom";
import PublicLayout from "../components/templates/PublicLayout/PublicLayout";
import HomePage from "../pages/HomePage/HomePage";
import NotFoundPage from "../pages/NotFoundPage/NotFoundPage";

// Aligné sur --base=/quinoa (quarkus.quinoa.ui-root-path) pour que les liens
// internes et l'historique du navigateur restent cohérents avec le chemin de service.
const basename = import.meta.env.BASE_URL.replace(/\/$/, "") || "/";

export const router = createBrowserRouter(
  [
    {
      path: "/",
      element: <PublicLayout />,
      children: [
        { index: true, element: <HomePage /> },
        { path: "*", element: <NotFoundPage /> },
      ],
    },
  ],
  { basename }
);

export default router;
