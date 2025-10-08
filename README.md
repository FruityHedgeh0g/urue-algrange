# code-with-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- Quinoa ([guide](https://quarkiverse.github.io/quarkiverse-docs/quarkus-quinoa/dev/index.html)): Develop, build, and serve your npm-compatible web applications such as React, Angular, Vue, Lit, Svelte, Astro, SolidJS, and others alongside Quarkus.

## Provided Code

### Quinoa

Quinoa codestart added a tiny Vite app in src/main/webui. The page is configured to be visible on <a href="/quinoa">/quinoa</a>.

[Related guide section...](https://quarkiverse.github.io/quarkiverse-docs/quarkus-quinoa/dev/index.html)


### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)


## Troubleshooting (Windows)

If running the frontend dev server fails with an error similar to:

- "You are using Node.js 20.10.0. Vite requires Node.js version 20.19+ or 22.12+."
- "TypeError: crypto.hash is not a function" (stack trace inside node_modules/vite)

This happens because Vite 7 depends on a Node crypto API that was added in Node 20.19 and 22.12. Older Node 20.x (e.g., 20.10) do not have `crypto.hash`, so Vite crashes at startup on Windows.

Fix:
1. Upgrade Node to a supported version (recommended LTS 22.12+ or at least 20.19+).
   - With NVM for Windows: https://github.com/coreybutler/nvm-windows
     - nvm install 22.12.0
     - nvm use 22.12.0
2. Reinstall dependencies in the frontend folder to ensure native modules and lockfile match your Node version:
   - cd src\\main\\webui
   - Remove node_modules and package-lock.json if present
   - npm install
3. Start the dev server again:
   - npm run dev

Notes:
- The project now declares an engines requirement in src/main/webui/package.json to indicate the minimum Node version. npm will warn if your Node version is out of range.
- The app is served under the /quinoa base path in dev and build (vite.config.ts and scripts are configured accordingly).
