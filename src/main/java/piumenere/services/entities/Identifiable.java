package piumenere.services.entities;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;

@MappedSuperclass
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Identifiable implements Serializable {
    
    @Id
    private String id;

    @Version
    private Long version;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Identifiable)) {
            return false;
        }
        Identifiable other = Identifiable.class.cast(object);
        return !((Objects.isNull(this.getId()) && Objects.isNull(other.getId())) || (Objects.nonNull(this.getId()) && !Objects.equals(this.getId(), other.getId())));
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"[ id=" + id + " ]";
    }
    
    @PrePersist
    protected void onPrePersistIdentifiable() {
        if (Objects.isNull(getVersion())){
            setId(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));
        }
    }
    
    public static <T extends Identifiable> T getIdentity(T toCopy) {
        if (Objects.isNull(toCopy)){
            return null;
        }
        if (Objects.isNull(toCopy.getId()) || Objects.isNull(toCopy.getVersion())){
            throw new RuntimeException(toCopy.getClass().getSimpleName() + " Empty id or version");
        }
        T newInstance;
        try {
            newInstance = (T) toCopy.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        newInstance.setId(toCopy.getId());
        newInstance.setVersion(toCopy.getVersion());
        return newInstance;
    }
    
}

