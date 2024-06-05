package es.in2.keycloak.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class Grant {
    @JsonProperty("pre-authorized_code")
    private final String preAuthorizedCode;

    @JsonProperty("tx_code")
    private final TxCode txCode;

    public Grant(@JsonProperty("pre-authorized_code") String preAuthorizedCode,
                 @JsonProperty("tx_code") TxCode txCode) {
        this.preAuthorizedCode = preAuthorizedCode;
        this.txCode = txCode;
    }

    @Builder
    public static class TxCode {
        @JsonProperty("length")
        private final int length;

        @JsonProperty("input_mode")
        private final String inputMode;

        @JsonProperty("description")
        private final String description;

        public TxCode(@JsonProperty("length") int length,
                      @JsonProperty("input_mode") String inputMode,
                      @JsonProperty("description") String description) {
            this.length = length;
            this.inputMode = inputMode;
            this.description = description;
        }
    }
}