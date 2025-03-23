# service-log Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Artemis configuration


You can configure until 8 Artemis servers for queues. You need to decide globally for your project and all mno-adapters deployed for this project MUST share the same configuration.

All queues are replicated across all instances.

### Define the number of required instances

Select from 1 to 8.

```
com.mobiera.ms.mno.quarkus.artemis.instances=1
```

### Configure each instance

```
quarkus.artemis."a0".url=tcp://artemis:61616
quarkus.artemis."a0".username=quarkus
quarkus.artemis."a0".password=...

quarkus.artemis."a1".url=tcp://artemis:61616
quarkus.artemis."a1".username=quarkus
quarkus.artemis."a1".password=...

quarkus.artemis."a2".url=tcp://artemis:61616
quarkus.artemis."a2".username=quarkus
quarkus.artemis."a2".password=...

quarkus.artemis."a3".url=tcp://artemis:61616
quarkus.artemis."a3".username=quarkus
quarkus.artemis."a3".password=...

quarkus.artemis."a4".url=tcp://artemis:61616
quarkus.artemis."a4".username=quarkus
quarkus.artemis."a4".password=...

quarkus.artemis."a5".url=tcp://artemis:61616
quarkus.artemis."a5".username=quarkus
quarkus.artemis."a5".password=...

quarkus.artemis."a6".url=tcp://artemis:61616
quarkus.artemis."a6".username=quarkus
quarkus.artemis."a6".password=...

quarkus.artemis."a7".url=tcp://artemis:61616
quarkus.artemis."a7".username=quarkus
quarkus.artemis."a7".password=...

```

### Kubernetes

use:

```
 
 COM_MOBIERA_MS_MNO_QUARKUS_ARTEMIS_INSTANCES=...

 QUARKUS_ARTEMIS__A0__URL=...
 QUARKUS_ARTEMIS__A0__USERNAME=...
 QUARKUS_ARTEMIS__A0__PASSWORD=...
 
 QUARKUS_ARTEMIS__A1__URL=...
 ...
 
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

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
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/service-log-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
