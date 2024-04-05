package es.in2.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fiware.keycloak.oidcvc.model.FormatVO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportedCredential {
	@JsonProperty("type")
	private String type;
	@JsonProperty("format")
	private FormatVO format;
}
