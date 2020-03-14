package piumenere.services.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Person extends Editable {

    @Column
    private String credential;

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
    
}
