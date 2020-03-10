package piumenere.services.find;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.reflections.ReflectionUtils;
import piumenere.services.entities.Identifiable;

abstract public class FindCriteria<T extends Identifiable> {

    public abstract Optional<Class<T>> getCriteriaClass();
    
    public static <D extends Identifiable> Optional<Class<D>> getSpecializedAncestor(Collection<FindCriteria<D>> criterias){
    
        Set<Class<?>> commonAncestors = new HashSet<>();
        
        for (FindCriteria<D> criteria : criterias) {
            Optional<Class<D>> criteriaClass = criteria.getCriteriaClass();
            
            if (criteriaClass.isEmpty()){
                continue;
            }
            
            Set<Class<?>> allSuperTypes = ReflectionUtils.getAllSuperTypes(criteriaClass.get(), t -> !t.isInterface());
            allSuperTypes.add(criteriaClass.get());
            
            if (CollectionUtils.isEmpty(commonAncestors)){
                commonAncestors.addAll(allSuperTypes);
            } else {
                commonAncestors = SetUtils.intersection(commonAncestors, allSuperTypes);
            }
        }

        Optional<Class<?>> specialized = Optional.empty();
        
        for (Class<?> ancestor : commonAncestors) {
            if (!specialized.isPresent() || specialized.get().isAssignableFrom(ancestor)) {
                specialized = Optional.of(ancestor);
            }
        }
        
        return specialized.map(spec -> (Class<D>) spec);
        
    }
    
}
