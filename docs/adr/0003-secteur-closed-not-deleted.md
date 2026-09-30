# A Secteur is closed (Fermé), never deleted

Only the Super admin opens, renames, closes and reopens a Secteur. Closing makes the Secteur fermé instead of deleting it: it becomes read-only and is seen, with its Groupes and Events, by the Super admin only; its Groupes lose their Chef de groupe, its unfinished Events become Annulé (one En cours becomes Archivé), and every sign-up is kept as it was. We chose this to keep the association's lineage (which Events happened in which Secteur, who rode with which Groupe) intact, in line with Events never being deleted.

## Considered Options

- **Delete the Secteur with its Groupes, Events and sign-ups**: rejected. It breaks "Events are never deleted" and loses the history the association wants to keep.
- **Delete the Secteur only once it has no Event**: rejected. Once a Secteur has held an Event it could never be removed.
- **Delete the Secteur and keep its Events without one**: rejected. Every Event belongs to exactly one Secteur.

## Consequences

- A Secteur fermé can be reopened by the Super admin; its Groupes come back without a Chef and its Events keep their final status.
- Events of a Secteur fermé leave the public Event history: they read as not found for everyone but the Super admin.
