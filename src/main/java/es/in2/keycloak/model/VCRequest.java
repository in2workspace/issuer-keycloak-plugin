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
public class VCRequest {
	@JsonProperty("template_id")
	private String templateId;
	@JsonProperty("config")
	private VCConfig config;
	@JsonProperty("credential_data")
	private VCData credentialData;
}
