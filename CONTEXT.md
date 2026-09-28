# Une Rose Un Espoir — Algrange

Site and member space of the association: public pages, a member space ("Mon espace") and an administration space ("Administration").

## Glossary

**Role**: a user's access level, ordered `visiteur` < `membre` < `bénévole` < `chef_de_groupe` < `bureau` < `admin` (`webui/src/auth/roles.ts`). A role grants everything the lower roles grant.
_Avoid_: permission (Role permissions exist in the roles admin screen but control nothing yet).

**Access map**: the single declaration of every navigable entry (path, label, minimum Role, space, optional Feature) (`webui/src/auth/access.ts`). Route guards, the header and the space tabs all derive from it, so a visible link always leads to an accessible page.

**Feature**: a switch an admin can turn on or off at runtime (`dons-en-ligne`, `inscription-evenements`, `galerie-photos`). An Access map entry or a UI element can depend on a Feature.
_Avoid_: feature flag (in UI copy: "fonctionnalité").

**Space**: a group of pages sharing a layout and tab bar: `main` (header), `account` (Mon espace), `admin` (Administration).
