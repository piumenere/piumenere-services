package piumenere.services.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class Validable extends Deletable {

    @Column(nullable = false)
    private Boolean valid;

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @PrePersist
    protected void onPrePersistValidable() {
        if (Objects.isNull(getValid())){
            setValid(Boolean.TRUE);
        }
    }
    
    @PreUpdate
    protected void onPreUpdateValidable() {
        if (Objects.isNull(getValid())){
            setValid(Boolean.TRUE);
        }
    }

}
