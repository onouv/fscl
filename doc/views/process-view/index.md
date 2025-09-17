
# Process View

A view to represented the design thinking of the technological expert (i.e. Hot Rolling Specialist, Chemical Process Engineer). Will contain mostly high-level functions and main components/ systems. 

![Overview](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/onouv/fscl/refs/heads/main/doc/views/process-view/process-domain.puml)

[Overview source](process-domain.puml)
  
A *Function, Component*, ... may have any number of *Parameters*. These are intended to be referenced in other entities of this view or even other view. 

>**Example**  
>  
> A "Pump Feedwater" *ProcessFunction* object has a suction header, a discharge header and flow rate as inputs. From this, rated power and rated RPM can be determined, but this depends on load curves  of a concrete pumd component, so that calculation is deferred to the *Component* workflow

A *Parameter* may depend on any number of other parameters. This is expressed by the association to a *DependencyList* (not shown in diagram).  A *Function* therefore may have any number of *DependencyLists*. A *DependencyList* does not actually compute any new values from the *Parameter* but only keeps track on which *Parameter* depends on which other.   

[Worked Example...](example.md)

## Features and Requirements

F: Allow entering of calculations and design specifications representing the technological details of a function (e.g. calculations of suction and discharge headers, calculation of flow rates and required drive power ratings and speeds for a centrifugal pump system  

F: Allow linking these calculations and their results and variables (parameters) to function parameters and component rating parameters
Allow linking of registered documents  