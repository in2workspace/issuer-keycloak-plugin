# Primera etapa: Compilación de la aplicación
FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
COPY /api/api.yaml ./api/
RUN mvn clean install
ARG KC_DB
ARG KC_DB_PASSWORD
ARG KC_DB_SCHEMA
ARG KC_DB_URL_HOST
ARG KC_DB_URL_PORT
ARG KC_DB_USERNAME
ENV KEYCLOAK_ADMIN=in2admin
ENV KEYCLOAK_ADMIN_PASSWORD=in2pass
ENV KC_HOSTNAME_URL=http://localhost:8080
ENV KC_HOSTNAME_ADMIN_URL=http://localhost:8080
ENV KC_DB=$KC_DB
ENV KC_DB_URL=$KC_DB_URL_HOST
ENV KC_DB_USERNAME=$KC_DB_USERNAME
ENV KC_DB_PASSWORD=$KC_DB_PASSWORD
ENV DB_PORT=$KC_DB_URL_PORT
ENV KC_DB_SCHEMA=$KC_DB_SCHEMA


# Segunda etapa: Creación de la imagen de Keycloak
FROM quay.io/keycloak/keycloak:20.0.3
# Copiar el artefacto de la aplicación desde la etapa de compilación
COPY --from=builder /app/target/classes/keyfile.json /opt/keycloak/providers/keyfile.json
COPY --from=builder /app/target/in2-issuer-auth-1.0-SNAPSHOT.jar /opt/keycloak/providers/
COPY /imports /opt/keycloak/data/import

#ENV KC_SPI_THEME_ADMIN_DEFAULT=siop-2
ENV VCISSUER_ISSUER_DID="did:key:z6MkqmaCT2JqdUtLeKah7tEVfNXtDXtQyj4yxEgV11Y5CqUa"
ENV VCISSUER_ISSUER_KEY_FILE="/opt/keycloak/providers/keyfile.json"

#ADD ./target/classes/keyfile.json /opt/keycloak/providers/keyfile.json
#ADD ./target/in2-issuer-auth-1.0-SNAPSHOT.jar /opt/keycloak/providers/in2-issuer-auth-1.0-SNAPSHOT.jar

#RUN bash -c 'touch /app/in2-issuer-backend-0.2.0-SNAPSHOT.jar'
COPY applicationinsights-agent-3.4.8.jar  /build/applicationinsights-agent-3.4.8.jar
COPY applicationinsights.json /build/applicationinsights.json
EXPOSE 8080
ENTRYPOINT ["/opt/keycloak/bin/kc.sh","start-dev","--health-enabled=true","--log-level DEBUG","--import-realm"]