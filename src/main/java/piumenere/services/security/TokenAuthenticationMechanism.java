package piumenere.services.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.CredentialValidationResult.Status;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import org.apache.commons.lang3.StringUtils;
import piumenere.services.utils.Constants;

@AutoApplySession
@ApplicationScoped
public class TokenAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private TokenIdentityStore identityStore;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isNotBlank(authorization)) {

            if (StringUtils.startsWithIgnoreCase(authorization, Constants.AUTHORIZATION_BEARER)) {

                String bearer = StringUtils.substringAfter(authorization, Constants.AUTHORIZATION_BEARER);

                if (StringUtils.isNoneBlank(bearer)) {
                    
                    CredentialValidationResult validate = this.identityStore.validate(new TokenCredential(bearer));

                    if (Status.VALID.equals(validate.getStatus())) {
                        return httpMessageContext.notifyContainerAboutLogin(validate);
                    }

                }

            }

        }

        return httpMessageContext.responseUnauthorized();
    }

}
