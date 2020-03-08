package piumenere.services.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class Deletable extends Editable {

    @Column(nullable = false)
    private Boolean deleted;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @PrePersist
    protected void onPrePersistDeletable() {
        if (Objects.isNull(getDeleted())){
            setDeleted(Boolean.FALSE);
        }
    }
    
    @PreUpdate
    protected void onPreUpdateDeletable() {
        if (Objects.isNull(getDeleted())){
            setDeleted(Boolean.FALSE);
        }
    }

}
