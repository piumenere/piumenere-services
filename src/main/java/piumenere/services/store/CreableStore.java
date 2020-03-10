package piumenere.services.store;

import java.util.Collection;
import piumenere.services.entities.Creable;

public interface CreableStore<T extends Creable> extends IdentifiableStore<T> {
    public <D extends T> Collection<D> create(Collection<D> entities);
}