package com.sap.exciseduty.utility;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;

import org.cloudfoundry.identity.client.UaaContext;
import org.cloudfoundry.identity.client.UaaContextFactory;
import org.cloudfoundry.identity.client.token.GrantType;
import org.cloudfoundry.identity.client.token.TokenRequest;
import org.cloudfoundry.identity.uaa.oauth.token.CompositeAccessToken;
import org.springframework.security.jwt.JwtHelper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sap.xs2.security.commons.SAPPropertyPlaceholderConfigurer;

@Deprecated
public final class ClientCredentialFlow {

    private static final String UAA_AUTHORIZE_PATH = "/oauth/authorize";
    private static final String UAA_TOKEN_PATH = "/oauth/token";
    private static final String XS_UAA_CLIENTID = "xs.uaa.clientid";
    private static final String XS_UAA_CLIENTSECRET = "xs.uaa.clientsecret";
    private static final String XS_UAA_URL = "xs.uaa.url";

    private static HashMap<String, CachedJwtToken> tokenCache = new HashMap<>(); // Add TenantId to lookup/structure
                                                                                 // once implementing Multi Tenancy

    private static String getJwtToken(String clientId, String clientSecret, String uaaUrl) throws URISyntaxException {
        CachedJwtToken cachedToken = tokenCache.get(clientId);
        if (cachedToken == null || !cachedToken.isValid()) {

            URI xsuaaUri = new URI(uaaUrl);
            UaaContextFactory factory = UaaContextFactory.factory(xsuaaUri).authorizePath(UAA_AUTHORIZE_PATH).tokenPath(UAA_TOKEN_PATH);
            TokenRequest tokenRequest = factory.tokenRequest();
            tokenRequest.setGrantType(GrantType.CLIENT_CREDENTIALS);
            tokenRequest.setClientId(clientId);
            tokenRequest.setClientSecret(clientSecret);
            UaaContext xsUaaContext = factory.authenticate(tokenRequest);
            CompositeAccessToken token = xsUaaContext.getToken();
            cachedToken = new CachedJwtToken(token.getValue());
            tokenCache.put(clientId, cachedToken);
        }
        return cachedToken.getJwtToken();
    }

    @Deprecated
    public static String getOwnJwtToken() throws URISyntaxException {

        final SAPPropertyPlaceholderConfigurer configurer = new SAPPropertyPlaceholderConfigurer();
        final String adminClientId = configurer.readVcapServices(XS_UAA_CLIENTID, "application");
        final String adminClientSecret = configurer.readVcapServices(XS_UAA_CLIENTSECRET, "application");
        final String baseUrl = configurer.readVcapServices(XS_UAA_URL, "application");
        return getJwtToken(adminClientId, adminClientSecret, baseUrl);
    }

    static class CachedJwtToken {
        String jwtToken;
        LocalDateTime exp;

        public CachedJwtToken(String jwtToken) {

            this.jwtToken = jwtToken;

            String jwtClaimsSerialized = JwtHelper.decode(jwtToken).getClaims();
            JsonObject jsonObj = new JsonParser().parse(jwtClaimsSerialized).getAsJsonObject();

            long exp = jsonObj.get("exp").getAsLong();
            this.exp = Instant.ofEpochSecond(exp).atZone(ZoneId.systemDefault()).toLocalDateTime();

        }

        public boolean isValid() {

            Duration remaining = Duration.between(LocalDateTime.now(), exp);
            return (remaining.getSeconds() > 60);
        }

        public String getJwtToken() {
            return jwtToken;
        }

    }
}