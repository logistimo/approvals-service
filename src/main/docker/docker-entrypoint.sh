#!/bin/bash

# Copyright © 2017 Logistimo.
#
# This file is part of Logistimo.
#
# Logistimo software is a mobile & web platform for supply chain management and remote temperature monitoring in
# low-resource settings, made available under the terms of the GNU Affero General Public License (AGPL).
#
# This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
# Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
# later version.
#
# This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
# warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
# for more details.
#
# You should have received a copy of the GNU Affero General Public License along with this program.  If not, see
# <http://www.gnu.org/licenses/>.
#
# You can be released from the requirements of the license by purchasing a commercial license. To know more about
# the commercial license, please contact us at opensource@logistimo.com
#

exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom \
    -Dserver.port=$SERVER_PORT \
    -Dspring.datasource.url=$MYSQL_HOST \
    -Dspring.datasource.username=$MYSQL_USER \
    -Dspring.datasource.password=$MYSQL_PASSWORD \
    -Dconversationservice.url=$CONVERSATION_SERVICE_URL \
    -Dspring.activemq.broker-url=$ACTIVEMQ_BROKER_URL \
    -Dspring.activemq.user=$ACTIVEMQ_USER \
    -Dspring.activemq.password=$ACTIVEMQ_PASSWORD \
    -Dtask.machine=$TASK_MACHINE \
    -javaagent:/opt/jmx_prometheus_javaagent-0.7.jar=$JAVA_AGENT_PORT:/opt/jmx_exporter.json \
    -jar /approval-service.jar


