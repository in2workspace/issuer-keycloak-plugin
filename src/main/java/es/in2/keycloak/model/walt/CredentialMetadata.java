package es.in2.keycloak.model.walt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

// for waltId compatibility
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CredentialMetadata {
    @JsonProperty("formats")
	private Map<String, FormatObject> formats;
    @JsonProperty("display")
	private List<CredentialDisplay> display;
}
