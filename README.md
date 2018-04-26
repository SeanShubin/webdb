# Web Database

## Running the tests
- still in prototyping mode, no tests yet

## Deploying the application

`gradle build`

## Running the application
`gradle jar`
`java -jar build/libs/webdb-1.0-SNAPSHOT.jar`
`./test-run.sh` 

You should get the following output

    + curl -X POST -d page http://localhost:8080/task
    task-1
    + curl -X POST -d styling http://localhost:8080/task
    task-2
    + curl -X POST -d database http://localhost:8080/task
    task-3
    + curl -X POST -d api http://localhost:8080/task
    task-4
    + curl -X POST -d bug http://localhost:8080/task
    task-5
    + curl http://localhost:8080/task
    {
      "task-1" : "page",
      "task-2" : "styling",
      "task-3" : "database",
      "task-4" : "api",
      "task-5" : "bug"
    }
    + curl -X DELETE http://localhost:8080/task/task-3
    task-3
    + curl http://localhost:8080/task
    {
      "task-1" : "page",
      "task-2" : "styling",
      "task-4" : "api",
      "task-5" : "bug"
    }
    + curl http://localhost:8080/task/task-2
    styling

## How this application was created
- did not use tooling, created everything manually starting with the build.gradle file
