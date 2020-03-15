package piumenere.services.controller;

import java.util.Collection;
import java.util.Optional;
import javax.inject.Inject;
import org.slf4j.Logger;
import piumenere.services.bean.GenericBeanProvider;
import piumenere.services.entities.Identifiable;
import piumenere.services.find.FindCriteria;
import piumenere.services.find.FindOrder;
import piumenere.services.find.FindResult;
import piumenere.services.generics.AbstractDoubleGenericClass;
import piumenere.services.store.IdentifiableStore;

public abstract class AbstractIdentifiableController<T extends Identifiable, S extends IdentifiableStore<T>> extends AbstractDoubleGenericClass<T, S> implements IdentifiableController<T> {
    
    @Inject
    private Logger logger;

    @Inject
    private GenericBeanProvider genericBeanProvider;

    public Logger getLogger() {
        return logger;
    }
    
    protected S getStore(){
        Optional<S> bean = genericBeanProvider.getBean(getSecondGenericsType());
        if(!bean.isPresent()){
            logger.error("STORE NOT FOUND " + getSecondGenericsType());
        }
        return bean.get();
    }
    
    @Override
    public <D extends T> FindResult<D> find(Collection<FindCriteria<D>> criterias, Collection<FindOrder<D>> orders, Integer quantity, Integer offset){
        return getStore().find(criterias, orders, quantity, offset);
    }
    
}
