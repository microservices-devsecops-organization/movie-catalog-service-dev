# FROM openjdk:8u212-jdk-alpine3.9
FROM amd64/openjdk:8-jre-alpine3.9

LABEL author="Dhiego Duarte"
LABEL email="Dhiego.Duarte.terceiros@claro.com.br"
LABEL company="Claro BR"
# same version in jar file
LABEL version="0.0.001-SNAPSHOT" 

RUN apk --no-cache add -u musl=1.1.20-r5 \
    && apk --no-cache add -u libtasn1=4.14-r0 \
    && apk --no-cache add libjpeg-turbo=1.5.3-r5

RUN addgroup -g 3000 movie-catalog-service-group
RUN adduser -D -g '' -G movie-catalog-service-group -u 1000 movie-catalog-service-user

USER movie-catalog-service-user

WORKDIR /home/movie-catalog-service-user

COPY --chown=movie-catalog-service-user:movie-catalog-service-group target/movie-catalog-service-0.0.001-SNAPSHOT.jar /home/movie-catalog-service-user

EXPOSE 8081

ENTRYPOINT ["/usr/bin/java", "-jar"]

CMD ["movie-catalog-service-0.0.001-SNAPSHOT.jar"]