package piumenere.services.security;

import java.io.Serializable;
import java.security.Principal;
import javax.security.enterprise.CallerPrincipal;
import piumenere.services.entities.User;

public class UserPrincipal extends SerializablePrincipal implements Principal, Serializable {
    
    private final User user;
    
    public UserPrincipal(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    
}
