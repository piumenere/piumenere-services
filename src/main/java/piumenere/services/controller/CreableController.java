package piumenere.services.controller;

import java.util.Collection;
import piumenere.services.entities.Creable;

public interface CreableController<T extends Creable>  extends IdentifiableController<T> {
    public <D extends T> Collection<D> create(Collection<D> entities);
}