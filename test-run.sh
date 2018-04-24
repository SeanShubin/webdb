#!/usr/bin/env bash

set -x

curl -X POST -d 'page'     http://localhost:8080/task
curl -X POST -d 'styling'  http://localhost:8080/task
curl -X POST -d 'database' http://localhost:8080/task
curl -X POST -d 'api'      http://localhost:8080/task
curl -X POST -d 'bug'      http://localhost:8080/task
curl http://localhost:8080/task
curl -X DELETE http://localhost:8080/task/task-3
curl http://localhost:8080/task
curl http://localhost:8080/task/task-2

