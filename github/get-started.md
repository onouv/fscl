# Get Started

These instructions are not complete, work in progress...

## PREREQUISITES

### Environment

generally assuming linux / ubuntu environment, adapt for others  

**Java 1.8**

**Maven**

**Web Browser** I tested with Firefox and Chrome

[npm](https://www.npmjs.com/package/npm-home)

**git**  well, yeah.

### IDE

personal choice, of course

**Spring Tool Suite (Eclipse)**

**Atom**


### MongoDB

[MongoDB](https://docs.mongodb.com/guides/server/install/)


### Kafka Messaging

[Kafka](https://kafka.apache.org/quickstart)

### Zookeeper

[Apache Zookeeper](https://zookeeper.apache.org/)

[A few tips for setup](https://www.digitalocean.com/community/tutorials/how-to-install-and-configure-an-apache-zookeeper-cluster-on-ubuntu-18-04)


## BUILD PREP
`$ mkdir fscl; cd fscl`

`$ git clone https://github.com/onouv/fscl`

### Build Client

`$ cd fscl/client`

`$ npm install`


### Build Server 

`$ export FSCLHOME=[path to fscl dir]`

`$ cd fscl`

`$ ./fscl.build`

The Maven dependencies are not very tidy, yet (sorry, it's a TODO). This might require you to run the `fscl.build` script a few times, until Maven eventually has downloaded everything and built all dependencies. 

#### Setup Ports and Credentials

**component-service**

  rename [component-service/src/main/resources/application.sample](../component-service/src/main/resources/application.sample)
to `application.yml`
  
  choose reasonable values for 
  
      server:      
        port: <port-number> (client actually expects 8083)
  
      data:      
        mongodb:        
          uri: mongodb://<user-name>:<password>@<server>:27017/fscl-components
          
**function-service**

  rename [function-service/src/main/resources/application.sample](../function-service/src/main/resources/application.sample)
to `application.yml`
  
  choose reasonable values for 
  
     server:      
        port: <port-number> (client actually expects 8081)
  
     data:      
        mongodb:        
          uri: mongodb://<user-name>:<password>@<server>:27017/fscl-functions
          
**project-service**

  rename [project-service/src/main/resources/application.sample](../project-service/src/main/resources/application.sample)
to `application.yml`
  
  choose reasonable values for  
      
      data:      
        mongodb:        
          uri: mongodb://<user-name>:<password>@<server>:27017/fscl-projects


## STARTUP

### Start MongoDB

no authorization in place (not recommended):

`mongod --dbpath="<path-to-data-dir>" &`

with authorization in place:

`mongod --dbpath="<path-to-data-dir>" --auth &`

### Start Zookeeper

brute force: 

`$ sudo /<kafka-install-dir>/bin/zookeeper-server-start.sh /<kafka-install-dir>/config/zookeeper.properties`

elegant: 

`$ sudo systemctl start zookeeper.service`


### Start Kafka

brute force: 

`$ sudo /<kafka-install-dir>/bin/kafka-server-start.sh /<kafka-install-dir>/config/server.properties`

elegant : 

`$ sudo systemctl start kafka.service`

### Start Project Service

`$ cd fscl/project-service`

`$ mvn spring-boot:run`

### Start Function Service

`$ cd fscl/function-service`

`$ mvn spring-boot:run`

### Start Component Service

`$ cd fscl/component-service`

`$ mvn spring-boot:run`

### Start Web Client
`$ cd fscl/client`

`$ npm start`

Then open a web browser at ***`http://localhost:3000`***



