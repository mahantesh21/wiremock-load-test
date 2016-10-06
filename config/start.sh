#!/bin/bash


java -Xmx2500m -Xms2500m -XX:+PrintCommandLineFlags -XX:+PrintGCDateStamps \
       -XX:+PrintGCTimeStamps \
       -XX:+PrintGCDetails \
       -XX:+PrintTenuringDistribution \
       -Djava.util.logging.config.file=logging.properties \
       -jar wmock-extn-1.0-SNAPSHOT-jar-with-dependencies.jar

