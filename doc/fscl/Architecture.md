

A system of  Microservices connected by a Kafka backbone. Each Service support a specific domain view on the overall system under design (see [[Features]] for details). 

Each service has its own database management system 
# Core Data Management Llibrary

As each view-specific service must maintain a copy of a common Shadow Model as defined in the [[Data Management Concept]],  a shared library supporting this feature set is needed. This library can be used by any view specific service to implement the shadow model. 

Handles authoritative reference data for the following domain entities, see [[Core Domain Model]]:
* Functions
* Systems
* Components
* Locations

We define **Domain Item** as an object of any of these aforementioned entiies  and **Domain Item Link** as a link between eny of these Domain Items.

* Create new Domain Item and publish associated event
* Create child of Domain Item (same type) and publish associated event
* Create Domain Item Link and publish associated event
* allow to publish event for Deletion of a Domain Item or Domain Item Link in the view model and mark element in shadow model
* handle Domain Item created and Domain Item Link created events according to [[Data Management Concept]]
* handle domain item deleted and dmoain item link deleted events according to [[Data Management Concept]]
* Initialize a database instance for shadow model


# View-specific Services

For serving a web client with primitives for the View Model, each view-specific service needs to provide a REST API 

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



# Service Overview

```plantuml
component core_model <<library>>
component ui_core <<library>>
component automation_service <<microservice>>
interface "fscl/automation" as automation <<REST API>>
component automation_ui <<web client>>
component automation_db <<RDBMS>>

interface "fscl/safety" as safety <<REST API>>
component safety_service <<microservice>>
component safety_ui <<web client>>
component safety_db <<RDBMS>>

interface "fscl/process" as process <<REST API>>
component process_service <<microservice>>
component process_ui <<web client>>
component process_db <<RDBMS>>

component event_bus <<kafka-broker>>

process_ui --> process
process_ui --> ui_core
process --- process_service
process_service --> core_model
process_service --> process_db
process_service --> event_bus

automation_ui --> automation
automation_ui --> ui_core
automation --- automation_service
automation_service --> core_model
automation_service --> automation_db
automation_service --> event_bus

safety_ui --> safety
safety_ui --> ui_core
safety --- safety_service
safety_service --> core_model
safety_service --> safety_db
safety_service --> event_bus
```


# Change Data Capture

To allow for transactional consistency between the state that a view service injects into its associated database and the other view services connected via Kafka, the outbox pattern is implemented with Debezium.   