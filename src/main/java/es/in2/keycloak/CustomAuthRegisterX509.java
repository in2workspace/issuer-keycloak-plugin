package es.in2.keycloak;

import antlr.ASTFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import es.in2.keycloak.model.EidasUser;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.AuthenticationFlowException;
import org.keycloak.authentication.authenticators.util.AuthenticatorUtils;
import org.keycloak.authentication.authenticators.x509.CertificateValidator;
import org.keycloak.authentication.authenticators.x509.X509AuthenticatorConfigModel;
import org.keycloak.authentication.authenticators.x509.X509ClientCertificateAuthenticator;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.keycloak.models.utils.FormMessage;

import javax.security.auth.x500.X500Principal;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CustomAuthRegisterX509 extends X509ClientCertificateAuthenticator {

    CustomAuthRegisterX509() {
        log.info("Creating CustomAuthRegisterX509");
    }
    @Override
    public void authenticate(AuthenticationFlowContext context) {
        try {
            this.dumpContainerAttributes(context);
            X509Certificate[] certs = this.getCertificateChain(context);
            if (certs == null || certs.length == 0) {
                logger.debug("[X509ClientCertificateAuthenticator:authenticate] x509 client certificate is not available for mutual SSL.");
                context.attempted();
                return;
            }

            this.saveX509CertificateAuditDataToAuthSession(context, certs[0]);
            this.recordX509CertificateAuditDataViaContextEvent(context);
            X509AuthenticatorConfigModel config = null;
            if (context.getAuthenticatorConfig() != null && context.getAuthenticatorConfig().getConfig() != null) {
                config = new X509AuthenticatorConfigModel(context.getAuthenticatorConfig());
            }

            if (config == null) {
                logger.warn("[X509ClientCertificateAuthenticator:authenticate] x509 Client Certificate Authentication configuration is not available.");
                context.challenge(this.createInfoResponse(context, "X509 client authentication has not been configured yet", new Object[0]));
                context.attempted();
                return;
            }

            String errorMessage;
            try {
                CertificateValidator.CertificateValidatorBuilder builder = this.certificateValidationParameters(context.getSession(), config);
                CertificateValidator validator = builder.build(certs);
                validator.checkRevocationStatus().validateTrust().validateKeyUsage().validateExtendedKeyUsage().validatePolicy().validateTimestamps();
            } catch (Exception var9) {
                logger.error(var9.getMessage(), var9);
                errorMessage = "Certificate validation's failed.";
                context.challenge(this.createErrorResponse(context, certs[0].getSubjectDN().getName(), errorMessage, "Certificate revoked or incorrect."));
                context.attempted();
                return;
            }

            Object userIdentity = this.getUserIdentityExtractor(config).extractUserIdentity(certs);
            X509Certificate[] userCertificate = certs;
            if (userIdentity == null) {
                context.getEvent().error("invalid_user_credentials");
                logger.warnf("[X509ClientCertificateAuthenticator:authenticate] Unable to extract user identity from certificate.", new Object[0]);
                errorMessage = "Unable to extract user identity from specified certificate";
                context.challenge(this.createErrorResponse(context, certs[0].getSubjectDN().getName(), errorMessage));
                context.attempted();
                return;
            }

            UserModel user;
            try {
                context.getEvent().detail("username", userIdentity.toString());
                context.getAuthenticationSession().setAuthNote("ATTEMPTED_USERNAME", userIdentity.toString());
                this.getUserIdentityToModelMapper(config).find(context, userIdentity);
                user = this.getUserIdentityToModelMapper(config).find(context, userIdentity);

            } catch (ModelDuplicateException var8) {
                logger.modelDuplicateException(var8);
                errorMessage = "X509 certificate authentication's failed.";
                context.challenge(this.createErrorResponse(context, certs[0].getSubjectDN().getName(), errorMessage, var8.getMessage()));
                context.attempted();
                return;
            }
            if (user == null) {
                this.registerNewUser(context, userCertificate);
//                context.setUser(user);
            } else {
                String bruteForceError;
                if (this.invalidUser(context, user)) {
                    bruteForceError = "X509 certificate authentication's failed.";
                    context.challenge(
                            this.createErrorResponse(context, certs[0].getSubjectDN().getName(), bruteForceError,
                                                     "Invalid user"));
                    context.attempted();
                    return;
                }

                bruteForceError = AuthenticatorUtils.getDisabledByBruteForceEventError(context, user);
                if (bruteForceError != null) {
                    context.getEvent().user(user);
                    context.getEvent().error(bruteForceError);
                    errorMessage = "X509 certificate authentication's failed.";
                    context.challenge(this.createErrorResponse(context, certs[0].getSubjectDN().getName(), errorMessage,
                                                               "Invalid user"));
                    context.attempted();
                    return;
                }

                if (!this.userEnabled(context, user)) {
                    errorMessage = "X509 certificate authentication's failed.";
                    context.challenge(this.createErrorResponse(context, certs[0].getSubjectDN().getName(), errorMessage,
                                                               "User is disabled"));
                    context.attempted();
                    return;
                }
                context.setUser(user);
                if (!config.getConfirmationPageDisallowed()) {
                    context.forceChallenge(this.createSuccessResponseCustom(context, certs[0].getSubjectDN().toString()));
                } else {
                    context.success();
                }
            }

        } catch (Exception var10) {
            logger.errorf("[X509ClientCertificateAuthenticator:authenticate] Exception: %s", var10.getMessage());
            context.attempted();
        }

    }
    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        if (formData.containsKey("cancel")) {
            context.clearUser();
            context.attempted();
        } else if (formData.containsKey("register")) {
                setUserData(context, formData);
                context.success();
        } else if (context.getUser() != null) {

            this.recordX509CertificateAuditDataViaContextEvent(context);

            context.success();
        } else {
            context.attempted();
        }
    }

    private void setUserData(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        // create user
        String email = formData.getFirst("email");
        String cn = formData.getFirst("cn");
        String organization = formData.getFirst("organization");
        String organizationIdentifier = formData.getFirst("organizationIdentifier");
        String myid = formData.getFirst("myid");
        String serialNumber = formData.getFirst("serialNumber");
        String country = formData.getFirst("country");
        String role = formData.getFirst("role");

        UserModel user = context.getSession().users().addUser(context.getRealm(), email);

        user.setEmail(email);
        user.setUsername(email);
        user.setFirstName(formData.getFirst("firstName"));
        user.setLastName(formData.getFirst("lastName"));
        user.setSingleAttribute("displayName", user.getFirstName() + " " + user.getLastName());
        user.setSingleAttribute("myid", myid);
        user.setSingleAttribute("cn", cn);
        user.setSingleAttribute("organizationIdentifier", organizationIdentifier);
        user.setSingleAttribute("o", organization);
        user.setSingleAttribute("c", country);
        user.setSingleAttribute("serialNumber", serialNumber);

        user.grantRole(context.getRealm().getRole(role));

        user.setEnabled(true);

        context.setUser(user);
    }

    private Response createForm(AuthenticationFlowContext context, EidasUser user) {
        //mock dummy user in context.getUser().getUsername()
        context.setUser(context.getSession().users().getUserByUsername(context.getRealm(),"-notExistingUser-"));


        LoginFormsProvider form = context.form();

        form.setAttribute("name", user.getFirstName());
        form.setAttribute("lastName", user.getLastName());
        form.setAttribute("email", user.getEmail());
        form.setAttribute("username", user.getUsername());
        form.setAttribute("myid", user.getMyid());
        form.setAttribute("cn", user.getCn());
        form.setAttribute("organizationIdentifier", user.getOrganizationIdentifier());
        form.setAttribute("organization", user.getOrganization());
        form.setAttribute("country", user.getCountry());
        form.setAttribute("serialNumber", user.getSerialNumber());
        form.setAttribute("role", user.getRolName());

        return form.createForm("in2-login.ftl");
    }

    private void registerNewUser(AuthenticationFlowContext context, X509Certificate[] userIdentity) {

        log.info("Registering new user");
        String name;
        String lastName;
        String email;
        String myid;
        X500Principal principal = userIdentity[0].getSubjectX500Principal();
        log.info("principal: {}", principal.toString());
        name = getName(principal);
        lastName = getLastName(principal);
        String cn = getCN(principal);
        email = getEmail(principal);
        String organizationIdentifier = getOrganizationIdentifier(principal);
        String organization = getOrganization(principal);
        String country = getCountry(principal);
        String serialNumber = getSerialNumber(principal);
        myid = getMyid(userIdentity);
        log.info("name: {}, lastName: {}, serialNumber: {}", name, lastName, myid);

        EidasUser user = EidasUser.builder().build();
        user.setUsername(email);
        user.setFirstName(name);
        user.setLastName(lastName);

        user.setDisplayName(name + " " + lastName);
        user.setMyid(myid);
        user.setCn(cn);
        user.setOrganizationIdentifier(organizationIdentifier);
        user.setOrganization(organization);
        user.setCountry(country);
        user.setSerialNumber(serialNumber);
        user.setEmail(email);

//        user.setEnabled(true);
        if (email != null && !email.isEmpty()) {
            user.setRolName("signer");
        } else {
            user.setRolName("mandator");
        }
        context.challenge(createForm(context, user));

//        return user;
    }

    private String getSerialNumber(X500Principal principal) {
        String serialNumber;
        // Get serial number
        Pattern pattern = Pattern.compile("SERIALNUMBER=([^,]+)");
        Matcher matcher = pattern.matcher(principal.toString());
        serialNumber = matcher.find() ? matcher.group(1) : "";
        return serialNumber;
    }

    private String getCountry(X500Principal principal) {
        String country;
        // Get country
        Pattern pattern = Pattern.compile("C=([^,]+)");
        Matcher matcher = pattern.matcher(principal.toString());
        country = matcher.find() ? matcher.group(1) : "";
        return country;
    }

    private String getOrganization(X500Principal principal) {
        String organization;
        // Get organization
        Pattern pattern = Pattern.compile("O=([^,]+)");
        Matcher matcher = pattern.matcher(principal.toString());
        organization = matcher.find() ? matcher.group(1) : "";
        return organization;
    }

    private String getOrganizationIdentifier(X500Principal principal) {
        String organizationIdentifier;
        // Get organizationIdentifier
        Pattern pattern = Pattern.compile("2.5.4.97=([^,]+)");
        Matcher matcher = pattern.matcher(principal.toString());
        organizationIdentifier = matcher.find() ? matcher.group(1) : "";
        return organizationIdentifier;
    }

    private String getEmail(X500Principal principal) {
        String email;
        // Get email
        Pattern pattern = Pattern.compile("EMAIL=([^,]+)");
        Matcher matcher = pattern.matcher(principal.toString());
        email = matcher.find() ? matcher.group(1) : "";
        return email;
    }

    private String getCN(X500Principal principal) {
        String cn;
        // Get CN
        Pattern pattern = Pattern.compile("CN=([^,]+)");
        Matcher matcher = pattern.matcher(principal.toString());
        cn = matcher.find() ? matcher.group(1) : "";
        return cn;
    }

    @NotNull
    private static String getMyid(X509Certificate[] userIdentity) {
        // Get the raw serial number bytes directly from the certificate.
        byte[] serialNumberBytes = userIdentity[0].getSerialNumber().toByteArray();
        // Convert the byte array to a hexadecimal string.
        StringBuilder hexString = new StringBuilder();
        for (byte b : serialNumberBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    @Nullable
    private static String getLastName(X500Principal principal) {
        Pattern pattern;
        String lastName;
        Matcher matcher;
        // Get last name
        pattern = Pattern.compile("SURNAME=([^,]+)");
        matcher = pattern.matcher(principal.toString());
        lastName = matcher.find() ? matcher.group(1) : null;
        return lastName;
    }

    @Nullable
    private static String getName(X500Principal principal) {
        String name;
        // Get name
        Pattern pattern = Pattern.compile("GIVENNAME=([^,]+)");
        Matcher matcher = pattern.matcher(principal.toString());
        name = matcher.find() ? matcher.group(1) : null;
        return name;
    }

    private boolean userEnabled(AuthenticationFlowContext context, UserModel user) {
        if (!user.isEnabled()) {
            context.getEvent().user(user);
            context.getEvent().error("user_disabled");
            return false;
        } else {
            return true;
        }
    }

    private boolean invalidUser(AuthenticationFlowContext context, UserModel user) {
        if (user == null) {
            context.getEvent().error("user_not_found");
            return true;
        } else {
            return false;
        }
    }
    private void dumpContainerAttributes(AuthenticationFlowContext context) {
        Map<String, Object> attributeNames = context.getSession().getAttributes();
        Iterator var3 = attributeNames.keySet().iterator();

        while(var3.hasNext()) {
            String name = (String)var3.next();
            logger.tracef("[X509ClientCertificateAuthenticator:dumpContainerAttributes] \"%s\"", name);
        }

    }
    private Response createSuccessResponseCustom(AuthenticationFlowContext context, String subjectDN) {
        return this.createResponseCustom(context, subjectDN, true, (String)null, (Object[])null);
    }
    private Response createErrorResponse(AuthenticationFlowContext context, String subjectDN, String errorMessage, String... errorParameters) {
        return this.createResponseCustom(context, subjectDN, false, errorMessage, errorParameters);
    }

    private Response createResponseCustom(AuthenticationFlowContext context, String subjectDN, boolean isUserEnabled, String errorMessage, Object[] errorParameters) {
        LoginFormsProvider form = context.form();
        if (errorMessage != null && errorMessage.trim().length() > 0) {
            List<FormMessage> errors = new LinkedList();
            errors.add(new FormMessage(errorMessage, new Object[0]));
            if (errorParameters != null) {
                Object[] var8 = errorParameters;
                int var9 = errorParameters.length;

                for(int var10 = 0; var10 < var9; ++var10) {
                    Object errorParameter = var8[var10];
                    if (errorParameter != null) {
                        String[] var12 = errorParameter.toString().split("\n");
                        int var13 = var12.length;

                        for (String part : var12) {
                            errors.add(new FormMessage(part, new Object[0]));
                        }
                    }
                }
            }

            form.setErrors(errors);
        }

        MultivaluedMap<String, String> formData = new MultivaluedHashMap();
        formData.add("username", context.getUser() != null ? context.getUser().getUsername() : "unknown user");
        formData.add("subjectDN", subjectDN);
        formData.add("isUserEnabled", String.valueOf(isUserEnabled));
        form.setFormData(formData);
        return form.createX509ConfirmPage();
    }
}
