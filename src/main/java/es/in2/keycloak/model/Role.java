package es.in2.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {
	@JsonProperty("names")
	private Set<String> names;
	@JsonProperty("target")
	private String target;

}
