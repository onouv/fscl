# Introduction


## Motivation
Industrial systems such as factories, refineries, chemical plants, power stations, power transmission systems, automatic transport systems, sea ships and many more examples are pretty complex. Many different components must be skillfully designed and combined by many engineering disciplines to work together in countless different relationships.
- Electrical,  Mechanical and Civil Engineers are using advanced tools like CAD and CAE systems to create the phyiscal design of the systems.
- Automation Software Engineers use advanced simulation and programming tool chains.
- Production and supply chain use systems such as ERP and MES to manufacture or procure the systems and components

**Requirements**, however, often are managed in documents at best and in the mind only or not at all at worst. There is no link in the various tools between the artifacts of engineering and the functional model of the system. In most cases, there is no explicit functional model for a project at all. This means, they are often missed or lost along the way and cause trouble downstream of a project.
The success of a technical design is based on the *complete* understanding of just these functional and non-functional requirements and their correct and complete *mapping to artefacts* in the physical and software design. FSCL is a demonstrator of a software system to bring this capability into the hands of project engineers and project managers.

**IoT Applications** may benefit in various ways from a simple straightforward but consistent information model. Just a few examples:
- establishing the relation between *components* and *functions* enables deducting availablity of *functions* from operational state of the *components*, to continously assess availability of *functions*
- streaming apps could track process events (e.g. from [OPC UA protocols](https://opcfoundation.org/about/opc-technologies/opc-ua/) and assign them to the histories of either *functions* or *components* to generate insight on performance, reliability, usage patterns, etc.

## Architecture
FSCL newgen will be built in a microservices architecture based on Spring Boot and postgres with Next.JS React web clients as user interfaces. [Learn More...](doc/fscl/architecture/Architecture.md)

## Development
Ideas and plans for the future, open items, etc. [Learn More...](github/development.md)
