package piumenere.services.resources;

import java.util.Collection;
import piumenere.services.entities.Creable;

public interface CreableResource<T extends Creable> extends IdentifiableResource<T> {
    
    public Collection<T> create(Collection<T> resources);

}
