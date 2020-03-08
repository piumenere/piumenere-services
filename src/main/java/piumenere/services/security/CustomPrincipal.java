package piumenere.services.security;

import java.io.Serializable;
import java.security.Principal;
import javax.security.enterprise.CallerPrincipal;
import piumenere.services.entities.Person;

public class CustomPrincipal extends SerializablePrincipal implements Principal, Serializable {
    
    private final Person user;
    
    public CustomPrincipal(Person user) {
        this.user = user;
    }

    public Person getUser() {
        return user;
    }
    
}
