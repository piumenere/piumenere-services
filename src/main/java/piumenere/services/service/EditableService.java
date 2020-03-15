package piumenere.services.service;

import java.util.Collection;
import piumenere.services.entities.Editable;

public interface EditableService<T extends Editable> extends CreableService<T> {
    
    public Collection<T> edit(Collection<T> resources);
    
}
