package piumenere.services.controller;

import java.util.Collection;
import piumenere.services.entities.Editable;
import piumenere.services.store.EditableStore;

public abstract class AbstractEditableController<T extends Editable, S extends EditableStore<T>> extends AbstractCreableController<T,S> implements EditableController<T> {

    @Override
    public <D extends T> Collection<D> edit(Collection<D> entities) {
        return getStore().edit(entities);
    }
    
}
