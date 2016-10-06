FROM  openjdk:8-jre
RUN apt-get -y update
RUN apt-get -y install lsof

COPY target/wmock-extn-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp
COPY src/main/resources/mappings/ /tmp/mappings
COPY config/start.sh /tmp/start.sh
RUN chmod 755 /tmp/start.sh
#RUN chown -R nobody /tmp
WORKDIR /tmp
EXPOSE 8080
#USER nobody
CMD /tmp/start.sh
