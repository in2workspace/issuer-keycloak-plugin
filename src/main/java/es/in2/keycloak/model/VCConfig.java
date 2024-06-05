package es.in2.keycloak.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VCConfig {
	@JsonProperty("issuer_did")
	private String issuerDid;
	@JsonProperty("subject_did")
	private String subjectDid;
	@JsonProperty("proof_type")
	private String proofType;
	@JsonProperty("expiration_date")
	private String expirationDate;

}


