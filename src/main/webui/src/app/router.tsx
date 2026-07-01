import { ReactNode } from "react";
import { createBrowserRouter } from "react-router-dom";
import PublicLayout from "../components/templates/PublicLayout/PublicLayout";
import AccountLayout from "../components/templates/AccountLayout/AccountLayout";
import RequireRole from "../auth/RequireRole";
import { RoleId } from "../auth/roles";
import HomePage from "../pages/HomePage/HomePage";
import AboutPage from "../pages/AboutPage/AboutPage";
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
import MembersAdminPage from "../pages/MembersAdminPage/MembersAdminPage";
import SectorsAdminPage from "../pages/SectorsAdminPage/SectorsAdminPage";
import EventsAdminPage from "../pages/EventsAdminPage/EventsAdminPage";
import RolesAdminPage from "../pages/RolesAdminPage/RolesAdminPage";
import FeatureRequestsPage from "../pages/FeatureRequestsPage/FeatureRequestsPage";
import ConfigurationPage from "../pages/ConfigurationPage/ConfigurationPage";
import FeatureFlagsPage from "../pages/FeatureFlagsPage/FeatureFlagsPage";
import NotFoundPage from "../pages/NotFoundPage/NotFoundPage";

// Aligné sur --base=/quinoa (quarkus.quinoa.ui-root-path) pour que les liens
// internes et l'historique du navigateur restent cohérents avec le chemin de service.
const basename = import.meta.env.BASE_URL.replace(/\/$/, "") || "/";

function guarded(minRole: RoleId, element: ReactNode) {
  return <RequireRole minRole={minRole}>{element}</RequireRole>;
}

export const router = createBrowserRouter(
  [
    {
      path: "/",
      element: <PublicLayout />,
      children: [
        { index: true, element: <HomePage /> },
        { path: "qui-sommes-nous", element: <AboutPage /> },
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
            { path: "secteur", element: guarded("chef_de_groupe", <SectorPage />) },
            { path: "membres", element: guarded("bureau", <MembersAdminPage />) },
            { path: "secteurs", element: guarded("bureau", <SectorsAdminPage />) },
            { path: "gestion-evenements", element: guarded("bureau", <EventsAdminPage />) },
            { path: "roles", element: guarded("bureau", <RolesAdminPage />) },
            { path: "demandes-fonctionnalites", element: guarded("bureau", <FeatureRequestsPage />) },
            { path: "configuration", element: guarded("admin", <ConfigurationPage />) },
            { path: "fonctionnalites", element: guarded("admin", <FeatureFlagsPage />) },
          ],
        },
        { path: "*", element: <NotFoundPage /> },
      ],
    },
  ],
  { basename }
);

export default router;
