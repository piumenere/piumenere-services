package piumenere.services.controller;

import java.util.Collection;
import piumenere.services.entities.Deletable;
import piumenere.services.store.DeletableStore;

public abstract class AbstractDeletableController<T extends Deletable, S extends DeletableStore<T>> extends AbstractEditableController<T,S> implements DeletableController<T> {

    @Override
    public <D extends T> void delete(Collection<D> entities) {
        getStore().delete(entities);
    }
    
}