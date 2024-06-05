package es.in2.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import es.in2.keycloak.model.KeyId;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.util.Optional;

/**
 * Factory implementation to provide the VCIssuer functionality as a realm resource.
 */
@AutoService(RealmResourceProviderFactory.class)
public class VCIssuerRealmResourceProviderFactory implements RealmResourceProviderFactory {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	private static final Logger LOGGER = Logger.getLogger(VCIssuerRealmResourceProviderFactory.class);
	public static final String ID = "verifiable-credential";
	private static final String ISSUER_DID_ENV_VAR = "VCISSUER_ISSUER_DID";
	private static final String ISSUER_DID_KEY_FILE_ENV_VAR = "VCISSUER_ISSUER_KEY_FILE";
	private String issuerDid;

	@Override
	public RealmResourceProvider create(KeycloakSession keycloakSession) {
		LOGGER.debug("Create vc-issuer resource provider");

		return new VCIssuerRealmResourceProvider(
				keycloakSession,
				issuerDid,
				new AppAuthManager.BearerTokenAuthenticator(
						keycloakSession),
				Clock.systemUTC());
	}

	@Override
	public void init(Config.Scope config) {
		try {
			LOGGER.info("Starting to initialization of issuer and key.");
			// import the issuer key, if present.
			Optional<String> keyId = importIssuerKey();
			keyId.ifPresentOrElse(k -> LOGGER.infof("Imported key %s.", keyId),
					() -> LOGGER.warnf("No key was imported."));

			//DG
			issuerDid = System.getenv(ISSUER_DID_ENV_VAR);
		} catch (WaltIdConnectException waltIdConnectException) {
			LOGGER.error("Was not able to initialize the issuer did. Issuing VCs is not available.",
					waltIdConnectException);
		}
	}

	private Optional<String> importIssuerKey() {

		Optional<String> keyFileEnv = Optional.ofNullable(System.getenv(ISSUER_DID_KEY_FILE_ENV_VAR));
		if (keyFileEnv.isEmpty()) {
			LOGGER.info("No keyfile is provided, skip key import.");
			return Optional.empty();
		}

		File keyFile = new File(keyFileEnv.get());
		if (!keyFile.exists()) {
			LOGGER.warnf("Despite being configured, no keyfile exists at %s. Skip import.", keyFileEnv.get());
			return Optional.empty();
		}else{
			try {
				return Optional.ofNullable(OBJECT_MAPPER.readValue(keyFile, KeyId.class).getId());
			} catch (IOException e) {
				LOGGER.warnf("The keyfile %s is not a valid key. Skip import.", keyFileEnv.get(), e);
				return Optional.empty();
			}
		}
	}

	@Override
	public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
		// nothing to do here.
	}

	@Override
	public void close() {
		// specific resources to be closed
	}

	@Override
	public String getId() {
		return ID;
	}
}
