package piumenere.services.security;

import javax.security.enterprise.CallerPrincipal;

public abstract class SerializablePrincipal extends CallerPrincipal {
    
    public SerializablePrincipal() {
        super("");
    }
    
}
