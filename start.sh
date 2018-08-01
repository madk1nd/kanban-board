#!/bin/sh

apt update
apt install -y screen
screen -dmS kanban java -jar backend/target/backend-0.0.1-SNAPSHOT.jar