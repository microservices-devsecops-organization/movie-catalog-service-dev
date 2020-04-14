FROM azul/zulu-openjdk-alpine:11-jre

LABEL author="Dhiego Duarte"
LABEL email="Dhiego.Duarte.terceiros@claro.com.br"
LABEL company="Claro BR"
# same version in jar file 
LABEL version="1.2.0.RELEASE" 

RUN addgroup -g 3000 movie-catalog-service-sidecar-fatura-group

RUN adduser -D -g '' -G movie-catalog-service-sidecar-fatura-group -u 1000 movie-catalog-service-sidecar-fatura-user

USER movie-catalog-service-sidecar-fatura-user

WORKDIR /home/movie-catalog-service-sidecar-fatura-user

COPY --chown=movie-catalog-service-sidecar-fatura-user:movie-catalog-service-sidecar-fatura-group target/movie-catalog-service-sidecar-fatura-1.2.0.RELEASE.jar /home/movie-catalog-service-sidecar-fatura-user

EXPOSE 8084

ENTRYPOINT ["/usr/bin/java", "-jar"]

CMD ["movie-catalog-service-sidecar-fatura-1.2.0.RELEASE.jar"]
