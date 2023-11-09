package es.in2.keycloak;

public class VCIssuerException extends RuntimeException {

	public VCIssuerException(String message) {
		super(message);
	}

	public VCIssuerException(String message, Throwable cause) {
		super(message, cause);
	}
}
