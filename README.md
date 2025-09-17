![](github/pics/master-logo.png)

# What is FSCL ?
FSCL is a tool to model industrial systems from the following simple artefacts:

* ***Functions*** - specifications of distinct elements of performance or behaviour
  * *for example, in an elevator system "Transport Passenger"*

* ***Systems***     - logical groupings of *components* to perform a set of common functions
  * *for example, in an elevator system "Drive System" or "Independent Brake System"*

* ***Components***  - physical or software artefacts (parts)
  * *for example, in an elevator system "Drive Motor" "Brake Magnet"*

* ***Locations***   - places where *components* are installed
  * *for example, in an elevator system "Machine Room", "Shaft Floor 1", "Concourse Floor 1"*  

Find more details on the motivation [here](doc/intro.md) 

# What is FSCL NewGen ?

I have refactored the original concept by defining a set of [application views](doc/views/index.md) and I am in the process of implementing a **NewGen of FSCL** based on these concepts, which you will find documented on 'main' in this repo.  

If the views before have been simply the Functions, Systems, Components and Locations described above, they are now defined with much more focus on real-world application purpose (at least in my mind :-). The four core concepts (FSCL) have now become elements of a common distributed [data management concept](doc/data-management-concept/index.md).

Find a more detailed explanation of this **core domain model** [here](doc/core-domain-model/index.md).

Find an outline of the new **architecture** [here](doc/architecture/index.md).

Find an outline of the new **data management concept** in the distributed system [here](doc/data-management-concept/index.md).

Find the [original FSCL demonstrator here](https://github.com/onouv/fscl/tree/master).


# Artefacts

## Supporting Artefacts
[fscl-core-lib](https://github.com/onouv/fscl-core-lib/tree/newgen)

## Process Technology View
[fscl-process-service](https://github.com/onouv/fscl-process-service)  
[fscl-process-ui]() (TO BE DONE)
