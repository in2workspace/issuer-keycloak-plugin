package es.in2.keycloak.model;

import lombok.Getter;
import org.hibernate.annotations.Target;

public enum ErrorType {

	INVALID_REQUEST("invalid_request"),
	INVALID_TOKEN("invalid_token"),
	UNSUPPORTED_CREDENTIAL_TYPE("unsupported_credential_type"),
	INVALID_TX_CODE("invalid_tx_code"),
	UNSUPPORTED_CREDENTIAL_FORMAT("unsupported_credential_format"),
	INVALID_OR_MISSING_PROOF("invalid_or_missing_proof");

	@Getter
	private final String value;

	ErrorType(String value) {
		this.value = value;
	}
}
