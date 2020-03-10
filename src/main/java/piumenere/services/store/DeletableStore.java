package piumenere.services.store;

import java.util.Collection;
import piumenere.services.entities.Deletable;

public interface DeletableStore<T extends Deletable> extends EditableStore<T> {
    public <D extends T> void delete(Collection<D> entities);
}