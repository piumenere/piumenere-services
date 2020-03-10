package piumenere.services.store;

import java.util.Collection;
import piumenere.services.entities.Identifiable;
import piumenere.services.find.FindCriteria;
import piumenere.services.find.FindOrder;
import piumenere.services.find.FindResult;

public interface IdentifiableStore<T extends Identifiable> {
    public <D extends T> FindResult<D> find(Collection<FindCriteria<D>> criterias, Collection<FindOrder<D>> orders, Integer quantity, Integer offset);
}
