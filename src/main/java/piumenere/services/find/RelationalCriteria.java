package piumenere.services.find;

import java.util.Map;
import java.util.Optional;
import piumenere.services.entities.Identifiable;

public class RelationalCriteria<D extends Identifiable, R extends Identifiable> extends FindCriteria<D> {
    
    private FindCriteria<D> related;
    private Map<String, FindCriteria<R>> relations;

    public FindCriteria<D> getRelated() {
        return related;
    }

    public void setRelated(FindCriteria<D> related) {
        this.related = related;
    }

    public Map<String, FindCriteria<R>> getRelations() {
        return relations;
    }

    public void setRelations(Map<String, FindCriteria<R>> relations) {
        this.relations = relations;
    }
    
    @Override
    public Optional<Class<D>> getCriteriaClass() {
        return related.getCriteriaClass();
    }
    
}
