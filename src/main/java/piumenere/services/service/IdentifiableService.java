package piumenere.services.service;

import java.util.Collection;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import piumenere.services.entities.Identifiable;
import piumenere.services.find.FindCriteria;
import piumenere.services.find.FindOrder;
import piumenere.services.find.FindResult;

public interface IdentifiableService<T extends Identifiable> {
    
    public FindResult<T> find(Collection<FindCriteria<T>> criterias, Collection<FindOrder<T>> orders, @Max(100) @Min(0) Integer quantity, @Min(0) Integer offset);
    
}
