
package com.demo.auth.authorization.oauth.token;

import com.demo.auth.authorization.core.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Slf4j
public class IdTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    @Override
    public void customize(JwtEncodingContext context) {
        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            context.getClaims().claims((claims) -> {
                if (context.getPrincipal().getPrincipal() instanceof Account account) {
                    claims.put("email", account.getEmail());
                }
            });
        }
    }
}
