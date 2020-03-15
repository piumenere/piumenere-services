package piumenere.services.controller;

import java.util.Collection;
import piumenere.services.entities.Creable;
import piumenere.services.store.CreableStore;

public abstract class AbstractCreableController<T extends Creable, S extends CreableStore<T>> extends AbstractIdentifiableController<T, S> implements CreableController<T> {

    @Override
    public <D extends T> Collection<D> create(Collection<D> entities) {
        return getStore().create(entities);
    }
    
}