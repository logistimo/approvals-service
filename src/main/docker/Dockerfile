#  Copyright © 2017 Logistimo - All Rights Reserved
#
#  Unauthorized copying of this file, via any medium is strictly prohibited.
#
#  Proprietary and confidential.

FROM openjdk:8-jre

MAINTAINER  dockers@logistimo.com

VOLUME /tmp

ARG APP_NAME

ARG APP_VERSION

EXPOSE 6400

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
                	TASK_MACHINE=true \
                	SERVICE_NAME=logi-approvals \
                    APM_SERVER_URL=localhost:8200 \
                	JAVA_AGENT_PORT=6409

RUN bash -c 'touch /app.jar'

ADD jmx_exporter.json /opt/

RUN cd /opt/ && wget http://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.7/jmx_prometheus_javaagent-0.7.jar \
	&& wget http://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/0.6.0/elastic-apm-agent-0.6.0.jar

COPY docker-entrypoint.sh /docker-entrypoint.sh

RUN chmod +x /docker-entrypoint.sh

CMD ["/docker-entrypoint.sh"]
