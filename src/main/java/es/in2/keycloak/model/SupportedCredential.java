package es.in2.keycloak.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fiware.keycloak.oidcvc.model.FormatVO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportedCredential {

	private String type;
	private FormatVO format;
}
