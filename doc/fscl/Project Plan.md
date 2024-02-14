
# Proof of Concept

## Goal

To prove all core concepts, use of tool chains, gain insight from implementing a first service
* Details of the [Core Domain Model](./core-domain-model/Core%20Domain%20Model.md) implementation
* shadow model library concept ([Data Management Concept](./data-management-concept/Data%20Management%20Concept.md))
* Postgres - Debezium - Kafka coupling
* general deployment concept

## Deliverables

### fscl-process-service 0.1.0

> A microservice implementing the backend for the process technology view, featuring the entire [Core Domain Model](./core-domain-model/Core%20Domain%20Model.md) with minimal extensions to adapt for process view specifics. Should be deployed as a Docker container on a linode by an Ansible playbook

* open Java latest 
* Spring Boot
* Spring Cloud (reuse of fscl-messaging-lib ?)
* OpenAPI
* Docker/-compose
* Ansible

### fscl-process-db 0.1.0

> The PostgreSQL database server for the process view. Should be deployed as a Docker container either locally by Docker-Compose or on a linode by an Ansible playbook. 

This deliverable may shrink down to a postgres server instance pulled and configured by its Docker file in a repo of that name. To keep the cost of the devsetup low, the data may be lost between node instances.

* postgres
* Debezium
* Docker/ -componse
* Ansible

### fscl-core-lib 0.1.0

> Shared library for shadow model management ([Data Management Concept](./data-management-concept/Data%20Management%20Concept.md))

* open Java latest
* Spring Boot
* Spring Cloud
* Maven
* Debezium (?)
* (Kafka)
* OpenAPI (?)

### fscl-process-ui 0.1.0

> Web Frontend for the process view. 

* React 18
* Next 14
* Typescript
* npm
* Node 20.11

### fscl-ui-commons 0.1.0

> A library to share common ui components, types and utilities. Allows to factor out anything that looks like it can be reused in other view-specific frontends from day one.

* React 18
* Next 14
* Typescript
* npm

### fscl-bus-service 0.1.0

>Kafka broker instance deployed in a Docker container either locally or on a linode via an Ansible playbook 

This deliverable may shrink down to a kafka broker instance pulled and configured by its Docker file in a repo of that name. To keep the cost of the devsetup low, the kafka instance data may be lost between node instances.

### fscl-automation-service-mock 0.1.0

>A microservice mocking  an automation backend service. Should maintain a simplified view model but implement a simple command line interface to trigger the core use cases of the fscl-shadow-lib (i.e. creating, deleting, recreating *elements*). Other than a production implementation, it does not provide a REST API for a future automation view ui.
>
>To be deployed locally as a Docker container (for dev testing only)

* open Java latest 
* Spring Boot
* Spring Cloud
* Maven


