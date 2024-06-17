package es.in2.keycloak.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EidasUser {

    private String username;
    private String firstName;
    private String lastName;
    private String displayName;
    private String email;
    private String cn;
    private String organization;
    private String country;
    private String serialNumber;
    private String myid;
    private String organizationIdentifier;
    private String rolName;

}
