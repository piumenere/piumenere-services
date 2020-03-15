package piumenere.services.security;

import java.io.Serializable;
import java.security.Principal;
import piumenere.services.entities.Person;

public class CustomPrincipal extends SerializablePrincipal implements Principal, Serializable {
    
    private final Person person;
    
    public CustomPrincipal(String name, Person user) {
        super(name);
        this.person = user;
    }

    public Person getPerson() {
        return person;
    }
    
}
