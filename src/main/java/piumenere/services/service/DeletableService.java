package piumenere.services.service;

import java.util.Collection;
import piumenere.services.entities.Deletable;

public interface DeletableService<T extends Deletable> extends EditableService<T> {
    
    public void delete(Collection<T> resources);
    
}
