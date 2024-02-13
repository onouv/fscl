

# Proof of Concept

## Goal

To prove all core concepts, use of toolchains, gain insight from implementing a first service
* Details of the [[Core Domain Model]] implementation
* shadow model library concept ([[Data Management Concept]])
* Postgres - Debezium - Kafka coupling
* general deployment concept

## Deliverables

### fscl-process-service 0.1.0

> A microservice implementing the backend for the process technology view, featuring the entire [[Core Domain Model]] with minimal extensions to adapt for process view specifics. Should be deployed as a Docker container on a linode by an Ansible playbook

open Java latest 
Spring Boot
Spring Cloud
OpenAPI
Docker/-compose
Ansible

### fscl-process-db 0.1.0

> The PostgreSQL database server for the process view. Should be deployed as a Docker container either locally by Docker-Compose or on a linode by an Ansible playbook. 

This deliverable may shrink down to a postgres server instance pulled and configured by its Docker file in a repo of that name. To keep the cost of the devsetup low, the data may be lost between node instances.


postgres
Debezium
Docker/ -componse
Ansible

### fscl-shadow-lib 0.1.0

> Shared library for shadow model management ([[Data Management Concept]])

open Java latest
Spring Boot
Spring Cloud
Maven
Debezium (?)
(Kafka)
OpenAPI (?)

### fscl-process-ui 0.1.0

> Web Frontend for the process view. 

React 18
Next 14
Typescript
npm
Node 20.11

### fscl-ui-commons 0.1.0

> A library to share common ui components, types and utilities. Allows to factor out anything that looks like it can be reused in other view-specific frontends from day one.

React 18
Next 14
Typescript
npm

### fscl-bus-service 0.1.0

>Kafka broker instance deployed in a Docker container either locally or on a linode via an Ansible playbook 

This deliverable may shrink down to a kafka broker instance pulled and configured by its Docker file in a repo of that name. To keep the cost of the devsetup low, the kafka instance data may be lost between node instances.

### fscl-automation-service-mock 0.1.0

>A microservice mocking  an automation backend service. Should maintain a simplified view model but implement a simple command line interface to trigger the core use cases of the fscl-shadow-lib (i.e. creating, deleting, recreating *elements*). Other than a production implementation, it does not provide a REST API for a future automation view ui.
>
>To be deployed locally as a Docker container (for dev testing only)

open Java latest 
Spring Boot
Spring Cloud
Maven


## Distributed Dev Deployment

```plantuml
node dev_host {
	component Intellij
	component Browser {
		artifact process_ui_app <<Next app>>
	}
	component bash
	component Ansible
}

cloud linode {
	node process_server {
		node process_container <<docker>> {
			interface process <<REST>>
			component process_service
			process -- process_service
		}
	}
	node process_db {
		node process_db_container <<docker>> {
			component debezium
			component postgres
			postgres --> debezium
		}
	}
	node process_ui_server {
		node process_ui_container <<docker>> {
			component process_ui <<Next server>>
		}
	}
	node bus_server {
		node bus_container <<docker>> {
			component kafka
		}
	}
	
	node automation_server {
		node automation-service_container <<docker>> {
			component automation_service
		}
	}
}

process_ui_app --> process_ui
process_ui --> process: https
bash --> automation_service: ssh
process_ui_app --> process: https
process_service -- kafka: tcp
debezium -- kafka: tcp
process_service -- postgres: tcp
automation_service -- kafka: tcp
```
