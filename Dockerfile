FROM openjdk:17-alpine

#ichaoge
MAINTAINER ichaoge

RUN mkdir -p /server/conf /server/logs

COPY ./target/cim-server-1.0.0.jar /server/cim-server.jar

EXPOSE 8085
EXPOSE 23456
EXPOSE 34567

ENTRYPOINT ["java","-jar","/server/cim-server.jar"]