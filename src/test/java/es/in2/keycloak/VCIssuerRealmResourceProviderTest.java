package es.in2.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.in2.keycloak.it.SIOP2IntegrationTest;
import es.in2.keycloak.it.model.IssuerMetaData;
import es.in2.keycloak.model.*;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.fiware.keycloak.oidcvc.model.FormatVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.keycloak.models.*;
import org.keycloak.services.managers.AppAuthManager;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static es.in2.keycloak.VCIssuerRealmResourceProvider.LD_PROOF_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@Slf4j
public class VCIssuerRealmResourceProviderTest {

	private static final String ISSUER_DID = "did:key:test";

	private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private KeycloakSession keycloakSession;
	private AppAuthManager.BearerTokenAuthenticator bearerTokenAuthenticator;

	private VCIssuerRealmResourceProvider testProvider;

	private CacheStore<String> cacheStore;

	@BeforeEach
	public void setUp() {
		this.keycloakSession = mock(KeycloakSession.class);
		this.bearerTokenAuthenticator = mock(AppAuthManager.BearerTokenAuthenticator.class);
		this.testProvider = new VCIssuerRealmResourceProvider(keycloakSession, ISSUER_DID,
				bearerTokenAuthenticator, Clock.systemUTC(),
				cacheStore);
	}

	@ParameterizedTest
	@MethodSource("provideClients")
	void testGetIssuerData(Stream<ClientModel> clientModelStream, ExpectedResult<IssuerMetaData> expectedResult)
			throws URISyntaxException {
		KeycloakContext context = mock(KeycloakContext.class);
		RealmModel realmModel = mock(RealmModel.class);
		when(realmModel.getId()).thenReturn("test");
		when(realmModel.getClientsStream()).thenReturn(clientModelStream);
		KeycloakUriInfo keycloakUriInfo = mock(KeycloakUriInfo.class);
		when(keycloakUriInfo.getBaseUri()).thenReturn(new URI("http://localhost:8080"));
		when(context.getUri(any())).thenReturn(keycloakUriInfo);
		when(keycloakSession.getContext()).thenReturn(context);
		when(context.getRealm()).thenReturn(realmModel);

		Response metaDataResponse = testProvider.getIssuerMetadata(ISSUER_DID);
		assertEquals(HttpStatus.SC_OK, metaDataResponse.getStatus(), expectedResult.getMessage());
		assertEquals(expectedResult.getExpectedResult(),
				OBJECT_MAPPER.convertValue(metaDataResponse.getEntity(), IssuerMetaData.class),
				expectedResult.getMessage());
	}

	public static Stream<Arguments> provideClients() throws MalformedURLException {
		return Stream.of(
				Arguments.of(
						Stream.of(
								getSiopClient("did:key:1", Map.of("vctypes_MyType", FormatVO.JWT_VC_JSON_LD.toString()),
										List.of())),
						new ExpectedResult<>(
								SIOP2IntegrationTest.getMetaData(
										List.of(new SupportedCredential("MyType", FormatVO.JWT_VC_JSON_LD)),
										ISSUER_DID),
								"Issuer data wioth the clients types should be returend.")
				),
				Arguments.of(
						Stream.of(
								getSiopClient("did:key:1", Map.of("vctypes_MyType",
												FormatVO.JWT_VC_JSON_LD.toString() + "," + FormatVO.LDP_VC),
										List.of())),
						new ExpectedResult<>(
								SIOP2IntegrationTest.getMetaData(
										List.of(new SupportedCredential("MyType", FormatVO.JWT_VC_JSON_LD),
												new SupportedCredential("MyType", FormatVO.LDP_VC)), ISSUER_DID),
								"Issuer data with the clients types and multiple formats should be returned.")
				),
				Arguments.of(
						Stream.of(
								getSiopClient("did:key:1", Map.of("vctypes_MyType", FormatVO.JWT_VC_JSON_LD.toString()),
										List.of()),
								getSiopClient("did:key:2",
										Map.of("vctypes_MyOtherType", FormatVO.JWT_VC_JSON_LD.toString()),
										List.of())),
						new ExpectedResult<>(
								SIOP2IntegrationTest.getMetaData(
										List.of(new SupportedCredential("MyType", FormatVO.JWT_VC_JSON_LD),
												new SupportedCredential("MyOtherType", FormatVO.JWT_VC_JSON_LD)),
										ISSUER_DID),
								"Issuer data with multiple clients types should be returned.")
				),
				Arguments.of(
						Stream.of(
								getSiopClient("did:key:1", Map.of("vctypes_MyType", FormatVO.JWT_VC_JSON_LD.toString()),
										List.of()),
								getSiopClient("did:key:2", Map.of("vctypes_MyType", FormatVO.LDP_VC.toString()),
										List.of())),
						new ExpectedResult<>(
								SIOP2IntegrationTest.getMetaData(
										List.of(new SupportedCredential("MyType", FormatVO.LDP_VC),
												new SupportedCredential("MyType", FormatVO.JWT_VC_JSON_LD)),
										ISSUER_DID),
								"Issuer data with multiple clients formats should be returned.")
				),
				Arguments.of(
						Stream.of(
								getSiopClient("did:key:1", Map.of("vctypes_MyType", FormatVO.JWT_VC_JSON_LD.toString(),
												"vctypes_MyOtherType", FormatVO.JWT_VC_JSON_LD.toString()),
										List.of())),
						new ExpectedResult<>(
								SIOP2IntegrationTest.getMetaData(
										List.of(new SupportedCredential("MyOtherType", FormatVO.JWT_VC_JSON_LD),
												new SupportedCredential("MyType", FormatVO.JWT_VC_JSON_LD)),
										ISSUER_DID),
								"Issuer data with multiple typses should be returned.")
				)
		);
	}

	private static Arguments getArguments(UserModel um, Map<ClientModel, List<RoleModel>> clients,
			ExpectedResult expectedResult) {
		return Arguments.of(um,
				clients.keySet().stream(),
				clients.entrySet()
						.stream()
						.filter(e -> e.getValue() != null)
						.collect(
								Collectors.toMap(Map.Entry::getKey, e -> ((List) e.getValue()).stream(),
										(e1, e2) -> e1)),
				expectedResult);
	}

	private static Stream<Arguments> provideUserAndClients() {
		return Stream.of(
				getArguments(getUserModel("e@mail.org", "Happy", "User"),
						Map.of(getSiopClient("did:key:1",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"MyType_claims", "email,firstName,familyName,roles"),
										List.of("MyRole")),
								List.of(getRoleModel("MyRole"))),
						new ExpectedResult<>(
								getVCRequest(Set.of(new Role(Set.of("MyRole"), "did:key:1")), "e@mail.org", "Happy",
										"User",
										Map.of()), "A valid VCRequest should have been sent to Walt-ID")
				),
				getArguments(getUserModel("e@mail.org", null, "User"),
						Map.of(getSiopClient("did:key:1",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"MyType_claims", "email,firstName,familyName,roles"),
										List.of("MyRole")),
								List.of(getRoleModel("MyRole"))),
						new ExpectedResult<>(
								getVCRequest(Set.of(new Role(Set.of("MyRole"), "did:key:1")), "e@mail.org",
										null,
										"User",
										Map.of()), "A valid VCRequest should have been sent to Walt-ID")
				),
				getArguments(
						getUserModel("e@mail.org", null, null),
						Map.of(getSiopClient("did:key:1",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"MyType_claims", "email,firstName,familyName,roles"),
										List.of("MyRole")),
								List.of(getRoleModel("MyRole"))),
						new ExpectedResult<>(
								getVCRequest(Set.of(new Role(Set.of("MyRole"), "did:key:1")), "e@mail.org",
										null,
										null,
										Map.of()), "A valid VCRequest should have been sent to Walt-ID")
				),
				getArguments(
						getUserModel(null, null, null),
						Map.of(getSiopClient("did:key:1",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"MyType_claims", "email,firstName,familyName,roles"),
										List.of("MyRole")),
								List.of(getRoleModel("MyRole"))),
						new ExpectedResult<>(
								getVCRequest(Set.of(new Role(Set.of("MyRole"), "did:key:1")), null, null,
										null,
										Map.of()), "A valid VCRequest should have been sent to Walt-ID")
				),
				getArguments(
						getUserModel(null, null, null),
						Map.of(getSiopClient("did:key:1",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"MyType_claims", "email,firstName,familyName,roles"),
										List.of("MyRole", "MySecondRole")),
								List.of(getRoleModel("MyRole"), getRoleModel("MySecondRole"))),
						new ExpectedResult<>(
								getVCRequest(Set.of(new Role(Set.of("MyRole", "MySecondRole"), "did:key:1")),
										null,
										null,
										null,
										Map.of()), "Multiple roles should be included")
				),
				getArguments(
						getUserModel(null, null, null),
						Map.of(getSiopClient("did:key:1",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"MyType_claims", "email,firstName,familyName,roles"),
										List.of("MyRole", "MySecondRole")),
								List.of(getRoleModel("MyRole"))),
						new ExpectedResult<>(
								getVCRequest(Set.of(new Role(Set.of("MyRole"), "did:key:1")),
										null,
										null,
										null,
										Map.of()), "Only assigned roles should be included.")
				),
				getArguments(
						getUserModel(null, null, null),
						Map.of(getSiopClient("did:key:1",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"MyType_claims", "email,firstName,familyName,roles"),
										List.of("MyRole", "MySecondRole")),
								List.of(getRoleModel("MyRole"), getRoleModel("MySecondRole")),
								getSiopClient("did:key:2",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"MyType_claims", "email,firstName,familyName,roles"),
										List.of("AnotherRole")),
								List.of(getRoleModel("AnotherRole"))),
						new ExpectedResult<>(
								getVCRequest(Set.of(new Role(Set.of("MyRole", "MySecondRole"), "did:key:1"),
												new Role(Set.of("AnotherRole"), "did:key:2")),
										null,
										null,
										null,
										Map.of()), "The request should contain roles from both clients")
				),
				getArguments(
						getUserModel(null, null, null),
						Map.of(getSiopClient("did:key:1",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"MyType_claims", "email,firstName,familyName,roles"),
										List.of("MyRole", "MySecondRole")),
								List.of(getRoleModel("MyRole"), getRoleModel("MySecondRole")),
								getSiopClient("did:key:2",
										Map.of("vctypes_AnotherType", FormatVO.LDP_VC.toString(),
												"AnotherType_claims", "email,firstName,familyName,roles"),
										List.of("AnotherRole")),
								List.of(getRoleModel("AnotherRole"))),
						new ExpectedResult<>(
								getVCRequest(Set.of(new Role(Set.of("MyRole", "MySecondRole"), "did:key:1")),
										null,
										null,
										null,
										Map.of()), "Only roles for supported clients should be included.")
				),
				getArguments(
						getUserModel(null, null, null),
						Map.of(getSiopClient("did:key:1",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"vc_additional", "claim",
												"MyType_claims", "email,firstName,familyName,roles,additional,more"),
										List.of("MyRole", "MySecondRole")),
								List.of(getRoleModel("MyRole"), getRoleModel("MySecondRole")),
								getSiopClient("did:key:2",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"vc_more", "claims",
												"MyType_claims", "email,firstName,familyName,roles,additional,more"),
										List.of("AnotherRole")),
								List.of(getRoleModel("AnotherRole"))),
						new ExpectedResult<>(
								getVCRequest(Set.of(new Role(Set.of("MyRole", "MySecondRole"), "did:key:1"),
												new Role(Set.of("AnotherRole"), "did:key:2")), null,
										null,
										null,
										Map.of("additional", "claim", "more", "claims")),
								"Additional claims should be included.")
				),
				getArguments(
						getUserModel(null, null, null),
						Map.of(getSiopClient("did:key:1",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"vc_additional", "claim",
												"MyType_claims", "email,firstName,familyName,roles,additional"),
										List.of("MyRole", "MySecondRole")),
								List.of(getRoleModel("MyRole"), getRoleModel("MySecondRole")),
								getSiopClient("did:key:2",
										Map.of("vctypes_MyType", FormatVO.LDP_VC.toString(),
												"vc_additional", "claim",
												"MyType_claims", "email,firstName,familyName,roles,additional"),
										List.of("AnotherRole")),
								List.of(getRoleModel("AnotherRole"))),
						new ExpectedResult<>(
								getVCRequest(Set.of(new Role(Set.of("MyRole", "MySecondRole"), "did:key:1"),
												new Role(Set.of("AnotherRole"), "did:key:2")), null,
										null,
										null,
										Map.of("additional", "claim")),
								"Additional claims should be included.")
				)
		);
	}

	private static VCRequest getVCRequest(Set<Role> roles, String email, String firstName, String lastName,
			Map<String, String> additionalClaims) {
		return VCRequest.builder()
				.templateId("MyType")
				.config(VCConfig.builder()
						.issuerDid(ISSUER_DID)
						.proofType(LD_PROOF_TYPE)
						.build())
				.credentialData(VCData.builder()
						.credentialSubject(
								VCClaims.builder()
										.roles(roles)
										.email(email)
										.firstName(firstName)
										.familyName(lastName)
										.additionalClaims(additionalClaims)
										.build()
						).build())
				.build();
	}

	private static Stream<Arguments> provideTypesAndClients() {
		return Stream.of(
				Arguments.of(Stream.of(getOidcClient(), getNullClient(), getSiopClient(
								Map.of("vctypes_TestType", FormatVO.LDP_VC.toString()))),
						new ExpectedResult<>(Set.of(new SupportedCredential("TestType", FormatVO.LDP_VC)),
								"The list of configured types should be returned.")),
				Arguments.of(Stream.of(getOidcClient(), getNullClient()),
						new ExpectedResult<>(Set.of(), "An empty list should be returned if nothing is configured.")),
				Arguments.of(Stream.of(),
						new ExpectedResult<>(Set.of(), "An empty list should be returned if nothing is configured.")),
				Arguments.of(
						Stream.of(getSiopClient(Map.of("vctypes_TestType", FormatVO.LDP_VC.toString(),
								"another", "attribute"))),
						new ExpectedResult<>(Set.of(new SupportedCredential("TestType", FormatVO.LDP_VC)),
								"The list of configured types should be returned.")),
				Arguments.of(Stream.of(getSiopClient(
								Map.of("vctypes_TestTypeA", FormatVO.LDP_VC.toString(), "vctypes_TestTypeB",
										FormatVO.LDP_VC.toString()))),
						new ExpectedResult<>(
								Set.of(new SupportedCredential("TestTypeA", FormatVO.LDP_VC),
										new SupportedCredential("TestTypeB", FormatVO.LDP_VC)),
								"The list of configured types should be returned.")),
				Arguments.of(Stream.of(
								getSiopClient(Map.of()),
								getSiopClient(
										Map.of("vctypes_TestTypeA", FormatVO.LDP_VC.toString(), "vctypes_TestTypeB",
												FormatVO.LDP_VC.toString()))),
						new ExpectedResult<>(
								Set.of(new SupportedCredential("TestTypeA", FormatVO.LDP_VC),
										new SupportedCredential("TestTypeB", FormatVO.LDP_VC)),
								"The list of configured types should be returned.")),
				Arguments.of(Stream.of(
								getSiopClient(null),
								getSiopClient(
										Map.of("vctypes_TestTypeA", FormatVO.LDP_VC.toString(), "vctypes_TestTypeB",
												FormatVO.LDP_VC.toString()))),
						new ExpectedResult<>(
								Set.of(new SupportedCredential("TestTypeA", FormatVO.LDP_VC),
										new SupportedCredential("TestTypeB", FormatVO.LDP_VC)),
								"The list of configured types should be returned.")),
				Arguments.of(Stream.of(
								getSiopClient(Map.of("vctypes_AnotherType", FormatVO.LDP_VC.toString())),
								getSiopClient(
										Map.of("vctypes_TestTypeA", FormatVO.LDP_VC.toString(), "vctypes_TestTypeB",
												FormatVO.LDP_VC.toString()))),
						new ExpectedResult<>(
								Set.of(new SupportedCredential("TestTypeA", FormatVO.LDP_VC),
										new SupportedCredential("TestTypeB", FormatVO.LDP_VC),
										new SupportedCredential("AnotherType", FormatVO.LDP_VC)),
								"The list of configured types should be returned.")),
				Arguments.of(Stream.of(
								getSiopClient(
										Map.of("vctypes_AnotherType", FormatVO.LDP_VC.toString(), "vctypes_AndAnother",
												FormatVO.LDP_VC.toString())),
								getSiopClient(
										Map.of("vctypes_TestTypeA", FormatVO.LDP_VC.toString(), "vctypes_TestTypeB",
												FormatVO.LDP_VC.toString()))),
						new ExpectedResult<>(
								Set.of(new SupportedCredential("TestTypeA", FormatVO.LDP_VC),
										new SupportedCredential("TestTypeB", FormatVO.LDP_VC),
										new SupportedCredential("AnotherType", FormatVO.LDP_VC),
										new SupportedCredential("AndAnother", FormatVO.LDP_VC)),
								"The list of configured types should be returned."))
		);
	}

	private static UserModel getUserModel(String email, String firstName, String lastName) {
		UserModel userModel = mock(UserModel.class);
		when(userModel.getEmail()).thenReturn(email);
		when(userModel.getFirstName()).thenReturn(firstName);
		when(userModel.getLastName()).thenReturn(lastName);
		return userModel;
	}

	private static RoleModel getRoleModel(String name) {
		RoleModel roleModel = mock(RoleModel.class);
		when(roleModel.getName()).thenReturn(name);
		return roleModel;
	}

	private static ClientModel getOidcClient() {
		ClientModel clientA = mock(ClientModel.class);
		when(clientA.getProtocol()).thenReturn("OIDC");
		return clientA;
	}

	private static ClientModel getNullClient() {
		ClientModel clientA = mock(ClientModel.class);
		when(clientA.getProtocol()).thenReturn(null);
		return clientA;
	}

	private static ClientModel getSiopClient(String clientId, Map<String, String> attributes, List<String> roles) {
		Stream<RoleModel> roleModelStream = roles.stream().map(role -> {
			RoleModel roleModel = mock(RoleModel.class);
			when(roleModel.getName()).thenReturn(role);
			return roleModel;
		});
		ClientModel clientA = mock(ClientModel.class);
		when(clientA.getProtocol()).thenReturn(SIOP2LoginProtocolFactory.PROTOCOL_ID);
		when(clientA.getAttributes()).thenReturn(attributes);
		when(clientA.getClientId()).thenReturn(clientId);
		when(clientA.getRolesStream()).thenReturn(roleModelStream);
		return clientA;
	}

	private static ClientModel getSiopClient(Map<String, String> attributes) {
		return getSiopClient(null, attributes, List.of());
	}
}