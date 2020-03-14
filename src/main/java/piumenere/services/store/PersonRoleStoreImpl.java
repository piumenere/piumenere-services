package piumenere.services.store;

import javax.enterprise.context.ApplicationScoped;
import piumenere.services.entities.PersonRole;

@ApplicationScoped
public class PersonRoleStoreImpl extends AbstractDeletableStore<PersonRole> implements DeletableStore<PersonRole> {

}