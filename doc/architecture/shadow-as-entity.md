# Shadow Items as Entities

It must be defined how shadow elements (see Data Management Concept) are to be implemented in an actual distributed system.

Currently the Shadow elements shall be modelled as Domain Entities rather than as Aggregates. The resulting structure is shown in diagram below:



![Shadow as Entity](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/onouv/fscl/doc/fscl/architecture/cqrs/shadow-as-entity.puml)

A similar structure would derive for _System_, _Component_, _Location_ by extending from `FunctionBase`.

To enable easy recognition in traces, message logs, etc., a Shadow Entity will be producing the same identifier as the View element, but in braces `(...)`.
