# Une Rose Un Espoir — Front

Front du site de l'association, servi par Quarkus/Quinoa (Vite + React + TypeScript).

## Démarrer

```bash
npm install
npm run dev      # serveur de dev, sur /quinoa
npm run build    # build de prod
npm test         # tests unitaires (Vitest)
```

## Architecture

Design atomique strict, avec CSS Modules pour une atomicité réelle des styles (aucune classe globale hors `theme/tokens.css`) :

```
src/
  app/            routing (React Router), providers (TanStack Query, thème, auth)
  auth/           contexte d'auth mocké, garde de route par rôle (RequireRole)
  theme/          tokens CSS (couleurs, espacements, mode nuit) + contexte de thème
  components/
    atoms/        Button, Badge, Input, Spinner, Logo...
    molecules/    FormField, MediaCard, DropdownMenu, AdminListItem...
    organisms/    Header, Footer, Carousel, PostList, EventList, PhotoGrid...
    templates/    PublicLayout, AccountLayout
  pages/          une page = une route
  features/       un dossier par domaine métier (events, posts, medias, sectors,
                   groups, users, roles, configurations, featureFlags,
                   featureRequests) : hooks TanStack Query + client API
  lib/            utilitaires partagés (dates, images de substitution)
```

## Données mockées

Le backend n'expose aujourd'hui que des `GET` liste pour la plupart des ressources
(`EventController`, `PostController`, `MediaController`...) ; les créations/éditions
et l'authentification ne sont pas encore implémentées côté Java. En attendant,
chaque domaine dans `features/*` expose un client API mocké (`*Api.ts`) qui :

- reflète exactement la forme des DTOs backend (`EventDto`, `PostDto`, `SectorDto`...) ;
- a la même signature qu'un futur appel `fetch` réel (fonctions `async`, mêmes
  paramètres/retours) ;
- persiste les créations/éditions en `localStorage` pour permettre de démontrer
  des parcours complets (inscription à un événement, édition d'un secteur...)
  sans backend actif.

**Pour rebrancher un domaine sur l'API réelle**, il suffit de remplacer le
contenu de son `*Api.ts` par de vrais appels `fetch`/`fetch` — les hooks
(`use*.ts`) et les composants qui les consomment n'ont pas besoin de changer.

Deux domaines (`featureFlags`, `configurations` en écriture, `featureRequests`)
n'ont aucune contrepartie backend actuelle et resteront mockés jusqu'à ce que
les endpoints correspondants existent.

## Rôles & authentification

L'authentification réelle (OIDC) n'est pas encore activée côté backend. En
attendant, `auth/AuthContext` simule une session avec un rôle courant parmi :
`visiteur`, `membre`, `benevole`, `chef_de_groupe`, `bureau`, `admin`
(hiérarchie croissante, voir `auth/roles.ts`). Les formulaires de connexion et
d'inscription authentifient localement avec le rôle `membre`.

En développement (`import.meta.env.DEV`), un sélecteur de rôle apparaît dans
l'en-tête pour prévisualiser chaque espace sans repasser par un vrai formulaire.
Il est absent du build de production.

Les routes protégées utilisent `<RequireRole minRole="...">`, qui redirige
vers l'accueil si le rôle courant est insuffisant.

## Thème

`theme/tokens.css` définit la palette (bleu du tracé moto, rouge-orangé de la
rose du logo) et un mode nuit complet, activé via `[data-theme="dark"]` sur
`<html>` (bascule dans le header, persistée en `localStorage`).

## Tests

`npm test` lance Vitest + Testing Library. Les tests couvrent la logique
métier sensible (hiérarchie des rôles, garde de route) et quelques composants
partagés (Button, FormField) — pas une couverture exhaustive de chaque écran.
