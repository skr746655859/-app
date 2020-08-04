FROM openjdk:8
MAINTAINER SalHe <SalHe@qq.com>
RUN mkdir /usr/app
ADD demo-0.0.1-SNAPSHOT.jar /usr/app
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]