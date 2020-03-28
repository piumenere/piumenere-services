package piumenere.services;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import piumenere.services.entities.Person;
import piumenere.services.entities.PersonRole;
import piumenere.services.entities.RoleType;
import piumenere.services.store.CreableStore;
import piumenere.services.utils.StoreUtils;

@Singleton
@Startup
public class Initializer {

    @Resource(lookup = "java:global/superadmins")
    private String superadmins;
    
    @Inject
    private CreableStore<Person> personStore;
    
    @Inject
    private CreableStore<PersonRole> personRoleStore;
    
    @PostConstruct
    protected void initialize() {
        Arrays.asList(superadmins.split(",")).forEach(superadmin -> {
            Person personToFind = new Person();
            personToFind.setCredential(superadmin);
            Person person = StoreUtils.createIfNotFound(personToFind, personToFind, personStore);
            Arrays.asList(RoleType.values()).forEach(roleType -> {
            PersonRole personRoleToFind = new PersonRole();
            personRoleToFind.setValid(Boolean.TRUE);
            personRoleToFind.setDeleted(Boolean.FALSE);
            personRoleToFind.setPerson(Person.getIdentity(person));
            personRoleToFind.setType(roleType);
            StoreUtils.createIfNotFound(personRoleToFind, personRoleToFind, personRoleStore);
            });
        });
    }

}
