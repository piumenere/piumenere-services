package piumenere.services.store;

import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import piumenere.services.entities.Creable;

public abstract class AbstractCreableStore<T extends Creable> extends AbstractIdentifiableStore<T> implements CreableStore<T> {

    @Override
    public <D extends T> Collection<D> create(Collection<D> entities) {
        CollectionUtils.emptyIfNull(entities).forEach(entity -> {
            getEntityManager().persist(entity);
        });
        
        getEntityManager().flush();
        CollectionUtils.emptyIfNull(entities).forEach(entity -> getEntityManager().detach(entity));
        return entities;
    }

}
