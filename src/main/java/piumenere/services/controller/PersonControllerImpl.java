package piumenere.services.controller;

import javax.ejb.Stateless;
import piumenere.services.entities.Person;
import piumenere.services.store.EditableStore;

@Stateless
public class PersonControllerImpl extends AbstractEditableController<Person, EditableStore<Person>> implements EditableController<Person> {
    
}
