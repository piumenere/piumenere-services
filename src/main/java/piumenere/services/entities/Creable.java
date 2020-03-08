package piumenere.services.entities;

import java.time.Instant;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class Creable extends Identifiable {

	@Column(nullable = false, updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
	private Date creation;

	public Date getCreation() {
            return creation;
	}

	public void setCreation(Date creation) {
            this.creation = creation;
	}
	
        @PrePersist
        protected void onPrePersistCreated() {
            setCreation(Date.from(Instant.now()));
        }
            
}
