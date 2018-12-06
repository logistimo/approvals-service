Approvals service
=================

Approval Service is web service which exposes APIs for creating approvals, retrieving approvals and approval status history, updating approval status and fetching a list of approvals based on a variety of filters. It contains a scheduled task for expiring an approval in a specific queue and for expiring approval as a whole. It generates events for approval creation, approval status update and approver status update for the purpose of consumption by dependent services.


Pre-requisites
------------------

* MariaDB
* Docker
* Gradle



Build Instructions
------------------

To build the artifact and create a docker image of the approval service, run the following commands.


1. Build the artifact and docker image

```
./gradlew --stacktrace clean build buildDocker
````