# Web Database

## Running the application

    mvn clean
    mvn verify
    java -jar console/target/webdb.jar 8080

## Running the tests
- still in prototyping mode, no tests yet

## Verifying the application

Run the application, then

    ./sample-data.sh 

You should get the following output

    + curl -X POST -d '{ "name":"page"    , "complete":false }' http://localhost:8080/task
    task-1
    + curl -X POST -d '{ "name":"styling" , "complete":false }' http://localhost:8080/task
    task-2
    + curl -X POST -d '{ "name":"database", "complete":false }' http://localhost:8080/task
    task-3
    + curl -X POST -d '{ "name":"api"     , "complete":false }' http://localhost:8080/task
    task-4
    + curl -X POST -d '{ "name":"bug"     , "complete":false }' http://localhost:8080/task
    task-5
    + curl -X DELETE http://localhost:8080/task/task-3
    task-3
    + curl -X POST -d '{ "complete":true }' http://localhost:8080/task/task-5
    task-5
    + curl http://localhost:8080/task/task-4
    {
      "name" : "api",
      "complete" : false,
      "id" : "task-4"
    }
    + curl http://localhost:8080/task
    [ {
      "name" : "page",
      "complete" : false,
      "id" : "task-1"
    }, {
      "name" : "styling",
      "complete" : false,
      "id" : "task-2"
    }, {
      "name" : "api",
      "complete" : false,
      "id" : "task-4"
    }, {
      "name" : "bug",
      "complete" : true,
      "id" : "task-5"
    } ]
