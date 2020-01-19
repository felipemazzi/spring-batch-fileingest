# Spring Batch File Ingest

## Project Description

A [Spring Batch][SB] example application with the following features:

[SB]:https://spring.io/projects/spring-batch

* [External configuration in environment variables][12FCONFIG]
* [Run the job from within a Web Container][SBWEB]
* Launch the job asynchronously from a HttpRequest (REST endpoint)

[12FCONFIG]:https://12factor.net/config

[SBWEB]:https://docs.spring.io/spring-batch/docs/current-SNAPSHOT/reference/html/job.html#runningJobsFromWebContainer

This example is an ETL process that:
* Read an input file with persons *name*, *phone number* and *e-mail address*
* Process/Transform data
  * Split the *name* into *first name* and *last name*
  * Parse the *phone number* to a long value
* Write the persons data into a database

## Setup

You need an input file with *name*, *phone number* and *e-mail address* delimited by comma ( , ).

You need at least one database to store the data processed from the input file. The script 
`src/main/sql/setup-db-mysql.sql` is an example to create the initial structure in a MySQL instance.

> **TIP**:
>
> You may run a [MySQL instance in a docker container][MYSQLIMAGE].

[MYSQLIMAGE]:https://hub.docker.com/_/mysql

The application needs external configuration defined in environment variables as follows:

URL of the input file with the data to be read and processed by Spring Batch and the number of lines 
to skip when reading the file:

* APP_INPUT_FILE_URL
* APP_INPUT_LINES_SKIP

URL, username and password of the *output* datasource used to write the contents of input file on 
the database:

* APP_OUTPUT_DATASOURCE_URL
* APP_OUTPUT_DATASOURCE_USERNAME
* APP_OUTPUT_DATASOURCE_PASSWORD

URL, username and password of the *job* datasource used to store the Spring Batch core tables:

* APP_JOB_DATASOURCE_URL
* APP_JOB_DATASOURCE_USERNAME
* APP_JOB_DATASOURCE_PASSWORD

## Running

* Build the application
* Start the application with `java -jar JAR_FILE` or `mvn spring-boot:run`
  * This will launch a Spring Boot web container listening on port 8080 by default
* Start the job making a HTTP POST request to http://localhost:8080/jobs
