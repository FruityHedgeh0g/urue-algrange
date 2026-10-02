# A person's Role is stored in the database and mirrored one-way to Keycloak

The application database is the source of truth for each person's Role, and every API access check reads it from there. On each Role change the application moves the person into the matching Keycloak group, so tokens and the Keycloak console stay consistent, but Keycloak never drives the Role. We chose this so promotions made on the site take effect on the next request (no stale token claims), stay testable without a running Keycloak, and still leave Keycloak usable by other tools that read groups.

## Considered Options

- **Keycloak groups as the source of truth** (the former `urue.oidc.groups.*` mapping): rejected. Every promotion becomes a Keycloak admin API call that tests must mock, and access lags until the person's token is refreshed.
- **Database only, no Keycloak mirror**: rejected. Keycloak and anything reading its groups would show no Roles at all.

## Consequences

- Keycloak access sits behind a small adapter ("set this person's Role group"), replaced by a fake in tests.
- If the mirror call fails, the database Role still applies; the Keycloak group is repaired on the next Role change. A group changed by hand in Keycloak has no effect on access.
