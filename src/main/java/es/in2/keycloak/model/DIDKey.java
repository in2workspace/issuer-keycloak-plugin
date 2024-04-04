package es.in2.keycloak.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DIDKey {

	private String kty;
	private String d;
	private String use;
	private String crv;
	private String kid;
	private String x;
	private String alg;
}
