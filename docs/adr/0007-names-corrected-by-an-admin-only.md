# A person's names are corrected by an Admin only

A person's first and last name come from Keycloak when they register, then belong to the database: an Admin corrects them for the Inscrits of their Secteur, the Super admin for anyone, and nobody renames themselves, not even from Mon espace, where a person edits only their phone number. The association knows its people by their real names, which appear on rosters and in exports, so they change only through someone accountable for the Secteur.

## Considered Options

- **Each person edits their own names** (Mon espace, or their Keycloak account): rejected for the reason above.
- **The Bureau edits names**, like it promotes: rejected; the user asked to keep it to Admins.

## Consequences

- Names changed in a Keycloak account no longer reach the database: Keycloak's "user updated" event only fills a name still missing. Keycloak and the site may therefore show different names.
- Renaming follows the Inscrits list (ADR 0004): an Admin renames the people of their Secteur and the Bénévoles who signed up for one of its Events.
