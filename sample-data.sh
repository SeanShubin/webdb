#!/usr/bin/env bash

set -x

curl -X POST -d '{ "name":"page"    , "complete":false }' http://localhost:8080/task
curl -X POST -d '{ "name":"styling" , "complete":false }' http://localhost:8080/task
curl -X POST -d '{ "name":"database", "complete":false }' http://localhost:8080/task
curl -X POST -d '{ "name":"api"     , "complete":false }' http://localhost:8080/task
curl -X POST -d '{ "name":"bug"     , "complete":false }' http://localhost:8080/task
curl -X DELETE http://localhost:8080/task/task-3
curl -X POST -d '{ "complete":true }' http://localhost:8080/task/task-5
curl http://localhost:8080/task/task-4
curl http://localhost:8080/task
