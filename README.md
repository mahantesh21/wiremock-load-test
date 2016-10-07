# wiremock-load-test
Using wiremock for load test

# How to run 
* Instructions specific to mac

## Whats needed
* Docker 1.12.1

## How to run wiremock server
* run oneclick.sh
* it will take care of building the project, building the docker image and starting it as a service
* hit http://localhost:12015/v1/contactprofiles to see the response

## How to run load test against the server
* Use Apache benchmark
* ab -v 4  -n 100000 -c 120 -t 100 http://localhost:12015/v1/contacts

