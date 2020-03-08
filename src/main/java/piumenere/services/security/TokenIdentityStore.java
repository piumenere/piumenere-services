package piumenere.services.security;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

public interface TokenIdentityStore extends IdentityStore {
    
    public CredentialValidationResult validate(TokenCredential credential);
    
}
