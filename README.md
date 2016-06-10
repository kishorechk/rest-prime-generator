# rest-prime-generator
RESTful service that calculates prime numbers. The service generates prime number with upper range upto 2^63-1. Long.MAX_VALUE

The service has been developed using Java 8 and Spring Boot Web and runs on port 8080. 
Clone the repository. 
The build tool is maven 3. Assuming maven is installed and is present in classpath, command "mvn clean package" will create the jar in the target folder. 
Command "java -jar target/rest-prime-generator-0.0.1-SNAPSHOT.jar" will make the tomcat to listen to http requests at port 8080.

This project consists of Swagger. This url - http://localhost:8080/swagger-ui.html, will provide all the api methods. 

http://localhost:8080/swagger-ui.html#!/primes-controller/generatePrimesUsingGET will trigger an async request to generate the prime numbers. This request will receive a request uuid in response which can be used to get the prime results.

The service caches the prime results for 3 hours.

It implements various strategies to generate prime numbers which can be looked up using http://localhost:8080/api/v1/primes/strategyNames
  "FORK_JOIN",
  "ITERATIVE",
  "PARALLEL_STREAM",
  "STREAM",
  "ERATOSTHENES_SIEVE"
  

The unit tests cases has been written using TestNG and Mockito. TestNG is to test the algorithms/methods of prime generation. Mockito has been used to test the primes service
