package piumenere.services.find;

import java.util.Optional;
import piumenere.services.entities.Identifiable;

public class NegateCriteria<D extends Identifiable> extends FindCriteria<D> {

    private FindCriteria<D> negated;

    public FindCriteria<D> getNegated() {
        return negated;
    }

    public void setNegated(FindCriteria<D> negated) {
        this.negated = negated;
    }
    
    @Override
    public Optional<Class<D>> getCriteriaClass() {
        return negated.getCriteriaClass();
    }
    
}
