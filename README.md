Movie Ticket System Design Exercise
===================================

### Description
Simple but consistent and scalable system that allows following:
* Create a movie entity with available number of tickets
* Reserve a ticket for a given movie
* Get a current status of a given movie

Interface is simple RESTful-like JSON-over-HTTP API build on Akka HTTP.

### Goals
* Consistency - all requests to given movie are linearized, single actor stores state for a movie
* Durability - all updates are stored to the persistent storage in document-oriented manner
* Persistence - MongoDB was chosen as a scalable document-oriented database
* Isolation - the 'Transaction' entity with own lifecycle is being created for each reservation request
* Scalability - sharding of independent movies' states across nodes gives horizontal scalability

### Timeline
* 12/5, 21:30 Start: repository setup from template, brief description
* 

