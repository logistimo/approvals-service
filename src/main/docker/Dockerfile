#
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

FROM openjdk:8-jre

MAINTAINER dockers@logistimo.com

VOLUME /tmp

ARG APP_NAME

ARG APP_VERSION

ADD approval-service-0.1.0.jar approval-service.jar

ENV JAVA_OPTS="" \
                	SERVER_PORT=6400 \
                	MYSQL_HOST=jdbc:mariadb://localhost:3306/approval_service \
                	MYSQL_USER=logistimo \
                	MYSQL_PASSWORD=logistimo \
                	CONVERSATION_SERVICE_URL=http://localhost:8080 \
                	ACTIVEMQ_BROKER_URL=tcp://localhost:61616 \
                	ACTIVEMQ_USER=admin \
                	ACTIVEMQ_PASSWORD=admin \
                	JAVA_AGENT_PORT=6410

RUN bash -c 'touch /app.jar'

ADD jmx_exporter.json /opt/

RUN wget -P /opt/ http://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.7/jmx_prometheus_javaagent-0.7.jar

COPY docker-entrypoint.sh /docker-entrypoint.sh

RUN chmod +x /docker-entrypoint.sh

EXPOSE 6400 6410

CMD ["/docker-entrypoint.sh"]
