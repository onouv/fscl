# Architecture

Outline of the FSCL architecture - an experimental system under implementation.

## General Outline

A system of microservices connected by a Kafka backbone. Each service supports a specific domain view on the overall system under design (see [[Views]] for details). The system will be built as a prototype for gathering experience with the management of distributed deeply structured engineering data following a common meta model (FSCL). Therefore, features like authentication/ authorization, scaling of performance and volume, advanced deployment options are postponed. Unit tests will be implemented only for critical elements. Integration or end-to-end testing will be implemented sparingly only for most critical features. Import and export of data/ adaptation to engineering tools is postposed but will gain a higher priority after the core concept has been implemented and evaluated.   A build pipeline will be implemented when it appears necessary.

* Each service has its own database management system (one database server per view, i.e. 1 DB to n view service instances) 
* The view services will provide RESTful interfaces to the view front end web clients and will communicate by publish / subscribe pattern over a Kafka messaging backbone
* To allow for transactional consistency between the state that a view service injects into its associated database and the other view services connected via Kafka, the outbox pattern is implemented with Debezium
* Each service type will be deployed on a separate linode
* Each Views UI will be implemented by a Next.js Web Client
* View front ends will be served from a separate ui server linode per view 
* All common functionality in the backend as well as in the frontend will be factored out into library artefacts (e.g. core data management, kafka messaging)

The system now is cut differently from the original prototype: the views are disjunct application domains now rather than simply parts of the common domain meta model. All inter-view data exchange is expected to happen in terms of  the core metamodel ([Data Management Concept](../data-management-concept/Data%20Management%20Concept.md), see also next section). Therefore, the need for view-specific inter-service messaging is expected to be not necessary anymore, and view-specific libraries for messaging API classes/ interfaces (DTO) should become obsolete. 

I can drive the implementation only infrequently, so versioning of specific elements in the technology stacks at this point doesn't make much sense: 

* Back end stack : 
	* Java 17, 
	* Spring Boot
	* JPA 
	* postgres + Debezium postgres adapter
	* ubuntu linux
	* linode nanode (or bigger as required)

* Front end stack
	* React
	* Material UI
	* Next.JS
	* nginx
	* ubuntu linux
	* linode nanode (or bigger as required)

* Messaging stack
	* Kafka
	* zookeeper (?)
	* ubuntu linux
	* linode nanode (or bigger as required)

Ansible playbooks will be used to manage the linode setups.

## Core Data Management Llibrary

As each view-specific service must maintain a copy of a common Shadow Model as defined in the [Data Management Concept](../data-management-concept/Data%20Management%20Concept.md),  a shared library supporting this feature set is needed. This library can be used by any view specific service to implement the shadow model. 

Handles authoritative reference data for the following domain entities, see [[Core Domain Model]]:
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
* handle Domain Item created and Domain Item Link created events according to [Data Management Concept](../data-management-concept/Data%20Management%20Concept.md)
* handle domain item deleted and domain item link deleted events according to [Data Management Concept](../data-management-concept/Data%20Management%20Concept.md)
* Initialize a database instance for shadow model


## View-specific Services

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



## Service Overview

![Service Overview](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/onouv/fscl/newgen/doc/fscl/architecture/service-overview.puml)


# Deployment for Dev

![Deployment](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/onouv/fscl/newgen/doc/fscl/architecture/deployment.puml)

 
