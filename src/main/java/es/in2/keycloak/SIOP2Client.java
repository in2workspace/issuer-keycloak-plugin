package es.in2.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import es.in2.keycloak.model.SupportedCredential;

import java.util.List;
import java.util.Map;

/**
 * Pojo, containing all information required to create a VCClient.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SIOP2Client {

	/**
	 * Did of the target/client, will be used as client-id
	 */
	@JsonProperty("client_did")
	private String clientDid;
	/**
	 * Comma-separated list of supported credentials types
	 */
	@JsonProperty("supported_VC_Types")
	private List<SupportedCredential> supportedVCTypes;
	/**
	 * Description of the client, will f.e. be displayed in the admin-console
	 */
	@JsonProperty("description")
	private String description;
	/**
	 * Human-readable name of the client
	 */
	@JsonProperty("name")
	private String name;
	/**
	 * Expiry for the credentials to be created.
	 * Be aware: this used the non-primitive long to stay nullable.
	 */
	@JsonProperty("expiry_in_min")
	private Long expiryInMin;
	/**
	 * A map of additional claims that will be provided within the generated VC.
	 */
	@JsonProperty("additional_claims")
	private Map<String, String> additionalClaims;
}
