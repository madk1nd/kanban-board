# kanban-board
Simple trello clone repository based on Vue.js and Spring Boot with QueryDSL

## start mongo instance in docker
docker run --name mongo -p 27017:27017 -v /path/to/db:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=admin -d mongo

## run vert.x with redeploy
java -jar target/backend-0.0.1-SNAPSHOT-fat.jar -conf ../config/vertx.json --redeploy="**/*.java" --on-redeploy="mvn package"

## configuration
in the root of repository go to the config folder and check files with configuration properties

## Three important environment variables
`MAILPASS=provide this password` if you wan't to send email registration confirmation emails
You also may provide this option through the config/dev.properties
`spring.mail.password=is what you need`
note: don't forget about `spring.mail.username` in this case

`VERTX_CONF=path/to/vertx.json` configuration for vertx instance
default path is pointing to config/vertx.json file

`NODE_ENV=production || development` default value is development