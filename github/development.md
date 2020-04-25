# FSCL Development

Future activties, if I get around to it: 

(1)     tidy Maven dependencies - OPEN


(2)     Write clean test cases       

* for the services REST APIs (mock DB and messaging)

* for the client UI (mock services)



(3)     Transactional consistency ( a rather large action ... )

*  Ensure this for the messaging: support the [transaction trailing log pattern](https://microservices.io/patterns/data/transaction-log-tailing.html) or the [message outbox pattern](https://microservices.io/patterns/data/transactional-outbox.html). With MongoDB this could be an issue. 
        
*  Ensure this for the database


(4)     *Components* could be enriched by additional descriptive or classification features to allow users defining more        
meaningfull items. This could mean an interface to allow selecting [eClass categories](https://www.eclasscontent.com/index.php?id=&action=&searchtxt=&options=&version=11.0&language=en) or interfaces to connect to component vendor repositories, etc. 

(5)     *Functions* could be tagged with additional properties defining functional safety (e.g. IEC 61508 [Safety Integrity Levels](https://en.wikipedia.org/wiki/Safety_integrity_level) )

(6)     Any of these entities should be *reusable* for the users, i.e. users should be allowed to browse in catalogs of predefined *Functions* and *Components* select from there. Searching by categories such as [eCl@ss](https://www.eclasscontent.com/index.php?id=&action=&searchtxt=&options=&version=11.0&language=en) could be supported. Users should be allowed to save existing entities in such catalogs. 

(7)     Of course, *Locations* and *Systems* should be supported in a way similar to what is currently implemented for *Functions* and *Components*

(8)     A streaming service could process incoming event data (such as process alarms and faults, and assign each event to the appropriate *Component* and/or *Function* for future evaluation. An evaluation service could then walk over these and generate findings and/ or action recommendations (e.g. "function XYZ inhibited due to fault of component UVW, currently backed by function ABC, but recommend maintenance staff activation within 48 hrs")

        
