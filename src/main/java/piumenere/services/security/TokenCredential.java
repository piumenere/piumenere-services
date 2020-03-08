package piumenere.services.security;

import javax.security.enterprise.credential.Credential;

public class TokenCredential implements Credential {
    
    private final String token;
    
    public TokenCredential(String token) {
        this.token = token;
    }
    
    public String getToken() {
        return token;
    }
    
}
