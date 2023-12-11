# Primera etapa: Compilación de la aplicación
FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
COPY /api/api.yaml ./api/
RUN mvn clean install


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
EXPOSE 8088
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--health-enabled=true","--metrics-enabled=true", "--log-level DEBUG", "--import-realm"]