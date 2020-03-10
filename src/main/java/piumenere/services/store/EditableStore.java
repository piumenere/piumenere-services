package piumenere.services.store;

import java.util.Collection;
import piumenere.services.entities.Editable;

public interface EditableStore<T extends Editable> extends CreableStore<T> {
    public <D extends T> Collection<D> edit(Collection<D> entities);
}