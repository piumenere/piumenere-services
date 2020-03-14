package piumenere.services.security;

import com.restfb.DefaultFacebookClient;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.slf4j.Logger;
import piumenere.services.entities.Person;
import piumenere.services.entities.PersonRole;
import piumenere.services.find.FindResult;
import piumenere.services.store.IdentifiableStore;
import piumenere.services.utils.CriteriaUtils;

@ApplicationScoped
public class FacebookIdentityStore implements TokenIdentityStore {

    @Resource(lookup = "java:global/facebookAppSecret")
    protected String facebookAppSecret;

    @Inject
    protected Logger logger;

    @Inject
    protected IdentifiableStore<Person> personStore;

    @Inject
    protected IdentifiableStore<PersonRole> personRoleStore;

    @Override
    public CredentialValidationResult validate(TokenCredential credential) {

        try {
            DefaultFacebookClient client = new DefaultFacebookClient(credential.getToken(), facebookAppSecret, Version.LATEST);
            User me = client.fetchObject("me", User.class);
            Person personToFind = new Person();
            personToFind.setCredential(me.getId());
            Optional<Person> personFound = CollectionUtils.emptyIfNull(personStore.find(Arrays.asList(CriteriaUtils.getInstance(personToFind)), null, 1, 0).getResults()).stream().findFirst();
            Optional<Collection<PersonRole>> personRolesFound = personFound.map(person -> {
                PersonRole personRoleToFind = new PersonRole();
                personRoleToFind.setPerson(Person.getIdentity(person));
                return CollectionUtils.emptyIfNull(
                        personRoleStore.find(Arrays.asList(CriteriaUtils.getInstance(personRoleToFind)), null, null, null).getResults()
                );
            });

            return new CredentialValidationResult(
                    new CustomPrincipal(
                            me.getId(),
                            personFound.orElse(null)
                    ),
                    personRolesFound.map(
                            personRoles -> personRoles.stream().map(
                                    personRole -> personRole.getType().toString()
                            ).collect(Collectors.toSet())
                    ).orElse(SetUtils.EMPTY_SORTED_SET)
            );

        } catch (FacebookOAuthException exception) {
            logger.error(exception.getErrorMessage());
        }

        return CredentialValidationResult.NOT_VALIDATED_RESULT;

    }

}
