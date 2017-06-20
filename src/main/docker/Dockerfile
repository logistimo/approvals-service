FROM openjdk:8-jre

MAINTAINER Nitisha Khandelwal <nitisha@logistimo.com>

VOLUME /tmp

EXPOSE 6400

ADD approval-service-0.1.0.jar approval-service.jar

ENV JAVA_OPTS="" \
                	SERVER_PORT=6400 \
                	MYSQL_HOST=jdbc:mariadb://localhost:3306/approval_service \
                	MYSQL_USER=root \
                	MYSQL_PASS= \
                	CONVERSATION_SERVICE_URL=http://localhost:8080 \
                	ACTIVEMQ_BROKER_URL=tcp://localhost:61616 \
                	ACTIVEMQ_USER=admin \
                	ACTIVEMQ_PASSWORD=admin \
                	JAVA_AGENT_PORT=6409

RUN bash -c 'touch /app.jar'

ADD jmx_exporter.json /opt/

RUN cd /opt/ && wget http://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.7/jmx_prometheus_javaagent-0.7.jar

COPY docker-entrypoint.sh /docker-entrypoint.sh

RUN chmod +x /docker-entrypoint.sh

CMD ["/docker-entrypoint.sh"]
