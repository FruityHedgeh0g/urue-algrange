# Une Rose Un Espoir — Algrange

Site and member space of the association: public pages, a member space ("Mon espace") and an administration space ("Administration").

## Glossary

### Access

**Role**: a person's single access level, ordered `visiteur` < `bénévole` < `membre` < `chef_de_groupe` < `bureau` < `admin` < `super_admin` (`RoleEnum` in the back end, stored per person; mirrored in `webui/src/auth/roles.ts`). A role grants everything the lower roles grant.
_Avoid_: permission, legal role, organizational role.

**Access map**: the single declaration of every navigable entry (path, label, minimum Role, space, optional Feature) (`webui/src/auth/access.ts`). Route guards, the header and the space tabs all derive from it, so a visible link always leads to an accessible page.

**Feature**: a switch an admin can turn on or off at runtime (`dons-en-ligne`, `inscription-evenements`, `galerie-photos`). An Access map entry or a UI element can depend on a Feature.
_Avoid_: feature flag (in UI copy: "fonctionnalité").

**Space**: a group of pages sharing a layout and tab bar: `main` (header), `account` (Mon espace), `admin` (Administration).

### People

**Visiteur**: anyone browsing the site without being registered.

**Bénévole**: a Visiteur who has registered on the site. Has Mon espace and can sign up for Events. Belongs to no Secteur: the Bénévoles form one pool shared by every Secteur, riding at any Secteur's Events and promoted to Membre by the Bureau of a Secteur they rode with.
_Avoid_: volunteer, inscrit, user

**Membre**: a Bénévole who is part of the association through one Secteur, promoted by a Bureau member once their membership fee is settled (the fee itself is handled off-site). Every Role from Membre up belongs to exactly one Secteur (the Super admin excepted) and rides only at that Secteur's Events. Nobody changes Secteur (except when the Super admin appoints them Admin of another one): a person goes back to Bénévole, losing their Secteur, and is promoted again in the other one.
_Avoid_: adhérent

**Chef de groupe**: a Membre holding the operational function of leading a Groupe, appointed by the Bureau. Keeps the title between Affectations.
_Avoid_: group leader, responsable

**Affectation**: the Bureau's decision that a given Chef de groupe leads a given Groupe. A Groupe has at most one Chef de groupe at a time; a Chef de groupe leads at most one Groupe. Handing a Groupe to a new Chef de groupe is a single Affectation.
_Avoid_: assignment, nomination

**Bureau**: the Membres elected to run the association: an organizational function, ranked above Chef de groupe. Promotes Membres and Chefs de groupe. Promoted by an Admin.

**Président**: the one Bureau member who presides over the association. The only Bureau function the site records; grants no extra access yet.

**Admin**: a person who manages the site for one Secteur and promotes its Bureau members. Appointed by the Super admin from all registered people, for a Secteur the Super admin chooses.

**Super admin**: the person above every Admin, who appoints Admins and is the only one to open, rename, close (Fermé) or reopen a Secteur.

### Organisation

**Secteur**: a local branch of the national association (e.g. Algrange), subdivided into Groupes. Opened, renamed and closed only by the Super admin; its description is kept up to date by the Bureau.

**Secteur fermé**: a Secteur the Super admin has closed instead of deleting it, so that its lineage is kept: read-only, and seen with its Groupes and Events by the Super admin only. Closing ends its Groupes' Affectations (the Chefs keep their title), makes its unfinished Events Annulé (one En cours becomes Archivé) and keeps every sign-up as it was; the Super admin can reopen it.
_Avoid_: supprimé, gelé, actif (for the other state: a Secteur is simply a Secteur), deleted, frozen
_Avoid_: sector (in UI copy), zone

**Groupe**: a lasting team of bikers covering its own part of a Secteur, led by a Chef de groupe. Always belongs to exactly one Secteur. Who rides with a Groupe is decided per Event (see Demande de groupe).
_Avoid_: team, équipe

**Demande de groupe**: a Participant's request to ride with a specific Groupe at one Event, made when signing up. Stays pending until the Bureau or that Groupe's Chef de groupe accepts or refuses it. A refused Participant stays signed up without a Groupe, and can make a new Demande or be placed by the Bureau.
_Avoid_: join request, candidature

### Events

**Event**: a gathering organised by the Bureau for a Secteur. Never deleted: cancelled instead.

**Event status**: where an Event stands in its lifecycle: `Planification` → `Ouvert` ⇄ `Complet` (set by hand by the Bureau), then `En cours` and `Archivé` (follow the dates of an `Ouvert` or `Complet` Event; one still in `Planification` stays there, hidden). `Annulé` is set by hand from any status before `Archivé`. Only the Bureau sees an Event in `Planification`.
_Avoid_: Publié (for Events)

**Participant**: a Bénévole with a confirmed place at an Event (someone on a Liste d'attente is not yet a Participant), either as `pilote` or `passager` (chosen per Event). A `passager` is a registered Bénévole too and rides with the Groupe of their `pilote`. Signing up is possible while the Event is `Ouvert` or `Complet`; while `Complet`, or once a maximum is reached, a sign-up goes onto the Liste d'attente. A Participant can withdraw at any time before the Event is `Archivé`. The Bureau can remove a Participant from an Event; the Chef de groupe can only take them out of their Groupe, leaving them a Participant without a Groupe.

**Liste d'attente**: the queue of people who asked to take part in an Event, or to ride with a Groupe at an Event, after the maximum the Bureau set for that Event (or for that Groupe at that Event) was reached. It also serves as a reserve of riders on the day. Nobody moves up automatically: the Bureau (or, for a Groupe, its Chef de groupe) chooses. A `passager` counts towards every maximum and always follows their `pilote`.
_Avoid_: waitlist, file d'attente

### Content

**Post**: a news article written by the Bureau of a Secteur, belonging to that Secteur, either `Brouillon` or `Publié`.
_Avoid_: article, actualité

**Media**: a picture or video shown by the site, used in Posts and in the photo gallery. A video may live on an external platform rather than on the site.
