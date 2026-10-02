# PostgreSQL with Flyway in production; H2 for dev and tests

Production runs on PostgreSQL, and its schema is owned by Flyway migrations applied at startup, with Hibernate set to `validate`. Dev and tests keep H2, with Hibernate's `update`, so working on the app needs neither Docker nor a database server. H2 in file mode is not fit for a long-running multi-user server, and `update` plus hand-applied SQL patches cannot rename or drop columns safely, nor survive deploys done by pulling an image.

## Considered Options

- **H2 on a volume in production**: rejected. Single file, no safe online backup, and moving data out later is painful.
- **PostgreSQL everywhere, tests via Dev Services**: rejected for now. It needs Docker on every developer machine; the CI smoke test against PostgreSQL covers dialect drift instead.
- **Keep `update` and hand-applied scripts in `docs/migrations`**: rejected. Each deploy depends on someone remembering them.

## Consequences

- Dialect differences between H2 and PostgreSQL only surface in the CI smoke test, which starts the image on PostgreSQL with `validate`.
- Production starts empty from a Flyway baseline; the old H2 patches in `docs/migrations` are dropped.
