package piumenere.services.entities;

import java.time.Instant;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class Editable extends Creable {

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date edited;

    public Date getEdited() {
        return edited;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }
    
    @PrePersist
    protected void onPrePersistEditable() {
        setEdited(Date.from(Instant.now()));
    }
    
    @PreUpdate
    protected void onPreUpdateEditable() {
        setEdited(Date.from(Instant.now()));
    }
    
}
