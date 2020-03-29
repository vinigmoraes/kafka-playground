# Kafka Playground 

Bank Application simulating a Kafka environment using Kafka Connect, Schema Registry and Kafka Streams.

For building and running the application you need:

- [Gradle](https://gradle.org/)
- [Docker](https://www.docker.com/)


## Executing requests

[![Run in Postman](https://run.pstmn.io/button.svg)](https://documenter.getpostman.com/view/826217/SzKVRJQU)


## Running

```
docker-compose up --build
```

## Kafka Commands

Use this command inside kafka container or if you already have kafka in your computer.

```
List All Topics

    kafka-topics.sh --list --zookeeper localhost:2181

Create Topic

    kafka-topics.sh --zookeeper localhost:2181 --create --topic users-created --partitions 3 --replication-factor 1

See topic messages

    kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic users-created --from-beginning
```

#### Running with docker

```shell
./gradlew build && docker-compose up --build
```

##  Testing

```shell
./gradlew test
```

## Deployment

Application is configured to every push to master execute deploy automatic.

## Built With

- [Kotlin](https://kotlinlang.org/) - Programming language
- [IntelliJ](https://www.jetbrains.com/idea/) - IDE
- [Ktor](https://ktor.io) - Lightweight Web Framework
- [Gradle](https://gradle.org/) - Dependency Management
- [Docker](https://www.docker.com/) - Containerization Platform

