package piumenere.services.find;

import java.util.Collection;
import java.util.Optional;
import piumenere.services.entities.Identifiable;

public abstract class BooleanOperatorCriteria<D extends Identifiable> extends FindCriteria<D> {
    
    protected Collection<FindCriteria<D>> criterias;

    public Collection<FindCriteria<D>> getCriterias() {
        return criterias;
    }

    public void setCriterias(Collection<FindCriteria<D>> criterias) {
        this.criterias = criterias;
    }

    @Override
    public Optional<Class<D>> getCriteriaClass() {
        return FindCriteria.getSpecializedAncestor(criterias);
    }
    
}
