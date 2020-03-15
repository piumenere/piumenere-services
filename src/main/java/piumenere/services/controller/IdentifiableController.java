package piumenere.services.controller;

import java.util.Collection;
import piumenere.services.entities.Identifiable;
import piumenere.services.find.FindCriteria;
import piumenere.services.find.FindOrder;
import piumenere.services.find.FindResult;
import piumenere.services.generics.SingleGenericClass;

public interface IdentifiableController<T extends Identifiable> extends SingleGenericClass<T> {
    public <D extends T> FindResult<D> find(Collection<FindCriteria<D>> criterias, Collection<FindOrder<D>> orders, Integer quantity, Integer offset);
}
