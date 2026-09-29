# One ranked Role per person; leading a Groupe is an Affectation

Each person holds exactly one Role on the ladder `visiteur < bénévole < membre < chef_de_groupe < bureau < admin < super_admin`, and each Role grants everything below it. Being Chef de groupe is a title held on that ladder and kept between Groupes; *which* Groupe a Chef leads is a separate Affectation made by the Bureau. We chose this over letting Roles stack (e.g. `bureau` + `chef_de_groupe` on one person) because the ladder keeps every access check a single comparison, and the only thing stacking would add — the Groupe someone leads — is carried by the Affectation.

## Considered Options

- **Stacked, independent Roles**: rejected. Every Bureau member would also need `chef_de_groupe` to reach pages the association considers below the Bureau, and access checks would become set membership instead of one comparison.
- **Leadership stored only on the Groupe, no title**: rejected. A Chef de groupe keeps the title while between Groupes so they can be reassigned.

## Consequences

- A Bureau member who holds the Chef de groupe title but leads no Groupe cannot be told apart from any other Bureau member. Accepted: every Bureau member is eligible for an Affectation anyway.
- Roles, Bureau membership and Affectations are not yet scoped to a Secteur, since Algrange is the only one. When a second Secteur opens, Bureau and Admin will be scoped to their Secteur, with the Super admin above all Secteurs.
