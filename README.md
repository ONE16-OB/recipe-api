<pre>
 $$$$$$\  $$$$$$$$\ $$$$$$$\  $$$$$$$\   $$$$$$\        $$$$$$\ $$\   $$\ $$$$$$$$\ $$$$$$$$\ $$$$$$$\  $$\    $$\ $$$$$$\ $$$$$$$$\ $$\      $$\ 
$$  __$$\ $$  _____|$$  __$$\ $$  __$$\ $$  __$$\       \_$$  _|$$$\  $$ |\__$$  __|$$  _____|$$  __$$\ $$ |   $$ |\_$$  _|$$  _____|$$ | $\  $$ |
$$ /  \__|$$ |      $$ |  $$ |$$ |  $$ |$$ /  $$ |        $$ |  $$$$\ $$ |   $$ |   $$ |      $$ |  $$ |$$ |   $$ |  $$ |  $$ |      $$ |$$$\ $$ |
$$ |      $$$$$\    $$$$$$$  |$$$$$$$\ |$$$$$$$$ |$$$$$$\ $$ |  $$ $$\$$ |   $$ |   $$$$$\    $$$$$$$  |\$$\  $$  |  $$ |  $$$$$\    $$ $$ $$\$$ |
$$ |      $$  __|   $$  __$$< $$  __$$\ $$  __$$ |\______|$$ |  $$ \$$$$ |   $$ |   $$  __|   $$  __$$<  \$$\$$  /   $$ |  $$  __|   $$$$  _$$$$ |
$$ |  $$\ $$ |      $$ |  $$ |$$ |  $$ |$$ |  $$ |        $$ |  $$ |\$$$ |   $$ |   $$ |      $$ |  $$ |  \$$$  /    $$ |  $$ |      $$$  / \$$$ |
\$$$$$$  |$$$$$$$$\ $$ |  $$ |$$$$$$$  |$$ |  $$ |      $$$$$$\ $$ | \$$ |   $$ |   $$$$$$$$\ $$ |  $$ |   \$  /   $$$$$$\ $$$$$$$$\ $$  /   \$$ |
 \______/ \________|\__|  \__|\_______/ \__|  \__|      \______|\__|  \__|   \__|   \________|\__|  \__|    \_/    \______|\________|\__/     \__|
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

To run tests: 

``` bash
mvn test
```

#### Instructions for starting the application, including any profile to be used

To run the application:

``` bash
mvn spring-boot:run 
```

**Interact with the api**
- after starting swagger is available at http://localhost:8080/swagger-ui/index.html
- openapi-spec can be found in the resources folder (see recipe-api-1.0.0.yaml)
- project has intellij http files (see http folder in root)

**running without maven**
- make sure annotation processing is enable in your IDE
- run a maven install to mapstruct, lombok, and openapi codegen
- mark /target/generated-sources as Generated Sources Root for compilation
- run CerbaInterviewApplication.java


