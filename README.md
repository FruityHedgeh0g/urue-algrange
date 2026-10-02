# Lyfia

Lyfia is the software behind the site and member space of the association **Une Rose Un Espoir**: public pages, a member space ("Mon espace") and an administration space ("Administration"), across its Secteurs.

"Lyfia" names the software only. People using the site see the association, never Lyfia. The domain vocabulary lives in [`CONTEXT.md`](CONTEXT.md) and the decisions in [`docs/adr/`](docs/adr/).

## Stack

- [Quarkus](https://quarkus.io/) (Java 21) for the REST API
- [Quinoa](https://docs.quarkiverse.io/quarkus-quinoa/dev/) serving the React frontend in `src/main/webui`
- Keycloak for authentication (OIDC) and Role mirroring (ADR 0002)
- RabbitMQ for user events published by Keycloak

## Running in dev mode

```shell
./mvnw quarkus:dev
```

The dev profile stores data in a local H2 file under `data/`. The Dev UI is at <http://localhost:8080/q/dev/>.

## Tests

```shell
./mvnw verify
```

## Packaging

```shell
./mvnw package
```

This produces `target/quarkus-app/`, runnable with `java -jar target/quarkus-app/quarkus-run.jar`.
