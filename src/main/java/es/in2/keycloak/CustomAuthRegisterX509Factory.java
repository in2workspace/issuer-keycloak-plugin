package es.in2.keycloak;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.quarkus.runtime.Environment;


import java.util.List;

import static org.keycloak.provider.ProviderConfigProperty.STRING_TYPE;

@Slf4j
public class CustomAuthRegisterX509Factory implements AuthenticatorFactory {

    public static final String PROVIDER_ID = "in2-x509-authenticator";
    @Getter
    private static final CustomAuthRegisterX509 SINGLETON = new CustomAuthRegisterX509();

    public static final String PROP_IN2_TEMPLATE = "in2x509.template.login";
    @Override
    public String getDisplayType() {
        return "IN2 External Authenticator";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[0];
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "External Authenticator provided by IN2";
    }

    public static final List<ProviderConfigProperty> configProperties = List.of(
            new ProviderConfigProperty(PROP_IN2_TEMPLATE,
                                       "IN2 FTL page template name",
                                       "Will be used as the template for email link page",
                                       STRING_TYPE, "in2-login.ftl")
    );
    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return SINGLETON;
    }

    @Override
    public void init(Config.Scope config) {
        String profile = Environment.getProfile();
        log.info("Initializing External Authenticator, profile: {}", profile);
        log.info("Scope properties: {}", config.getPropertyNames());
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        log.info("Post-Initializing External Authenticator");
    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
