### Enterprise Integration Patterns – Content Based Router Microservice

A simple microservice with responsibility for routing and auditing messages

#### System Requirements
- Java 8
- Docker
- RabbitMQ
- MongoDB

#### Setup development environment
This command will setup RabbitMQ and MongoDB images on your docker container.
```
docker-compose up -d
```

#### Compile
```
./gradlew clean build -i
```

#### Run
```
./gradlew clean build -x test bootRun
```