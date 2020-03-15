package piumenere.services.controller;

import java.util.Collection;
import piumenere.services.entities.Deletable;

public interface DeletableController<T extends Deletable> extends EditableController<T> {
    public <D extends T> void delete(Collection<D> entities);
}
