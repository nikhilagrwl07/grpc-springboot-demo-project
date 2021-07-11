# Following tech stack used in project

 - springboot
 - cassandra (as data store)
 - spring-boot-starter-data-cassandra-reactive (orm) 
 - maven
 - wiremock (for client-server integration testing between aggregator client and service)  
 - grpc (for internal service communication between aggregator-service to 
   user-service and movie-service)
 - rest (for external service communication between aggregator-service with aggregator-client
    using REST based API)

# Pending Task

- Grpc based exception handling (in user-service and movie-service)
- Integration test cases based on wiremock 