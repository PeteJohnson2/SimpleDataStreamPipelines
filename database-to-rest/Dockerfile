FROM eclipse-temurin:25-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD target/$JAR_FILE /app.jar
ENV JAVA_OPTS="-XX:+UseSerialGC -XX:MaxRAMPercentage=60.0 -XX:MaxMetaspaceSize=160m -XX:MaxDirectMemorySize=64m -XX:+ExitOnOutOfMemoryError"
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar