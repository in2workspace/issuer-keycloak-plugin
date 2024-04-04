package es.in2.keycloak.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VCClaims {
	private String firstName;
	private String familyName;
	private String email;
	private Set<Role> roles;

	private Map<String, String> additionalClaims;

	@JsonAnyGetter
	public Map<String, String> getAdditionalClaims() {
		return additionalClaims;
	}
}
