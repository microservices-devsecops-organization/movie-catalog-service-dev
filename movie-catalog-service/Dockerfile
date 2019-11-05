FROM openjdk:8u212-jdk-alpine3.9

LABEL author="Dhiego Duarte"
LABEL email="Dhiego.Duarte.terceiros@claro.com.br"
LABEL company="Claro BR"
LABEL version="1.0.0"

RUN apk --no-cache add -u musl=1.1.20-r5 \
    && apk --no-cache add -u libtasn1=4.14-r0 \
    && apk --no-cache add libjpeg-turbo=1.5.3-r5

RUN addgroup -g 3000 poc_group
RUN adduser -D -g '' -G poc_group -u 1000 poc_user

USER poc_user
WORKDIR /home/poc_user

# For k8s ConfigMap and Secret
RUN mkdir /home/poc_user/configs && mkdir /home/poc_user/secrets

# sempre alterar versao do jar. Se manter a mesma este layer da imagem docker nao sera atualizado, pegando o layer que ja esta em cache 
# e o jar que foi atualizado nao sera copiado para o container
COPY --chown=poc_user:poc_group target/movie-catalog-service-0.0.110-SNAPSHOT.jar /home/poc_user

EXPOSE 8081

ENTRYPOINT ["/usr/bin/java", "-jar"]

CMD ["movie-catalog-service-0.0.110-SNAPSHOT.jar"]

