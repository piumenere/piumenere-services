package piumenere.services.resources;

import java.util.Collection;
import piumenere.services.entities.Editable;

public interface EditableResource<T extends Editable> extends CreableResource<T> {
    
    public Collection<T> edit(Collection<T> resources);
    
}
