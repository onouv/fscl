# Core Domain Model

## Overview

![Overview](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/onouv/fscl/newgen/doc/fscl/core-domain-model/overview.puml)


 A **Function** represents a well described purpose, performance or purposeful behavior of a technical unit or process.  Example: Controlled Deceleration of a Train (Braking).

A **Component** represents a tangible piece of equipment or a unit of software which can independently deployed and tested. In both cases, the Component must be designed to fulfill a specific purpose (implement Functions). Example:  Driver Brake Valve in a Train.

A **System** represent a conglomerate of components and subsystems and is designed to fulfill a specific purpose (Function). Example: Train Brake System.    

A **Location** - well, yeah. Exactly

All **Entities** may carry **Parameters**. These allow to attach Metainfo to them which can be used for future parametric design features or just as an explicit expression of design assumptions or results.

Any Entity can be referenced by **Views**. **Views** are collections of entities which are enriched by additional view-specific semantics and to allow modeling of a specific aspect when engineering a system. Examples: Automation View, Electrical Engineering View, Safety and Reliability View.   

## Functions 

![Functions](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/onouv/fscl/newgen/doc/fscl/core-domain-model/functions.puml)


**Parent**: indicates that a Function owns another one as an integral part. The owned Function may also be called a sub function of its owner. Semantically it means that a parent function may use a sub function at any time during its own execution. 

**FunctionLinks**: (Abstract) may connect Functions in different sub trees and on different levels of decomposition. 

**Trigger** indicates that execution of the source  Function implies the execution of the target. 

**Require**:  like the parent-child relation, require indicates that a Function needs another one to execute and may use it at any time during its own execution, but it doesn't imply ownership, just dependency.

**Inhibit** indicates that a Function A may prevent the execution of Function B when A executes, irrespective of other conditions.

**Enable** indicates that a Function B may execute only (save other conditions) when a Function A executes and my not execute if B is not executing.

Use of Trigger, Require, Inhibit and Enable is mutually exclusive.



### Components

![Components](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/onouv/fscl/newgen/doc/fscl/core-domain-model/components.puml)


One or more **Components** may implement one or more **Functions**.

Components may be composed of sub components. 

A **System** may consist of one or more **Components**.

A **Location** may hold one or more **Components**, i.e. Components may be installed at a Location. It is intended that the Component breakdown structure will provide leaf elements that contain exactly one Component, but multiple components may also be lumped into one location.  In the first case, only one **holds** relation may be modelled, and it must be at the leaf Location (i.e. don't repeat this relation type for all parent locations). 

**ComponentLink:** indicates a physical connection between two **Components**. Intended to allow specific views to mark such a view-specific connection for other views without specifying any view particulars.

## Locations

![Locations](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/onouv/fscl/newgen/doc/fscl/core-domain-model/locations.puml)