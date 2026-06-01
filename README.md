# service-log

A scalable [Quarkus](https://quarkus.io/) container that centralizes **business data logging**.

It consumes log messages from an ActiveMQ Artemis queue and persists them as structured rows in PostgreSQL. It is an **ingest‑only** service: it does not expose a query API, it only stores the business events emitted by the other services of the platform.

## Architecture

```text
  producers (other services)
        │  ServiceLogMsg
        ▼
  Artemis queue "service-log-queue"  (replicated across 1..8 broker instances)
        │
        ▼
  service-log container ──► persists ──► PostgreSQL ("service_log" table)
```

- **Ingest**: a multi‑threaded JMS consumer reads `ServiceLogMsg` messages from `service-log-queue`.
- **Storage**: each message is stored as one row in the `service_log` table.

### `service_log` table

| Column | Type | Description |
|--------|------|-------------|
| `id` | bigint | Primary key (sequence `HIBERNATE_SEQ`). |
| `ts` | timestamptz | Business timestamp of the event (set by the producer). |
| `persistedTs` | timestamptz | Timestamp at which the row was persisted. |
| `module` | text | Source module / service. |
| `entity` | text | Related entity (e.g. an id). |
| `userid` | text | User associated with the event, if any. |
| `instance` | text | Producing instance. |
| `data1` … `data9`, `dataA` | text | Free‑form business payload fields. |

## Requirements

- Java 25 (the container image is built from `maven:3-eclipse-temurin-25`)
- Maven (a wrapper `./mvnw` is provided)
- PostgreSQL
- ActiveMQ Artemis (shared with the rest of the platform)

## Configuration

Configuration is provided through `src/main/resources/application.properties` and can be overridden with environment variables.

### Application

| Property | Default | Description |
|----------|---------|-------------|
| `quarkus.http.port` | `8701` | HTTP port. |
| `com.mobiera.ms.commons.service-log.threads` | `4` | Number of consumer threads (per Artemis instance). |
| `com.mobiera.ms.commons.service-log.debug` | `false` | Enables verbose logging. |

### Queue

| Property | Default | Description |
|----------|---------|-------------|
| `com.mobiera.ms.commons.service-log.jms.queue.name` | `service-log-queue` | Queue the messages are read from. |
| `com.mobiera.ms.commons.service-log.jms.incoming.ttl` | `86400000` | TTL (ms) of incoming messages. |
| `com.mobiera.ms.commons.service-log.jms.retry.delay` | `10000` | Retry delay (ms). |
| `com.mobiera.ms.commons.service-log.jms.ex.delay` | `10000` | Delay (ms) after an exception. |
| `com.mobiera.artemis.producer.poolsize` | `16` | Producer pool size. |

### Database

| Property | Env var | Description |
|----------|---------|-------------|
| `quarkus.datasource.db-kind` | — | Database kind (`postgresql`). |
| `quarkus.datasource.username` | — | DB username. |
| `quarkus.datasource.password` | `QUARKUS_DATASOURCE_PASSWORD` | DB password (never commit it). |
| `quarkus.datasource.jdbc.url` | `QUARKUS_DATASOURCE_JDBC_URL` | JDBC URL. |

The schema is managed by Hibernate (`quarkus.hibernate-orm.database.generation=update`).

### Artemis (1 to 8 instances)

Queues can be spread across **1 to 8 Artemis broker instances**. This decision is **global to the platform**: every Mobiera service (all `mno-adapters-*`, `stats`, `service-log`…) **must share the same Artemis configuration**, because all queues are replicated across all configured instances.

Select the number of instances:

```properties
com.mobiera.ms.mno.quarkus.artemis.instances=1
```

Configure each instance (`a0` … `a7`):

```properties
quarkus.artemis."a0".url=tcp://artemis:61616
quarkus.artemis."a0".username=quarkus
quarkus.artemis."a0".password=...

quarkus.artemis."a1".url=tcp://artemis:61616
quarkus.artemis."a1".username=quarkus
quarkus.artemis."a1".password=...
# ... up to a7
```

> **Note:** the Artemis credentials configured here must be able to connect to the broker(s).

On Kubernetes, use the equivalent environment variables:

```bash
COM_MOBIERA_MS_MNO_QUARKUS_ARTEMIS_INSTANCES=...

QUARKUS_ARTEMIS__A0__URL=...
QUARKUS_ARTEMIS__A0__USERNAME=...
QUARKUS_ARTEMIS__A0__PASSWORD=...

QUARKUS_ARTEMIS__A1__URL=...
# ...
```

## Producing log messages

Other services publish `ServiceLogMsg` messages (from the `service-log-api` dependency) to `service-log-queue`. Each message carries the business fields described in the `service_log` table above. Access to the `service-log-api` and `mobiera-commons` artifacts requires the appropriate Maven repository credentials, configured either in `settings.xml` or as environment variables.

## Build & run

Dev mode (live reload, Dev UI on `http://localhost:8701/q/dev/`):

```bash
./mvnw quarkus:dev
```

Package the application:

```bash
./mvnw package
# runnable with:
java -jar target/quarkus-app/quarkus-run.jar
```

Native executable:

```bash
./mvnw package -Pnative
# or, without a local GraalVM:
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

## Container image

CI builds and pushes the image to Docker Hub as `mobiera/service-log` (`src/main/docker/Dockerfile.jvm`).

- Push to `main` → tag `main`
- Push to `dev` → tag `dev`
- Release `vX.Y.Z` → tag `X.Y.Z`

Run locally:

```bash
docker run --rm -p 8701:8080 \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://db/aircast \
  -e QUARKUS_DATASOURCE_USERNAME=aircast \
  -e QUARKUS_DATASOURCE_PASSWORD=... \
  -e ARTEMIS_HOST=artemis \
  -e ARTEMIS_PASSWORD=... \
  mobiera/service-log:main
```

## License

Apache License 2.0 — see [LICENSE](./LICENSE).
