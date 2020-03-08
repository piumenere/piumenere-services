package piumenere.services.security;

import com.restfb.DefaultFacebookClient;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import org.slf4j.Logger;

@ApplicationScoped
public class FacebookIdentityStore implements TokenIdentityStore {
    
    @Resource(lookup = "java:global/facebookAppSecret")
    protected String facebookAppSecret;
    
    @Inject
    protected Logger logger;
    
    @Override
    public CredentialValidationResult validate(TokenCredential credential) {

        try {
            DefaultFacebookClient client = new DefaultFacebookClient(credential.getToken(), facebookAppSecret, Version.LATEST);
            User me = client.fetchObject("me", User.class);
            System.out.println("ciccio2");
        } catch (FacebookOAuthException exception) {
            logger.error(exception.getErrorMessage(), exception);
        }

        return CredentialValidationResult.NOT_VALIDATED_RESULT;

    }

}
