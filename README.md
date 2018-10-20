# kanban-board
Simple trello clone repository based on Vue.js and Spring Boot with QueryDSL

## start mongo instance in docker
docker run --name mongo -p 27017:27017 -v /path/to/db:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=admin -d mongo

## run vert.x with redeploy
java -jar target/backend-0.0.1-SNAPSHOT-fat.jar -conf ../config/vertx.json --redeploy="**/*.java" --on-redeploy="mvn package"
