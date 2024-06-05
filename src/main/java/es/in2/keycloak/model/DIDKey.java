package es.in2.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DIDKey {
	@JsonProperty("kty")
	private String kty;
	@JsonProperty("d")
	private String d;
	@JsonProperty("use")
	private String use;
	@JsonProperty("crv")
	private String crv;
	@JsonProperty("kid")
	private String kid;
	@JsonProperty("x")
	private String x;
	@JsonProperty("alg")
	private String alg;
}
