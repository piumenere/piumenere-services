package piumenere.services.resources;

import java.util.Collection;
import piumenere.services.entities.Deletable;

public interface DeletableResource<T extends Deletable> extends EditableResource<T> {
    
    public void delete(Collection<T> resources);
    
}
