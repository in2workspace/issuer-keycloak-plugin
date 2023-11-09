package es.in2.keycloak;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import es.in2.keycloak.model.walt.CredentialMetadata;
import es.in2.keycloak.model.walt.FormatObject;
import es.in2.keycloak.model.walt.IssuerDisplay;
import es.in2.keycloak.model.walt.ProofType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import liquibase.pro.packaged.S;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import es.in2.keycloak.model.ErrorResponse;
import es.in2.keycloak.model.ErrorType;
import es.in2.keycloak.model.Role;
import es.in2.keycloak.model.SupportedCredential;
import es.in2.keycloak.model.TokenResponse;
import es.in2.keycloak.model.VCClaims;
import es.in2.keycloak.model.VCConfig;
import es.in2.keycloak.model.VCData;
import es.in2.keycloak.model.VCRequest;
import es.in2.keycloak.model.walt.CredentialDisplay;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.fiware.keycloak.oidcvc.model.CredentialIssuerVO;
import org.fiware.keycloak.oidcvc.model.CredentialRequestVO;
import org.fiware.keycloak.oidcvc.model.CredentialResponseVO;
import org.fiware.keycloak.oidcvc.model.CredentialsOfferVO;
import org.fiware.keycloak.oidcvc.model.DisplayObjectVO;
import org.fiware.keycloak.oidcvc.model.ErrorResponseVO;
import org.fiware.keycloak.oidcvc.model.FormatVO;
import org.fiware.keycloak.oidcvc.model.PreAuthorizedVO;
import org.fiware.keycloak.oidcvc.model.ProofTypeVO;
import org.fiware.keycloak.oidcvc.model.ProofVO;
import org.fiware.keycloak.oidcvc.model.SupportedCredentialVO;
import org.jboss.logging.Logger;
import org.keycloak.OAuth2Constants;
import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.common.util.Time;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.AuthenticatedClientSessionModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.OIDCWellKnownProvider;
import org.keycloak.protocol.oidc.TokenManager;
import org.keycloak.protocol.oidc.utils.OAuth2Code;
import org.keycloak.protocol.oidc.utils.OAuth2CodeParser;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.JsonWebToken;
import org.keycloak.services.ErrorResponseException;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.util.DefaultClientSessionContext;
import org.keycloak.urls.UrlType;
import twitter4j.JSONArray;
import twitter4j.JSONObject;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.ByteBuffer;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static es.in2.keycloak.SIOP2ClientRegistrationProvider.VC_TYPES_PREFIX;

/**
 * Real-Resource to provide functionality for issuing VerfiableCredentials to users, depending on there roles in
 * registered SIOP-2 clients
 */
public class VCIssuerRealmResourceProvider implements RealmResourceProvider {

	private static final Logger LOGGER = Logger.getLogger(VCIssuerRealmResourceProvider.class);
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME
			.withZone(ZoneId.of(ZoneOffset.UTC.getId()));

	public static final String LD_PROOF_TYPE = "LD_PROOF";
	public static final String CREDENTIAL_PATH = "credential";
	public static final String TYPE_VERIFIABLE_CREDENTIAL = "VerifiableCredential";
	public static final String GRANT_TYPE_PRE_AUTHORIZED_CODE = "urn:ietf:params:oauth:grant-type:pre-authorized_code";

	private final KeycloakSession session;
	private final String issuerDid;
	private final AppAuthManager.BearerTokenAuthenticator bearerTokenAuthenticator;
	private final WaltIdClient waltIdClient;
	private final ObjectMapper objectMapper;
	private final Clock clock;

	public VCIssuerRealmResourceProvider(KeycloakSession session, String issuerDid, WaltIdClient waltIdClient,
										 AppAuthManager.BearerTokenAuthenticator authenticator,
										 ObjectMapper objectMapper, Clock clock) {
		this.session = session;
		this.issuerDid = issuerDid;
		this.waltIdClient = waltIdClient;
		this.bearerTokenAuthenticator = authenticator;
		this.objectMapper = objectMapper;
		this.clock = clock;
	}

	@Override
	public Object getResource() {
		return this;
	}

	@Override
	public void close() {
		// no specific resources to close.
	}

	private UserModel getUserModel(ErrorResponseException errorResponse) {
		return getAuthResult(errorResponse).getUser();
	}

	private UserSessionModel getUserSessionModel() {
		return getAuthResult(new ErrorResponseException(getErrorResponse(ErrorType.INVALID_TOKEN))).getSession();
	}

	private AuthenticationManager.AuthResult getAuthResult() {
		return getAuthResult(new ErrorResponseException(getErrorResponse(ErrorType.INVALID_TOKEN)));
	}

	private AuthenticationManager.AuthResult getAuthResult(ErrorResponseException errorResponse) {
		AuthenticationManager.AuthResult authResult = bearerTokenAuthenticator.authenticate();
		if (authResult == null) {
			throw errorResponse;
		}
		return authResult;
	}

	private UserModel getUserModel() {
		return getUserModel(new ErrorResponseException(getErrorResponse(ErrorType.INVALID_TOKEN)));
	}

	private void assertIssuerDid(String requestedIssuerDid) {
		if (!requestedIssuerDid.equals(issuerDid)) {
			throw new ErrorResponseException("not_found", "No such issuer exists.", Response.Status.NOT_FOUND);
		}
	}

	private Response getErrorResponse(ErrorType errorType) {
		return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(errorType.getValue())).build();
	}

	//TODO set to IN2 fields
	@GET
	@Path("{issuer-did}/.well-known/openid-credential-issuer")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Return the issuer metadata", notes = "https://openid.net/specs/openid-4-verifiable-credential-issuance-1_0.html#name-credential-issuer-metadata-", tags = {})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The credentials issuer metadata", response = CredentialIssuerVO.class) })
	public Response getIssuerMetadata(@PathParam("issuer-did") String issuerDidParam) {
		LOGGER.info("Retrieve issuer meta data");
		assertIssuerDid(issuerDidParam);
		KeycloakContext currentContext = session.getContext();
		String authorizationEndpointPattern = "%s/.well-known/openid-configuration";

		return Response.ok().entity(new CredentialIssuerVO()
						.credentialIssuer(getIssuer())
						.authorizationServer(String.format(authorizationEndpointPattern, getIssuer()))
						.credentialEndpoint(getCredentialEndpoint())
						.credentialsSupported(getSupportedCredentials(currentContext)))
				.header("Access-Control-Allow-Origin", "*").build();
	}

	private String getCredentialEndpoint() {

		return getIssuer() + "/" + CREDENTIAL_PATH;
	}

	private String getIssuer() {
		return String.format("%s/%s/%s", getRealmResourcePath(),
				VCIssuerRealmResourceProviderFactory.ID,
				issuerDid);
	}

	private List<SupportedCredentialVO> getSupportedCredentials(KeycloakContext context) {
		return context.getRealm().getClientsStream()
				.flatMap(cm -> cm.getAttributes().entrySet().stream())
				.filter(entry -> entry.getKey().startsWith(VC_TYPES_PREFIX))
				.flatMap(entry -> mapAttributeEntryToScVO(entry).stream())
				.collect(Collectors.toList());

	}

	private List<SupportedCredentialVO> mapAttributeEntryToScVO(Map.Entry<String, String> typesEntry) {
		String type = typesEntry.getKey().replaceFirst(VC_TYPES_PREFIX, "");
		Set<FormatVO> supportedFormats = getFormatsFromString(typesEntry.getValue());
		return supportedFormats.stream().map(formatVO -> {
					String id = buildIdFromType(formatVO, type);
					return new SupportedCredentialVO().id(id).types(List.of(type)).format(formatVO);
				}
		).collect(Collectors.toList());
	}

	private Set<FormatVO> getFormatsFromString(String formatString) {
		return Arrays.stream(formatString.split(",")).map(FormatVO::fromString).collect(Collectors.toSet());
	}

	private String buildIdFromType(FormatVO formatVO, String type) {
		return String.format("%s_%s", type, formatVO.toString());
	}

	private String getRealmResourcePath() {
		KeycloakContext currentContext = session.getContext();
		String realm = currentContext.getRealm().getId();
		String backendUrl = currentContext.getUri(UrlType.BACKEND).getBaseUri().toString();
		if (backendUrl.endsWith("/")) {
			return String.format("%srealms/%s", backendUrl, realm);
		}
		return String.format("%s/realms/%s", backendUrl, realm);
	}

	//TODO return only preauth code
	@GET
	@Path("{issuer-did}/credential-offer")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCredentialOffer(@PathParam("issuer-did") String issuerDidParam,
									   @QueryParam("type") String vcType, @QueryParam("format") FormatVO format) {

		LOGGER.infof("Get an offer for %s - %s", vcType, format);
		assertIssuerDid(issuerDidParam);
		// workaround to support implementations not differentiating json & json-ld
		if (format == FormatVO.JWT_VC) {
			// validate that the user is able to get the offered credentials
			getClientsOfType(vcType, FormatVO.JWT_VC_JSON);
		} else {
			getClientsOfType(vcType, format);
		}

		SupportedCredential offeredCredential = new SupportedCredential(vcType, format);
		Instant now = clock.instant();
		JsonWebToken token = new JsonWebToken()
				.id(UUID.randomUUID().toString())
				.subject(getUserModel().getId())
				.nbf(now.getEpochSecond())
				//maybe configurable in the future, needs to be short lived
				.exp(now.plus(Duration.of(300, ChronoUnit.SECONDS)).getEpochSecond());
		token.setOtherClaims("offeredCredential", new SupportedCredential(vcType, format));

		CredentialsOfferVO theOffer = new CredentialsOfferVO()
				.credentialIssuer(getIssuer())
				.credentials(List.of(offeredCredential))
				.grants(new PreAuthorizedVO().preAuthorizedCode(generateAuthorizationCode()).userPinRequired(false));
		LOGGER.infof("Responding with offer: %s", theOffer);
		return Response.ok()
				.entity(theOffer)
				.header("Access-Control-Allow-Origin", "*")
				.build();

	}

	public String generateAuthorizationCode() {

		AuthenticationManager.AuthResult authResult = getAuthResult();
		UserSessionModel userSessionModel = getUserSessionModel();

		AuthenticatedClientSessionModel clientSessionModel = userSessionModel.
				getAuthenticatedClientSessionByClient(
						authResult.getClient().getId());
		int expiration = Time.currentTime() + getUserSessionModel().getRealm().getAccessCodeLifespan();

		String codeId = UUID.randomUUID().toString();
		String nonce = UUID.randomUUID().toString();
		OAuth2Code oAuth2Code = new OAuth2Code(codeId, expiration, nonce, null, null, null, null);

		return OAuth2CodeParser.persistCode(session, clientSessionModel, oAuth2Code);
	}

	//TODO check IN2 changes
	@POST
	@Path("{issuer-did}/token")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response exchangeToken(@PathParam("issuer-did") String issuerDidParam,
								  @FormParam("grant_type") String grantType, @FormParam("code") String code,
								  @FormParam("pre-authorized_code") String preauth) {
		assertIssuerDid(issuerDidParam);
		LOGGER.infof("Received token request %s - %s - %s.", grantType, code, preauth);

		if (!grantType.equals(GRANT_TYPE_PRE_AUTHORIZED_CODE)) {
			throw new ErrorResponseException(getErrorResponse(ErrorType.INVALID_TOKEN));
		}
		// some (not fully OIDC4VCI compatible) wallets send the preauthorized code as an alternative parameter
		String codeToUse = Optional.ofNullable(code).orElse(preauth);

		EventBuilder eventBuilder = new EventBuilder(session.getContext().getRealm(), session,
				session.getContext().getConnection());
		OAuth2CodeParser.ParseResult result = OAuth2CodeParser.parseCode(session, codeToUse,
				session.getContext().getRealm(),
				eventBuilder);
		if (result.isExpiredCode() || result.isIllegalCode()) {
			throw new ErrorResponseException(getErrorResponse(ErrorType.INVALID_TOKEN));
		}
		AccessToken accessToken = new TokenManager().createClientAccessToken(session,
				result.getClientSession().getRealm(),
				result.getClientSession().getClient(),
				result.getClientSession().getUserSession().getUser(),
				result.getClientSession().getUserSession(),
				DefaultClientSessionContext.fromClientSessionAndScopeParameter(result.getClientSession(),
						OAuth2Constants.SCOPE_OPENID, session));

		String encryptedToken = session.tokens().encodeAndEncrypt(accessToken);
		LOGGER.infof("Successfully returned the token: %s.", encryptedToken);
		String tokenType = "bearer";
		long expiresIn = accessToken.getExp() - Time.currentTime();
		List<String> response = sendAccessTokenToIssuerToGetNonce(encryptedToken);
		String nonce = response.get(0);
		long nonceExpiresIn = Long.parseLong(response.get(1));

		return Response.ok().entity(new TokenResponse(encryptedToken, tokenType, expiresIn, nonce, nonceExpiresIn))
				.header("Access-Control-Allow-Origin", "*")
				.build();
	}


	// since we cannot know the address of the requesting wallets in advance, we have to accept all origins.
	@OPTIONS
	@Path("{any: .*}")
	public Response optionCorsResponse() {
		return Response.ok().header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST,GET,OPTIONS")
				.header("Access-Control-Allow-Headers", "Content-Type,Authorization")
				.build();
	}

	@NotNull
	private List<ClientModel> getClientsOfType(String vcType, FormatVO format) {
		LOGGER.debugf("Retrieve all clients of type %s, supporting format %s", vcType, format.toString());
		if (format == FormatVO.JWT_VC) {
			// backward compat
			format = FormatVO.JWT_VC_JSON;
		}
		String formatString = format.toString();
		Optional.ofNullable(vcType).filter(type -> !type.isEmpty()).orElseThrow(() -> {
			LOGGER.info("No VC type was provided.");
			return new ErrorResponseException("no_type_provided",
					"No VerifiableCredential-Type was provided in the request.",
					Response.Status.BAD_REQUEST);
		});

		String prefixedType = String.format("%s%s", VC_TYPES_PREFIX, vcType);
		LOGGER.infof("Looking for client supporting %s with format %s", prefixedType, formatString);
		List<ClientModel> vcClients = getClientModelsFromSession().stream()
				.filter(clientModel -> Optional.ofNullable(clientModel.getAttributes())
						.filter(attributes -> attributes.containsKey(prefixedType))
						.filter(attributes -> Arrays.asList(attributes.get(prefixedType).split(","))
								.contains(formatString))
						.isPresent())
				.collect(Collectors.toList());

		if (vcClients.isEmpty()) {
			LOGGER.infof("No SIOP-2-Client supporting type %s registered.", vcType);
			throw new ErrorResponseException(getErrorResponse(ErrorType.UNSUPPORTED_CREDENTIAL_TYPE));
		}
		return vcClients;
	}

	@NotNull
	private List<ClientModel> getClientModelsFromSession() {
		return session.clients().getClientsStream(session.getContext().getRealm())
				.filter(clientModel -> clientModel.getProtocol() != null)
				.filter(clientModel -> clientModel.getProtocol().equals(SIOP2LoginProtocolFactory.PROTOCOL_ID))
				.collect(Collectors.toList());
	}

	/**
	*	Obtains the environment variable ISSUER_API_URL from the docker-compose environment
	*/
	private static String getIssuerUrl() {
		return System.getenv("ISSUER_API_URL");
	}

	public static List<String> sendAccessTokenToIssuerToGetNonce(String accessToken){
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			List<String> nonceList = new ArrayList<>();

			HttpPost httpPost = new HttpPost(getIssuerUrl() + "/api/nonce");

			String jsonBody = "{\"accessToken\":\"" + accessToken + "\"}";
			StringEntity entity = new StringEntity(jsonBody);
			httpPost.setEntity(entity);

			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("Authorization", "Bearer " + accessToken);

			try (CloseableHttpResponse response = client.execute(httpPost)) {
				LOGGER.debug(response.toString());
				String responseBody = response.getEntity() != null ?
						EntityUtils.toString(response.getEntity()) : "";
				JSONObject jsonResponse = new JSONObject(responseBody);
				String nonce = jsonResponse.optString("nonce");
				String nonceExpiresIn = jsonResponse.optString("nonce_expires_in");
				nonceList.add(nonce);
				nonceList.add(nonceExpiresIn);
			}
			return nonceList;
		} catch (Exception e) {
			throw new ErrorResponseException("Communication failed", "Error sending data to issuer",
					Response.Status.BAD_REQUEST);
		}
	}
}
