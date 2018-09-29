# kanban-board
Simple trello clone repository based on Vue.js and Spring Boot with QueryDSL

## run vert.x with redeploy
java -jar target/backend-0.0.1-SNAPSHOT-fat.jar --redeploy="**/*.java" --on-redeploy="mvn package"
