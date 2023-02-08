FROM openjdk:17-alpine

MAINTAINER ichaoge

WORKDIR /ichaoge

COPY ./cim-server-1.0.0.jar ./cim-server.jar

EXPOSE 8085
EXPOSE 23456
EXPOSE 34567

ENTRYPOINT ["java","-jar","cim-server.jar"]