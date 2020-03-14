package piumenere.services.security;

import java.io.Serializable;
import java.security.Principal;
import piumenere.services.entities.Person;

public class CustomPrincipal extends SerializablePrincipal implements Principal, Serializable {
    
    private final Person user;
    
    public CustomPrincipal(String name, Person user) {
        super(name);
        this.user = user;
    }

    public Person getUser() {
        return user;
    }
    
}
