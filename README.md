<pre>

/$$$$$$  /$$$$$$$$ /$$$$$$$  /$$$$$$$   /$$$$$$        /$$$$$$ /$$   /$$ /$$$$$$$$ /$$$$$$$$ /$$    /$$ /$$$$$$ /$$$$$$$$ /$$      /$$
/$$__  $$| $$_____/| $$__  $$| $$__  $$ /$$__  $$      |_  $$_/| $$$ | $$|__  $$__/| $$_____/| $$   | $$|_  $$_/| $$_____/| $$  /$ | $$
| $$  \__/| $$      | $$  \ $$| $$  \ $$| $$  \ $$        | $$  | $$$$| $$   | $$   | $$      | $$   | $$  | $$  | $$      | $$ /$$$| $$
| $$      | $$$$$   | $$$$$$$/| $$$$$$$ | $$$$$$$$ /$$$$$$| $$  | $$ $$ $$   | $$   | $$$$$   |  $$ / $$/  | $$  | $$$$$   | $$/$$ $$ $$
| $$      | $$__/   | $$__  $$| $$__  $$| $$__  $$|______/| $$  | $$  $$$$   | $$   | $$__/    \  $$ $$/   | $$  | $$__/   | $$$$_  $$$$
| $$    $$| $$      | $$  \ $$| $$  \ $$| $$  | $$        | $$  | $$\  $$$   | $$   | $$        \  $$$/    | $$  | $$      | $$$/ \  $$$
|  $$$$$$/| $$$$$$$$| $$  | $$| $$$$$$$/| $$  | $$       /$$$$$$| $$ \  $$   | $$   | $$$$$$$$   \  $/    /$$$$$$| $$$$$$$$| $$/   \  $$
\______/ |________/|__/  |__/|_______/ |__/  |__/      |______/|__/  \__/   |__/   |________/    \_/    |______/|________/|__/     \__/

</pre>

### Required documentation includes:


#### Database configuration details
Connection to the DB requires docker on your system when running the application

On startup a docker compose script is ran through a spring boot integration (see spring-boot-docker-compose maven)
script is located in the root (see compose.yaml)

    datasource config:
    url: jdbc:postgresql://localhost:5432/cerba_interview_db
    username: postgres
    password: Th1s1sMyPass_word
        
For testing the application runs against a testcontainer with a postgres image (see AbstractBaseTest.java)
connection info is injected into the test properties when running
        

#### Instructions for running application tests

To run you can execute: 
 ``` bash
 mvn spring-boot:run
```
    

**BEFORE running without maven if you prefer that** 
- make sure annotation processing is enable in your IDE
- run a maven install to mapstruct, lombok, and openapi codegen
- mark /target/generated-sources as Generated Sources Root

Tests can be ran through the maven test step or by manually running the tests in the be.one16.cerbainterview.api package
can run with the default profile, no extra config required


#### Instructions for starting the application, including any profile to be used
run CerbaInterviewApplication.java no extra config required

- after starting swagger is available at http://localhost:8080/swagger-ui/index.html
- openapi-spec can be found in the resources folder (see recipe-api-1.0.0.yaml)
- project has intellij http files (see http folder in root)
