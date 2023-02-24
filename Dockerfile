FROM maven:3.8.2-jdk-11 as BUILD

COPY . .

RUN mvn clean package -DskipTests -Pdocker


FROM tomcat:8.5-jdk11-openjdk

COPY --from=BUILD /target/sitio-1.0.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]