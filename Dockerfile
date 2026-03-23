FROM swr.cn-north-4.myhuaweicloud.com/ddn-k8s/docker.io/library/openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080