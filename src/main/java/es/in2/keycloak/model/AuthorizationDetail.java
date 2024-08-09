package es.in2.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AuthorizationDetail {
    @JsonProperty("type") String type;
    @JsonProperty("credential_configuration_id") String credentialConfigurationId;
}
