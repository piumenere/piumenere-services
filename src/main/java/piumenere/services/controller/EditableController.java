package piumenere.services.controller;

import java.util.Collection;
import piumenere.services.entities.Editable;

public interface EditableController<T extends Editable> extends CreableController<T> {
    public <D extends T> Collection<D> edit(Collection<D> entities);
}