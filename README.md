# SimpeDataStreamPipelines

---

This is a project shows howto create DataStream Pipelines based on Spring Boot and Kafka 
that are simple, efficient and based on Jdk 21+. A DataStream Pipeline has a source that
receives changes/requests and transforms them to events that are send to Kafka. The 
Kafka events are received by the sink and transformed to be send to the receiving
system. A DataStream is one Spring Boot application providing the source and sink.
The scalability and efficiency is provided by Jdk 21+ Virtual Threads. 

Author: Sven Loesekann

Technologies: Java, Kotlin, Spring Boot, Jpa, Postgresql, Soap, Rest, Kafka, Maven,
Kubernetes, Helm

[![CodeQL](https://github.com/Angular2Guy/SimpleDataStreamPipelines/actions/workflows/codeql.yml/badge.svg)](https://github.com/Angular2Guy/SimpleDataStreamPipelines/actions/workflows/codeql.yml)

## Articles

---

[Simple Efficient Spring/Kafka DataStreams](https://angular2guy.wordpress.com/2025/02/24/simple-efficient-spring-kafka-datastreams/)

## Features

---

1. SourceSink Application to create changes/events/requests to be processed by the DataStreams.
2. DatabaseToRest DataStream that uses Debezium to receive the changes in a table and send them to a rest endpoint.
3. SoapToDb DataStream that receives Soap requests and stores them in a database with Jpa.
4. EventToFile DataStream that receives Kafka events and stores them in files on the filesystem.
5. A Helm chart to deploy Kafka(KRaft), Postgresql and all the applications of the system to a Minikube Kubernestes cluster.

