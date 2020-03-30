package piumenere.services.resources;

import java.util.Collection;
import piumenere.services.entities.Identifiable;
import piumenere.services.find.FindCriteria;
import piumenere.services.find.FindOrder;
import piumenere.services.find.FindResult;

public interface IdentifiableResource<T extends Identifiable> {
    
    public FindResult<T> find(Collection<FindCriteria<T>> criterias, Collection<FindOrder<T>> orders, Integer quantity, Integer offset);
    
}
