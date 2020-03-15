package piumenere.services.service;

import java.util.Collection;
import piumenere.services.entities.Creable;

public interface CreableService<T extends Creable> extends IdentifiableService<T> {
    
    public Collection<T> create(Collection<T> resources);

}
