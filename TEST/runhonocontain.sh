#!/bin/bash
#
# Start the local services and adapters as Docker containers
# Save on a dedicated file the running containers
#
# Hono services started:
#	the JDBC based device registry with dedicated Postgres server,
#	the AMQP 1.0 messaging infrastructure
#	the Jaeger tracing back end
#
# Hono adapters started:
#	mqtt, http

echo
date
echo
cd /home/user/hono/tests/
echo "--- stop all Hono related containers ---"
echo
mvn verify -PstopContainers
echo
echo "--- start hono containers ---"
echo
mvn verify -Dstart -Pjaeger,mqtt-only,http-only -Dhono.deviceregistry.type=jdbc -Dhono.messaging-infra.type=amqp
echo
cd /home/user/
echo "--- save running docker containers ---"
echo
docker ps | tee docker_containers.txt
