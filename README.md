# IoT Fleet Management System

A real-time fleet tracking system built with Java microservices. You create vehicles, 
assign them routes across a graph of Midwest cities, and they travel between nodes 
in real time consuming fuel based on vehicle type. Services communicate asynchronously 
through Kafka and a Java CLI simulator drives the movement.

&nbsp;

## Tech Stack

Java 17 · Spring Boot · Apache Kafka · PostgreSQL · H2 · Lombok · Docker · Maven

&nbsp;

## Architecture

The system is split into four Spring Boot services and a standalone simulator.

The device service stores vehicle data and exposes a REST API for creating and 
updating devices. When a device is created it publishes an event to Kafka so the 
simulator picks it up automatically.

The route service implements Dijkstra's algorithm on a weighted undirected graph of 
7 Midwest cities. It listens for route requests on a Kafka topic, computes the 
shortest path, and publishes a route command back.

The telemetry service consumes device movement events and persists them to an H2 
database, exposing endpoints for latest readings and history per device.

The simulator is a plain Java CLI that fetches devices from the backend on startup, 
listens for route commands via Kafka, and executes routes by moving vehicles along 
graph edges tick by tick. It handles fuel consumption, speed variation, and syncs 
position back to the device service over HTTP.

The user service acts as a gateway that proxies requests to the device service and 
publishes route requests to Kafka.

&nbsp;

## Cities and Graph
```
0: Chicago     connects to Milwaukee, Indianapolis, Detroit
1: Milwaukee   connects to Chicago, Madison
2: Indianapolis connects to Chicago, Minneapolis, Columbus
3: Detroit     connects to Chicago, Columbus
4: Madison     connects to Milwaukee, Minneapolis
5: Minneapolis connects to Madison, Indianapolis
6: Columbus    connects to Indianapolis, Detroit
```

&nbsp;

## Running Locally

Start Kafka and Zookeeper first.
```bash
docker-compose up -d
```

Then start each service in separate terminals.
```bash
# Device service — port 8081
cd device-service && ./mvnw spring-boot:run

# Route service — port 8084
cd route-service && ./mvnw spring-boot:run

# Telemetry service — port 8083
cd telemetry-service && ./mvnw spring-boot:run

# User service — port 8082
cd user-service && ./mvnw spring-boot:run

# Simulator (start last, after at least one device exists)
cd simulator && mvn package && java -jar target/simulator-1.0-SNAPSHOT.jar
```

Create a device first using the device service REST API, then start the simulator 
and use the CLI to move it.

&nbsp;

## What I Learned

Writing Dijkstra from scratch and wiring it into a Kafka pipeline made the algorithm 
click in a way that just implementing it standalone doesn't. The hardest part was 
the simulator's threading model — route execution runs on its own thread while Kafka 
consumers run on daemon threads, so coordinating state between them without race 
conditions required using `ConcurrentHashMap` for the device registry and 
`volatile boolean` for the running flag. Debugging serialization mismatches between 
services (the simulator uses plain Kafka clients while Spring Boot uses Spring's 
JsonDeserializer) took more time than I expected.
