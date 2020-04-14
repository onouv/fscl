# FSCL
## A System to Manage Systems

[Repository](https://github.com/onouv/fscl)

FSCL is a tool to model industrial systems from the following artefacts: 

**Functions**   - specifications of distinct elements of performance or behaviour

**Systems**     - logical groupings of *components* to perform a set of common functions

**Components**  - physical or software artefacts (parts)

**Locations**   - places where *components* are installed

## Motivation
Industrial systems such as factories, refineries, chemical plants, power stations, power transmission systems, automatic transport systems, sea ships and many more examples are pretty complex. Many different components must be skillfully designed and combined by many engineering disciplines to work together in countless different relationships.
- Electrical,  Mechanical and Civil Engineers are using advanced tools like CAD and CAE systems to create the phyiscal design of the systems. 
- Automation Software Engineers use advanced simulation and programming tool chains. 
- Production and supply chain use systems such as ERP and MES to manufacture or procure the systems and components

**Functional requirements**, however, often are managed in documents and in the mind only. There is no link in the various tools between the artifcacts of engineering and the functional model of the system. In most cases, there is no explicit functional model for a project at all. This means, they are often missed or lost along the way and cause trouble downstream of a project.
The success of a technical design is based on the *complete* understanding of just these functional requirements and their correct and complete *mapping to artefacts* in the physical and software design. FSCL is a demonstrator of a software system to bring this capability into the hands of project engineers and project managers.

## Architecture
FSCL is built in a microservices architecture based on Spring Boot and MongoDB with a common React web client as user interface. [Learn More...]()

Note it is built as a demonstrator for private purposes and provided here without any license or any liability. If anybody is interested in contributing I'd be happy to consider picking one of the open source licenses.
