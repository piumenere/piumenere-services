package piumenere.services.find;

import java.util.Objects;
import piumenere.services.entities.Identifiable;
import java.util.Optional;

public class SimpleCriteria<D extends Identifiable> extends FindCriteria<D> {
        
    private D criteria;

    public D getCriteria() {
        return criteria;
    }

    public void setCriteria(D criteria) {
        this.criteria = criteria;
    }

    @Override
    public Optional<Class<D>> getCriteriaClass() {
        if (Objects.isNull(criteria)){
            return Optional.empty();
        }
        return Optional.of((Class<D>) criteria.getClass());
    }
    
}
