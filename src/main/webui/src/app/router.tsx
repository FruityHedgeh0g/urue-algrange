import { createBrowserRouter } from "react-router-dom";
import PublicLayout from "../components/templates/PublicLayout/PublicLayout";
import AccountLayout from "../components/templates/AccountLayout/AccountLayout";
import RequireRole from "../auth/RequireRole";
import HomePage from "../pages/HomePage/HomePage";
import NewsPage from "../pages/NewsPage/NewsPage";
import NewsDetailPage from "../pages/NewsDetailPage/NewsDetailPage";
import GalleryPage from "../pages/GalleryPage/GalleryPage";
import EventsPage from "../pages/EventsPage/EventsPage";
import EventDetailPage from "../pages/EventDetailPage/EventDetailPage";
import ContactPage from "../pages/ContactPage/ContactPage";
import DonationPage from "../pages/DonationPage/DonationPage";
import LoginPage from "../pages/LoginPage/LoginPage";
import RegisterPage from "../pages/RegisterPage/RegisterPage";
import ProfilePage from "../pages/ProfilePage/ProfilePage";
import MyEventsPage from "../pages/MyEventsPage/MyEventsPage";
import SectorPage from "../pages/SectorPage/SectorPage";
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
        { path: "actualites", element: <NewsPage /> },
        { path: "actualites/:postId", element: <NewsDetailPage /> },
        { path: "galerie", element: <GalleryPage /> },
        { path: "evenements", element: <EventsPage /> },
        { path: "evenements/:eventId", element: <EventDetailPage /> },
        { path: "contact", element: <ContactPage /> },
        { path: "don", element: <DonationPage /> },
        { path: "connexion", element: <LoginPage /> },
        { path: "inscription", element: <RegisterPage /> },
        {
          path: "mon-compte",
          element: (
            <RequireRole minRole="membre">
              <AccountLayout />
            </RequireRole>
          ),
          children: [
            { index: true, element: <ProfilePage /> },
            { path: "evenements", element: <MyEventsPage /> },
            {
              path: "secteur",
              element: (
                <RequireRole minRole="chef_de_groupe">
                  <SectorPage />
                </RequireRole>
              ),
            },
          ],
        },
        { path: "*", element: <NotFoundPage /> },
      ],
    },
  ],
  { basename }
);

export default router;
