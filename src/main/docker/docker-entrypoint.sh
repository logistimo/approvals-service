#!/bin/bash


exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom \
    -Dserver.port=$SERVER_PORT \
    -Dspring.datasource.url=$MYSQL_HOST \
    -Dspring.datasource.username=$MYSQL_USER \
    -Dspring.datasource.password=$MYSQL_PASSWORD \
    -Dconversationservice.url=$CONVERSATION_SERVICE_URL \
    -Dspring.activemq.broker-url=$ACTIVEMQ_BROKER_URL \
    -Dspring.activemq.user=$ACTIVEMQ_USER \
    -Dspring.activemq.password=$ACTIVEMQ_PASSWORD \
    -jar /approval-service.jar


