package piumenere.services.security;

import com.restfb.DefaultFacebookClient;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import org.slf4j.Logger;
import piumenere.services.entities.Person;
import piumenere.services.find.FindResult;
import piumenere.services.store.EditableStore;
import piumenere.services.utils.CriteriaUtils;

@ApplicationScoped
public class FacebookIdentityStore implements TokenIdentityStore {
    
    @Resource(lookup = "java:global/facebookAppSecret")
    protected String facebookAppSecret;
    
    @Inject
    protected Logger logger;
    
    @Inject
    protected EditableStore<Person> personStore;
    
    @Override
    public CredentialValidationResult validate(TokenCredential credential) {

        try {
            FindResult<Person> find = personStore.find(Arrays.asList(CriteriaUtils.getInstance(new Person())), null, 1, 0);
            DefaultFacebookClient client = new DefaultFacebookClient(credential.getToken(), facebookAppSecret, Version.LATEST);
            User me = client.fetchObject("me", User.class);
            System.out.println("ciccio2");
        } catch (FacebookOAuthException exception) {
            logger.error(exception.getErrorMessage(), exception);
        }

        return CredentialValidationResult.NOT_VALIDATED_RESULT;

    }

}
