# Reactive microservices demo with Spring Cloud and Kafka
This project presents a demo of a microservice architecture based on Spring Cloud covering different 
services from the Netflix OSS like service registry and gateway.

The project contains microservices
developed using reactive programming based on Spring Webflux project and implements the CQRS pattern
using Apache Kafka as streaming platform.

> This is a Demo project to test different technologies and patterns. The implementation and business 
logic have been created just for the demo propose so it can be incomplete or contain some mistakes.


## Modules description

The project is divided in 9 modules. Two modules provides the configuration for Netflix OSS services,
two other modules provides Spring Cloud services for security and configuration and the other 4 modules
provides the business logic.

### Spring Cloud Netflix services

* **livesound-eureka:**
registry service for service discovering using Netflix OSS Eureka instance
provided by Spring Cloud Netflix integration. Maintain a registry of all the microservices and
works as a single point of contact to locate any service needed avoiding hardcoded addresses.
* **livesound-gateway:**
gateway or edge service that routes all the requests to the proper microservice on the backend.
Single point of contact for the frontend. Uses the Spring Cloud Netflix Zuul integration.

### Spring Cloud services
* **livesound-authserver:** 
configure the authentication service using Oauth 2.0 from spring cloud security.
* **livesound-config:**
externalized configuration service using Spring Cloud Config. Moves the configuration of the application to a different repository.

### Business services

* **livesound-profiles:**
Maintain and provide the information of the users profiles. Query service from the CQRS pattern. 
Receive the user creation events from Kafka using Spring Cloud Streams and saves the user data in the database. Provides an API to
request the profile information.
* **livesound-users:**
Handles the creation and modification of new users. Works as the command side of the CQRS pattern sending events to Kafka using Spring Cloud Streams.  
Provides an API to create and request users.
* **livesound-venue-command:**
Handles the creation and modification of Venues. Works as the command side of the CQRS pattern sending creation and modification 
events to Kafka using Spring Cloud Streams.  
Provides an API to create, modify and delete venues.
* **livesound-venue-query:**
Provide venues information. Works as the query side of the CQRS pattern receiving the venues modification events from Kafka and updating
the model as required. Provides an API to request venues information.
  
## Build and run the example

The project provides a docker-compose file that run containers for mongodb and kafka and also every microservice using the jar built on 
the target folder.

To run the project using docker compose:

```shell
mvn clean package
docker-compose up
```  
  
The project can be run locally if you start mongodb and kafka services on the default ports using the provided `run.sh` script
```shell
./run.sh
``` 
After starting the project you can validate that all the microserivces are registered to eureka using the address http://localhost:1111

