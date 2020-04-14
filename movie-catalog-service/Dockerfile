FROM azul/zulu-openjdk-alpine:11-jre

LABEL author="Dhiego Duarte"
LABEL email="Dhiego.Duarte.terceiros@claro.com.br"
LABEL company="Claro BR"
# same version in jar file 
LABEL version="1.2.0.RELEASE" 

RUN addgroup -g 3000 movie-catalog-service-group

RUN adduser -D -g '' -G movie-catalog-service-group -u 1000 movie-catalog-service-user

USER movie-catalog-service-user

WORKDIR /home/movie-catalog-service-user

COPY --chown=movie-catalog-service-user:movie-catalog-service-group target/movie-catalog-service-1.2.0.RELEASE.jar /home/movie-catalog-service-user

EXPOSE 8081

ENTRYPOINT ["/usr/bin/java", "-jar"]

CMD ["movie-catalog-service-1.2.0.RELEASE.jar"]
