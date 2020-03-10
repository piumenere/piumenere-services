package piumenere.services.store;

import java.util.Set;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import piumenere.services.entities.Identifiable;
import piumenere.services.entities.reflections.Reflections;
import piumenere.services.find.annotations.Values;
import piumenere.services.generics.AbstractSingleGenericClass;

public abstract class AbstractStore<T extends Identifiable> extends AbstractSingleGenericClass<T> {

    @PersistenceContext(unitName = "piumenere")
    private EntityManager entityManager;
    
    @Inject
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }   
    
    @Inject
    @Values({"piumenere"})
    private Reflections reflections;
    
    protected <D extends Identifiable> Set<Class<? extends D>> getTypes(Class<D> type){
            Set<Class<? extends D>> subTypesOf = reflections.getSubTypesOf(type);
            subTypesOf.add(type);
            return subTypesOf;
    }
    
    protected EntityManager getEntityManager(){
        return entityManager;
    }
    
    protected CriteriaBuilder getCriteriaBuilder() {
        return getEntityManager().getCriteriaBuilder();
    }
    
}
