# Architecture

Outline of the FSCL architecture - an experimental system under implementation.

## Views
The system now is cut differently from the original prototype: the views are disjunct application domains now rather than simply different parts of the common domain meta model. 

All inter-view data exchange is expected to happen in terms of  the core metamodel ([Data Management Concept](../data-management-concept/index.md), see also next section). Therefore, the need for view-specific inter-service messaging is expected to be not necessary anymore, and view-specific libraries for messaging API classes/ interfaces (DTO) should become obsolete.


## General Outline

A system of microservices connected by a Kafka backbone. Each service supports a specific domain view on the overall system under design (see [Views](../views/index.md) for details). The system will be built as a prototype for gathering experience of the effects from working with the distributed deeply structured engineering data following a common meta model. Therefore, features like authentication/ authorization, scaling of performance and volume, advanced deployment options are postponed. Unit tests will be implemented only for critical elements. Integration or end-to-end testing will be implemented sparingly only for most critical features. Import and export of data/ adaptation to engineering tools is postposed but will gain a higher priority after the core concept has been implemented and evaluated.   A build pipeline will be implemented when it appears necessary.

## Microservices
* Each view is represented as a separate microservice
* Each Function, System, Component, Location will be a separate Aggregate in each views microservice. The _Shadow entities_ as described in the [Data Management Concept](../data-management-concept/index.md) are modelled by the [Shadow as Entity pattern](shadow-as-entity.md) 
* The view services will provide RESTful interfaces to the view front end web clients and will communicate by publish / subscribe pattern over a Kafka messaging backbone
* Each microservice type will be using a distinct RDBMS server (one RDBMS server per type, i.e. all instances of one type will talk to the same DB server). Preferred default: PostgreSQL
* The database schemas will be managed by flyway
* To ensure **data consistency between the kafka messaging bus and the associated view database instance** at all times, the _transactional outbox pattern_ will be applied using [debezium change data capture](https://debezium.io/blog/2019/02/19/reliable-microservices-data-exchange-with-the-outbox-pattern/)

## Deployment
* Eventually, each view will be a kubernetes deployment, integrating
    * microservice backend
    * postgres database server backend
    * webserver for the frontend, exposed to an external http port
* The Kafka messaging backbone will be deployed into the k8s cluster by means of the Strimzi Operator system
* For development, Minikube will be used as a local k8s cluster 

## Web Front Ends

* Each Views UI will be implemented by a React web client
* All common functionality in the backend as well as in the frontend will be factored out into library artefacts (e.g. core data management, kafka messaging)
  
## Artefacts
I can drive the implementation only infrequently, so versioning of specific elements in the technology stacks at this point doesn't make much sense.

The logical dependency tree for the overall system looks like this:  

![Service Overview](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/onouv/fscl/newgen/doc/architecture/service-overview.puml)

[image-src](service-overview.puml)  

### Core Data Management Library

As each view-specific service must maintain a copy of a common Shadow Model as defined in the [Data Management Concept](../data-management-concept/index.md),  a shared library supporting this feature set is needed. This library can be used by any view specific service to implement the shadow model. 

Handles authoritative reference data for the following domain entities, see [Core Domain Model](../core-domain-model/index.md):
* Functions
* Systems
* Components
* Locations

We define **Domain Item** as an object of any of these entities mentioned above and **Domain Item Link** as a link between any of these Domain Items.

Features:
* Create new Domain Item and publish associated event
* Create child of Domain Item (same type) and publish associated event
* Create Domain Item Link and publish associated event
* allow to publish event for Deletion of a Domain Item or Domain Item Link in the view model and mark element in shadow model
* handle Domain Item created and Domain Item Link created events according to [Data Management Concept](../data-management-concept/index)
* handle domain item deleted and domain item link deleted events according to [Data Management Concept](../data-management-concept/index.md)
* Initialize a database instance for shadow model


### View-specific Services

For serving a web client with primitives for the View Model, each view-specific service needs to provide a RESTful API 

#### CREATE
* Create Domain Item
* Create child of Domain Item
* Link  Domain Items
#### READ
* List all Domain Items at root level
* List all children of Domain Item with ID
* Read Domain Item Details by ID
* List all associates of of Domain Item with ID (e.g. Systems, Components, Locations for a Function)
* Read all associated Functions of Domain Item with ID
* Read all associated Components of Domain Item with ID
* Read all associated Systems of Domain Item with  ID
* Read all associated Locations of Domain Item with ID

#### UPDATE
* Update Domain Item details with ID and publish associated event

#### DELETE
* Delete Domain Item
* Unlink Domain Items


 
