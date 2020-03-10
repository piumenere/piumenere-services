package piumenere.services.store;

import javax.enterprise.context.ApplicationScoped;
import piumenere.services.entities.Person;

@ApplicationScoped
public class PersonStoreImpl extends AbstractEditableStore<Person> implements EditableStore<Person> {

}